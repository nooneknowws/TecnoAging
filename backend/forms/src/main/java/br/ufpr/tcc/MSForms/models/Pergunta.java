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

    @ManyToOne
    @JoinColumn(name = "etapa_id")
    @JsonIgnore
    private Etapa etapa;
    

    @Embedded
    private Validacao validacao;

    public Pergunta() {
    	super();
    }

    public Pergunta(Long id, String texto, String tipo, List<String> opcoes, String resposta, boolean validacaoRequired, Etapa etapa, Validacao validacao) {
        this.id = id;
        this.texto = texto;
        this.tipo = tipo;
        this.opcoes = opcoes;
        this.etapa = etapa;
        this.validacao = validacao;
    }
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


    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }
	public Validacao getValidacao() {
		return validacao;
	}

	public void setValidacao(Validacao validacao) {
		this.validacao = validacao;
	}
}
