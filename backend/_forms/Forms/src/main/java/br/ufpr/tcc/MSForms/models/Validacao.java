package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Validacao {

    private Integer min;
    private Integer max;
    private boolean required;

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}