package br.ufpr.tcc.MSForms.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Formulario {



	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String titulo;
    private String descricao;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL)
    private List<Etapa> etapas;
    
    public Formulario(Long id, String tipo, String titulo, String descricao, List<Etapa> etapas) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.titulo = titulo;
		this.descricao = descricao;
		this.etapas = etapas;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Etapa> getEtapas() {
		return etapas;
	}

	public void setEtapas(List<Etapa> etapas) {
		this.etapas = etapas;
	}

    // Getters and Setters
}

