package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;

public class EtapaCadastroDTO {

    private String titulo;
    private String descricao;
    private RegraCalculoDTO regraCalculoEtapa;
    private List<PerguntaCadastroDTO> perguntas;

    public EtapaCadastroDTO() {
    }

    public EtapaCadastroDTO(String titulo, String descricao, RegraCalculoDTO regraCalculoEtapa,
                            List<PerguntaCadastroDTO> perguntas) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.regraCalculoEtapa = regraCalculoEtapa;
        this.perguntas = perguntas;
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

    public RegraCalculoDTO getRegraCalculoEtapa() {
        return regraCalculoEtapa;
    }

    public void setRegraCalculoEtapa(RegraCalculoDTO regraCalculoEtapa) {
        this.regraCalculoEtapa = regraCalculoEtapa;
    }

    public List<PerguntaCadastroDTO> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<PerguntaCadastroDTO> perguntas) {
        this.perguntas = perguntas;
    }
}
