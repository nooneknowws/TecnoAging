package br.ufpr.tcc.MSForms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.ufpr.tcc.MSForms.models.Avaliacao;

@Service
public class ScoringService {

    @Autowired
    private GenericScoringService genericScoringService;

    public void calculateAndUpdateScore(Avaliacao avaliacao) {
        genericScoringService.calculateAndUpdateScore(avaliacao);
    }
    
    public double calculatePercentageScore(Avaliacao avaliacao) {
        if (avaliacao.getPontuacaoMaxima() == 0) {
            return 0.0;
        }
        return (double) avaliacao.getPontuacaoTotal() / avaliacao.getPontuacaoMaxima() * 100;
    }
    
    public String interpretScore(Avaliacao avaliacao) {
        String tipoFormulario = avaliacao.getFormulario().getTipo();
        int pontuacao = avaliacao.getPontuacaoTotal();
        
        switch (tipoFormulario.toLowerCase()) {
            case "minimental":
                return interpretMiniMental(pontuacao);
            case "factf":
                return interpretFactF(pontuacao, avaliacao.getPontuacaoMaxima());
            case "ivcf20":
                return interpretIVCF20(pontuacao);
            default:
                return "Interpretação não disponível para este tipo de formulário";
        }
    }
    
    private String interpretMiniMental(int pontuacao) {
        if (pontuacao >= 27) {
            return "Normal";
        } else if (pontuacao >= 24) {
            return "Comprometimento cognitivo leve";
        } else if (pontuacao >= 20) {
            return "Comprometimento cognitivo moderado";
        } else {
            return "Comprometimento cognitivo severo";
        }
    }
    
    private String interpretFactF(int pontuacao, int pontuacaoMaxima) {
        double percentual = (double) pontuacao / pontuacaoMaxima * 100;
        
        if (percentual >= 80) {
            return "Excelente qualidade de vida";
        } else if (percentual >= 60) {
            return "Boa qualidade de vida";
        } else if (percentual >= 40) {
            return "Qualidade de vida moderada";
        } else if (percentual >= 20) {
            return "Qualidade de vida ruim";
        } else {
            return "Qualidade de vida muito ruim";
        }
    }
    
    /**
     * Interpreta a pontuação do IVCF-20 (Índice de Vulnerabilidade Clínico-Funcional)
     * 
     * @param pontuacao Pontuação obtida no IVCF-20
     * @return Interpretação clínica do nível de vulnerabilidade
     */
    private String interpretIVCF20(int pontuacao) {
        if (pontuacao >= 0 && pontuacao <= 6) {
            return "Robusto - Baixo risco de vulnerabilidade. O idoso apresenta boa condição " +
                   "clínica e funcional, com independência para atividades de vida diária.";
        } else if (pontuacao >= 7 && pontuacao <= 14) {
            return "Em risco de vulnerabilidade - Risco moderado. O idoso apresenta alguns " +
                   "fatores de risco que podem evoluir para vulnerabilidade. Recomenda-se " +
                   "acompanhamento e intervenções preventivas.";
        } else if (pontuacao >= 15 && pontuacao <= 40) {
            return "Vulnerável - Alto risco de vulnerabilidade. O idoso apresenta múltiplos " +
                   "fatores de risco e comprometimentos funcionais. Necessita de cuidados " +
                   "especializados e acompanhamento multidisciplinar.";
        } else {
            return "Pontuação inválida para o IVCF-20. A pontuação deve estar entre 0 e 40 pontos.";
        }
    }
    
    /**
     * Obtém uma interpretação resumida da pontuação do IVCF-20
     * 
     * @param pontuacao Pontuação obtida no IVCF-20
     * @return Classificação resumida
     */
    public String getIVCF20Classification(int pontuacao) {
        if (pontuacao >= 0 && pontuacao <= 6) {
            return "Robusto";
        } else if (pontuacao >= 7 && pontuacao <= 14) {
            return "Em Risco";
        } else if (pontuacao >= 15 && pontuacao <= 40) {
            return "Vulnerável";
        } else {
            return "Inválido";
        }
    }
    
    /**
     * Calcula o percentual de vulnerabilidade baseado na pontuação do IVCF-20
     * 
     * @param avaliacao Avaliação com pontuação do IVCF-20
     * @return Percentual de vulnerabilidade (0-100%)
     */
    public double calculateVulnerabilityPercentage(Avaliacao avaliacao) {
        if (!"ivcf20".equalsIgnoreCase(avaliacao.getFormulario().getTipo())) {
            return 0.0;
        }
        
        int pontuacao = avaliacao.getPontuacaoTotal();
        int pontuacaoMaxima = avaliacao.getPontuacaoMaxima();
        
        if (pontuacaoMaxima == 0) {
            return 0.0;
        }
        
        return (double) pontuacao / pontuacaoMaxima * 100;
    }
    
    /**
     * Verifica se o paciente requer aplicação do Mini Mental baseado no IVCF-20
     * Conforme documento: "Baixo score → abrir MiniMental"
     * 
     * @param pontuacaoIVCF20 Pontuação obtida no IVCF-20
     * @return true se deve aplicar Mini Mental, false caso contrário
     */
    public boolean shouldApplyMiniMental(int pontuacaoIVCF20) {
        // Considera "baixo score" como pontuação que indica vulnerabilidade (15+)
        // ou risco moderado-alto (10+)
        return pontuacaoIVCF20 >= 10;
    }
}