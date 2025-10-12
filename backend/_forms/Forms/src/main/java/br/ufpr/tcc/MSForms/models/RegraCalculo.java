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
public class RegraCalculo {

    private String tipoCalculo;

    @Column(columnDefinition = "TEXT")
    private String formulaCustom;

    @ElementCollection
    @CollectionTable(name = "regra_pesos", joinColumns = @JoinColumn(name = "entidade_id"))
    @MapKeyColumn(name = "pergunta_id")
    @Column(name = "peso")
    private Map<Long, Double> pesos = new HashMap<>();

    public RegraCalculo() {
    }

    public String getTipoCalculo() {
        return tipoCalculo;
    }

    public void setTipoCalculo(String tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    public String getFormulaCustom() {
        return formulaCustom;
    }

    public void setFormulaCustom(String formulaCustom) {
        this.formulaCustom = formulaCustom;
    }

    public Map<Long, Double> getPesos() {
        return pesos;
    }

    public void setPesos(Map<Long, Double> pesos) {
        this.pesos = pesos;
    }
}
