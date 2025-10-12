package br.ufpr.tcc.MSForms.models.dto;

import java.util.Map;

public class RegraCalculoDTO {

    private String tipoCalculo;
    private String formulaCustom;
    private Map<Long, Double> pesos;

    public RegraCalculoDTO() {
    }

    public RegraCalculoDTO(String tipoCalculo, String formulaCustom, Map<Long, Double> pesos) {
        this.tipoCalculo = tipoCalculo;
        this.formulaCustom = formulaCustom;
        this.pesos = pesos;
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
