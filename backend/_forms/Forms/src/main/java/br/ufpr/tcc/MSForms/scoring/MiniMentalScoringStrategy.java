package br.ufpr.tcc.MSForms.scoring;

import java.util.List;
import br.ufpr.tcc.MSForms.models.Resposta;

public class MiniMentalScoringStrategy implements ScoringStrategy {
    
    @Override
    public int calculateScore(List<Resposta> respostas) {
        int pontuacaoTotal = 0;
        
        for (Resposta resposta : respostas) {
            String valorResposta = resposta.getValor();
            
            // Para MEEM, cada resposta correta vale 1 ponto
            if ("\"O paciente respondeu corretamente\"".equals(valorResposta)) {
                pontuacaoTotal += 1;
            }
            // Respostas incorretas valem 0 pontos (não somamos nada)
        }
        
        return pontuacaoTotal;
    }
    
    @Override
    public int getMaxScore() {
        // MEEM tem 30 pontos no total
        return 30;
    }
    
    @Override
    public String getFormType() {
        return "minimental";
    }
}
