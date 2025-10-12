package br.ufpr.tcc.MSForms.models.dto;

public class MetadadosCampoDTO {

    private String subTipo;
    private String placeholder;
    private String unidade;
    private String mascara;
    private Boolean multiplaEscolha;
    private Integer minOpcoes;
    private Integer maxOpcoes;

    public MetadadosCampoDTO() {
    }

    public MetadadosCampoDTO(String subTipo, String placeholder, String unidade, String mascara,
                            Boolean multiplaEscolha, Integer minOpcoes, Integer maxOpcoes) {
        this.subTipo = subTipo;
        this.placeholder = placeholder;
        this.unidade = unidade;
        this.mascara = mascara;
        this.multiplaEscolha = multiplaEscolha;
        this.minOpcoes = minOpcoes;
        this.maxOpcoes = maxOpcoes;
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
