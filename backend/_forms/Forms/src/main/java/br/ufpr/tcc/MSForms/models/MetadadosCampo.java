package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MetadadosCampo {

    private String subTipo;

    @Column(columnDefinition = "TEXT")
    private String placeholder;

    private String unidade;

    private String mascara;

    private Boolean multiplaEscolha;

    private Integer minOpcoes;

    private Integer maxOpcoes;

    public MetadadosCampo() {
    }

    public String getSubTipo() {
        return subTipo;
    }

    public void setSubTipo(String subTipo) {
        this.subTipo = subTipo;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public Boolean getMultiplaEscolha() {
        return multiplaEscolha;
    }

    public void setMultiplaEscolha(Boolean multiplaEscolha) {
        this.multiplaEscolha = multiplaEscolha;
    }

    public Integer getMinOpcoes() {
        return minOpcoes;
    }

    public void setMinOpcoes(Integer minOpcoes) {
        this.minOpcoes = minOpcoes;
    }

    public Integer getMaxOpcoes() {
        return maxOpcoes;
    }

    public void setMaxOpcoes(Integer maxOpcoes) {
        this.maxOpcoes = maxOpcoes;
    }
}
