package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;

public class FormularioCadastroDTO {

    private String tipo;
    private String titulo;
    private String descricao;
    private Boolean calculaPontuacao;
    private RegraCalculoDTO regraCalculoFinal;
    private List<EtapaCadastroDTO> etapas;

    public FormularioCadastroDTO() {
    }

    public FormularioCadastroDTO(String tipo, String titulo, String descricao, Boolean calculaPontuacao,
                                 RegraCalculoDTO regraCalculoFinal, List<EtapaCadastroDTO> etapas) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.descricao = descricao;
        this.calculaPontuacao = calculaPontuacao;
        this.regraCalculoFinal = regraCalculoFinal;
        this.etapas = etapas;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getCalculaPontuacao() {
        return calculaPontuacao;
    }

    public void setCalculaPontuacao(Boolean calculaPontuacao) {
        this.calculaPontuacao = calculaPontuacao;
    }

    public RegraCalculoDTO getRegraCalculoFinal() {
        return regraCalculoFinal;
    }

    public void setRegraCalculoFinal(RegraCalculoDTO regraCalculoFinal) {
        this.regraCalculoFinal = regraCalculoFinal;
    }

    public List<EtapaCadastroDTO> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<EtapaCadastroDTO> etapas) {
        this.etapas = etapas;
    }
}
