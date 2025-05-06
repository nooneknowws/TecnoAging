package br.ufpr.tcc.MSForms.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Long paciente;

    @ManyToOne
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Long tecnico;

    @ManyToOne
    @JoinColumn(name = "formulario_id", nullable = false)
    private Formulario formulario;

    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Resposta> respostas;

    @Column(name = "pontuacao_total")
    private int pontuacaoTotal;

    @Column(name = "pontuacao_maxima")
    private int pontuacaoMaxima;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    public Avaliacao() {}

    public Avaliacao(Long id, Long paciente, Long tecnico, Formulario formulario, int pontuacaoMaxima, int pontuacaoTotal,
                     LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<Resposta> respostas) {
        this.id = id;
        this.paciente = paciente;
        this.tecnico = tecnico;
        this.formulario = formulario;
        this.pontuacaoTotal = pontuacaoTotal;
        this.pontuacaoMaxima = pontuacaoMaxima;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.respostas = respostas;
    }

    // Getters e Setters
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

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
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

    public int getPontuacaoMaxima() {
        return pontuacaoMaxima;
    }

    public void setPontuacaoMaxima(int pontuacaoMaxima) {
        this.pontuacaoMaxima = pontuacaoMaxima;
    }

	public void setPaciente(int paciente) {
		this.paciente = paciente;
	}

	public void setTecnico(int tecnico) {
		this.tecnico = tecnico;
	}
}