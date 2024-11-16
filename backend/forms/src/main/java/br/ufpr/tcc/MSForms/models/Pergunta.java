package br.ufpr.tcc.MSForms.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String texto;
    private String tipo;

    @ElementCollection
    private List<String> opcoes;

    private String resposta;
    private boolean validacaoRequired;

    @ManyToOne
    @JoinColumn(name = "etapa_id")
    @JsonIgnore
    private Etapa etapa;

    public Pergunta() {
    }

    public Pergunta(Long id, String texto, String tipo, List<String> opcoes, String resposta, boolean validacaoRequired, Etapa etapa) {
        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
        this.opcoes = opcoes;
        this.resposta = resposta;
        this.validacaoRequired = validacaoRequired;
        this.etapa = etapa;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isValidacaoRequired() {
        return validacaoRequired;
    }

    public void setValidacaoRequired(boolean validacaoRequired) {
        this.validacaoRequired = validacaoRequired;
    }

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }
}
