package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;

import java.util.HashMap;
import java.util.Map;

@Embeddable
public class ConfiguracaoPontuacao {

    private String tipoPontuacao;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "pontuacao_mapeamento", joinColumns = @JoinColumn(name = "pergunta_id"))
    @MapKeyColumn(name = "chave")
    @Column(name = "pontos")
    private Map<String, Integer> mapeamentoPontos = new HashMap<>();

    @Column(columnDefinition = "TEXT")
    private String formula;

    private Integer pontosMinimos;

    private Integer pontosMaximos;

    public ConfiguracaoPontuacao() {
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
