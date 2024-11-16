package br.ufpr.tcc.MSForms.models;

import java.util.List;

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

    @Embedded
    private Validacao validacao;

    @ManyToOne
    private Etapa etapa;
    
    public Pergunta(Long id, String texto, String tipo, List<String> opcoes, String resposta, Validacao validacao,
			Etapa etapa) {
		super();
		this.id = id;
		this.texto = texto;
		this.tipo = tipo;
		this.opcoes = opcoes;
		this.resposta = resposta;
		this.validacao = validacao;
		this.etapa = etapa;
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

	public String getResposta() {
		return resposta;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public Validacao getValidacao() {
		return validacao;
	}

	public void setValidacao(Validacao validacao) {
		this.validacao = validacao;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}

    // Getters and Setters
}

