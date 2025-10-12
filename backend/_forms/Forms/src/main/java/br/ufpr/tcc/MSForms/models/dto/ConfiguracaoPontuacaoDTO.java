package br.ufpr.tcc.MSForms.models.dto;

import java.util.Map;

public class ConfiguracaoPontuacaoDTO {

    private String tipoPontuacao;
    private Map<String, Integer> mapeamentoPontos;
    private String formula;
    private Integer pontosMinimos;
    private Integer pontosMaximos;

    public ConfiguracaoPontuacaoDTO() {
    }

    public ConfiguracaoPontuacaoDTO(String tipoPontuacao, Map<String, Integer> mapeamentoPontos,
                                    String formula, Integer pontosMinimos, Integer pontosMaximos) {
        this.tipoPontuacao = tipoPontuacao;
        this.mapeamentoPontos = mapeamentoPontos;
        this.formula = formula;
        this.pontosMinimos = pontosMinimos;
        this.pontosMaximos = pontosMaximos;
    }

    public String getTipoPontuacao() {
        return tipoPontuacao;
    }

    public void setTipoPontuacao(String tipoPontuacao) {
        this.tipoPontuacao = tipoPontuacao;
    }

    public Map<String, Integer> getMapeamentoPontos() {
        return mapeamentoPontos;
    }

    public void setMapeamentoPontos(Map<String, Integer> mapeamentoPontos) {
        this.mapeamentoPontos = mapeamentoPontos;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Integer getPontosMinimos() {
        return pontosMinimos;
    }

    public void setPontosMinimos(Integer pontosMinimos) {
        this.pontosMinimos = pontosMinimos;
    }

    public Integer getPontosMaximos() {
        return pontosMaximos;
    }

    public void setPontosMaximos(Integer pontosMaximos) {
        this.pontosMaximos = pontosMaximos;
    }
}
