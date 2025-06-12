package br.ufpr.tcc.MSForms.scoring;

import java.util.List;
import br.ufpr.tcc.MSForms.models.Resposta;

public class IVCF20ScoringStrategy implements ScoringStrategy {
    
    private static final int MAX_SCORE = 40; // Pontuação máxima do IVCF-20
    private static final String FORM_TYPE = "ivcf20";
    
    @Override
    public int calculateScore(List<Resposta> respostas) {
        int totalScore = 0;
        
        for (Resposta resposta : respostas) {
            if (resposta.getValor() == null || resposta.getValor().trim().isEmpty()) {
                continue; // Pula respostas vazias
            }
            
            totalScore += calculateQuestionScore(resposta);
        }
        
        return totalScore;
    }
    
    /**
     * Calcula a pontuação para uma questão específica baseada na resposta
     */
    private int calculateQuestionScore(Resposta resposta) {
        String valor = resposta.getValor().trim().toLowerCase();
        String textoPergnta = resposta.getPergunta().getTexto().toLowerCase();
        
        // Questões de auto-percepção da saúde
        if (textoPergnta.contains("comparado com outras pessoas da sua idade")) {
            return calculateHealthPerceptionScore(valor);
        }
        
        if (textoPergnta.contains("comparada há um ano atrás")) {
            return calculateHealthComparisonScore(valor);
        }
        
        // Questões sobre atividades de vida diária (geralmente pontuam 2 para "Sim")
        if (textoPergnta.contains("deixou de fazer") || 
            textoPergnta.contains("tem dificuldade") ||
            textoPergnta.contains("precisa de ajuda")) {
            return valor.contains("sim") ? 2 : 0;
        }
        
        // Questões sobre quedas (modificada conforme documento)
        if (textoPergnta.contains("caiu no último ano") || textoPergnta.contains("quantas vezes")) {
            return calculateFallsScore(valor);
        }
        
        // Questões sobre medicamentos
        if (textoPergnta.contains("medicamento") || textoPergnta.contains("remédio")) {
            return calculateMedicationScore(valor, textoPergnta);
        }
        
        // Questões sobre hospitalização
        if (textoPergnta.contains("hospital") || textoPergnta.contains("internação")) {
            return valor.contains("sim") ? 2 : 0;
        }
        
        // Questões sobre problemas de saúde específicos
        if (textoPergnta.contains("problema de saúde") || 
            textoPergnta.contains("doença") ||
            textoPergnta.contains("condição de saúde")) {
            return calculateHealthProblemsScore(valor, textoPergnta);
        }
        
        // Questões sobre mobilidade
        if (textoPergnta.contains("andar") || 
            textoPergnta.contains("caminhar") ||
            textoPergnta.contains("locomoção")) {
            return calculateMobilityScore(valor);
        }
        
        // Questões sobre cognição/memória
        if (textoPergnta.contains("memória") || 
            textoPergnta.contains("esquec") ||
            textoPergnta.contains("concentr")) {
            return valor.contains("sim") || valor.contains("sempre") || valor.contains("frequentemente") ? 2 : 0;
        }
        
        // Questões sobre humor
        if (textoPergnta.contains("triste") || 
            textoPergnta.contains("deprim") ||
            textoPergnta.contains("desânimo")) {
            return valor.contains("sim") || valor.contains("sempre") || valor.contains("frequentemente") ? 1 : 0;
        }
        
        // Questões sobre audição/visão
        if (textoPergnta.contains("ouvir") || textoPergnta.contains("audição") ||
            textoPergnta.contains("ver") || textoPergnta.contains("visão")) {
            return calculateSensoryScore(valor);
        }
        
        // Score padrão para questões não categorizadas especificamente
        return calculateDefaultScore(valor);
    }
    
    private int calculateHealthPerceptionScore(String valor) {
        if (valor.contains("regular") || valor.contains("ruim") || valor.contains("péssima")) {
            return 1;
        }
        return 0; // Excelente, boa ou muito boa = 0 pontos
    }
    
    private int calculateHealthComparisonScore(String valor) {
        if (valor.contains("pior")) {
            return 1;
        }
        return 0; // Melhor ou igual = 0 pontos
    }
    
    private int calculateFallsScore(String valor) {
        // Conforme documento: Se 2 ou 3+ quedas, soma 2 pontos
        if (valor.contains("2") || valor.contains("3") || valor.contains("três") || 
            valor.contains("mais") || valor.contains("vezes")) {
            // Verifica se é 2 ou mais quedas
            if (valor.contains("2 vezes") || valor.contains("3 vezes") || 
                valor.contains("3 vezes ou mais") || valor.contains("mais")) {
                return 2;
            }
        }
        if (valor.contains("1") || valor.contains("uma vez")) {
            return 1;
        }
        return 0; // 0 quedas = 0 pontos
    }
    
    private int calculateMedicationScore(String valor, String pergunta) {
        // Questões sobre quantidade de medicamentos
        if (pergunta.contains("quantos") || pergunta.contains("número")) {
            try {
                int numMedicamentos = Integer.parseInt(valor.replaceAll("[^0-9]", ""));
                if (numMedicamentos >= 5) return 2;
                if (numMedicamentos >= 3) return 1;
                return 0;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        // Questões sobre dificuldades com medicamentos
        return valor.contains("sim") ? 1 : 0;
    }
    
    private int calculateHealthProblemsScore(String valor, String pergunta) {
        // Questões sobre número de problemas de saúde
        if (pergunta.contains("quantos") || pergunta.contains("número")) {
            try {
                int numProblemas = Integer.parseInt(valor.replaceAll("[^0-9]", ""));
                if (numProblemas >= 3) return 2;
                if (numProblemas >= 1) return 1;
                return 0;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        return valor.contains("sim") ? 1 : 0;
    }
    
    private int calculateMobilityScore(String valor) {
        if (valor.contains("muito difícil") || valor.contains("impossível")) {
            return 2;
        }
        if (valor.contains("difícil") || valor.contains("com dificuldade")) {
            return 1;
        }
        return 0; // Sem dificuldade = 0 pontos
    }
    
    private int calculateSensoryScore(String valor) {
        if (valor.contains("muito difícil") || valor.contains("não consegue")) {
            return 2;
        }
        if (valor.contains("difícil") || valor.contains("com dificuldade")) {
            return 1;
        }
        return 0;
    }
    
    private int calculateDefaultScore(String valor) {
        // Score padrão baseado em respostas comuns
        if (valor.contains("sim") || valor.contains("sempre") || valor.contains("frequentemente")) {
            return 1;
        }
        if (valor.contains("às vezes") || valor.contains("raramente")) {
            return 1;
        }
        return 0; // Não, nunca, etc. = 0 pontos
    }
    
    @Override
    public int getMaxScore() {
        return MAX_SCORE;
    }
    
    @Override
    public String getFormType() {
        return FORM_TYPE;
    }
    
    /**
     * Interpreta o resultado do IVCF-20
     * @param score Pontuação obtida
     * @return Interpretação do nível de vulnerabilidade
     */
    public String interpretScore(int score) {
        if (score >= 0 && score <= 6) {
            return "Robusto (baixo risco de vulnerabilidade)";
        } else if (score >= 7 && score <= 14) {
            return "Em risco de vulnerabilidade";
        } else if (score >= 15) {
            return "Vulnerável (alto risco)";
        }
        return "Score inválido";
    }
}