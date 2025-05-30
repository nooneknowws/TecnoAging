package br.ufpr.tcc.MSForms.service;

import java.util.List;
import org.springframework.stereotype.Service;
import br.ufpr.tcc.MSForms.models.Avaliacao;
import br.ufpr.tcc.MSForms.models.Resposta;
import br.ufpr.tcc.MSForms.scoring.ScoringStrategy;
import br.ufpr.tcc.MSForms.scoring.ScoringStrategyFactory;

@Service
public class ScoringService {
    
    public void calculateAndUpdateScore(Avaliacao avaliacao) {
        String tipoFormulario = avaliacao.getFormulario().getTipo();
        List<Resposta> respostas = avaliacao.getRespostas();
        
        try {
            ScoringStrategy strategy = ScoringStrategyFactory.createStrategy(tipoFormulario);
            
            int pontuacaoCalculada = strategy.calculateScore(respostas);
            int pontuacaoMaxima = strategy.getMaxScore();
            
            avaliacao.setPontuacaoTotal(pontuacaoCalculada);
            avaliacao.setPontuacaoMaxima(pontuacaoMaxima);
            
        } catch (IllegalArgumentException e) {
            // Formulário sem estratégia de pontuação definida
            // Pode implementar uma estratégia padrão ou deixar pontuação zerada
            avaliacao.setPontuacaoTotal(0);
            avaliacao.setPontuacaoMaxima(0);
        }
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
}
