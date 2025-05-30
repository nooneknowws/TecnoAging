package br.ufpr.tcc.MSForms.scoring;

import java.util.List;
import br.ufpr.tcc.MSForms.models.Resposta;

public interface ScoringStrategy {
    int calculateScore(List<Resposta> respostas);
    int getMaxScore();
    String getFormType();
}