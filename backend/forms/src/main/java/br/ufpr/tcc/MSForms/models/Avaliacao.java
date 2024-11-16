package br.ufpr.tcc.MSForms.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class Avaliacao {

	 public Avaliacao() {
		 super();
	 }
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente;

    @ManyToOne
    private Tecnico tecnico;

    @ManyToOne
    private Formulario formulario;

    private int pontuacaoTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    public Avaliacao(Long id, Paciente paciente, Tecnico tecnico, Formulario formulario, int pontuacaoTotal,
			LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
		super();
		this.id = id;
		this.paciente = paciente;
		this.tecnico = tecnico;
		this.formulario = formulario;
		this.pontuacaoTotal = pontuacaoTotal;
		this.dataCriacao = dataCriacao;
		this.dataAtualizacao = dataAtualizacao;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public Tecnico getTecnico() {
		return tecnico;
	}
	public void setTecnico(Tecnico tecnico) {
		this.tecnico = tecnico;
	}
	public Formulario getFormulario() {
		return formulario;
	}
	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}
	public int getPontuacaoTotal() {
		return pontuacaoTotal;
	}
	public void setPontuacaoTotal(int pontuacaoTotal) {
		this.pontuacaoTotal = pontuacaoTotal;
	}
	public LocalDateTime getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(LocalDateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public LocalDateTime getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

    // Getters and Setters
}