package br.ufpr.tcc.MSForms.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Etapa {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descricao;

    @ManyToOne
    private Formulario formulario;

    @OneToMany(mappedBy = "etapa", cascade = CascadeType.ALL)
    private List<Pergunta> perguntas;
    
    
    public Etapa(Long id, String titulo, String descricao, Formulario formulario, List<Pergunta> perguntas) {
 		super();
 		this.id = id;
 		this.titulo = titulo;
 		this.descricao = descricao;
 		this.formulario = formulario;
 		this.perguntas = perguntas;
 	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Formulario getFormulario() {
		return formulario;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public List<Pergunta> getPerguntas() {
		return perguntas;
	}

	public void setPerguntas(List<Pergunta> perguntas) {
		this.perguntas = perguntas;
	}

    // Getters and Setters
}
