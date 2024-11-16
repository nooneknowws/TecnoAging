package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Validacao {

    private Long min;
    private Long max;
    private boolean required;

    public Validacao() {
        super();
    }

    public Validacao(Long min, Long max, boolean required) {
        super();
        this.min = min;
        this.max = max;
        this.required = required;
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
