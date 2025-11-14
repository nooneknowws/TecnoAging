package br.ufpr.tcc.MSForms.models;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufpr.tcc.MSForms.models.dto.PacienteDTO;
import br.ufpr.tcc.MSForms.models.dto.TecnicoDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    public Avaliacao(Long id, Long paciente, String paciente_nome, int paciente_idade_avaliacao,
			float paciente_imc_avaliacao, Long tecnico, String tecnico_nome, Formulario formulario,
			List<Resposta> respostas, int pontuacaoTotal, int pontuacaoMaxima, OffsetDateTime dataCriacao,
			OffsetDateTime dataAtualizacao) {
		super();
		this.id = id;
		this.paciente = paciente;
		this.paciente_nome = paciente_nome;
		this.paciente_idade_avaliacao = paciente_idade_avaliacao;
		this.paciente_imc_avaliacao = paciente_imc_avaliacao;
		this.tecnico = tecnico;
		this.tecnico_nome = tecnico_nome;
		this.formulario = formulario;
		this.respostas = respostas;
		this.pontuacaoTotal = pontuacaoTotal;
		this.pontuacaoMaxima = pontuacaoMaxima;
		this.dataCriacao = dataCriacao;
		this.dataAtualizacao = dataAtualizacao;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "paciente_id", nullable = false)
    private Long paciente;
    @Column(name = "paciente_nome")
    private String paciente_nome;
    @Column(name = "paciente_idade_avaliacao")
    private int paciente_idade_avaliacao;
    @Column(name = "paciente_imc_avaliacao")
    private float paciente_imc_avaliacao;
    
    @Column(name = "tecnico_id", nullable = true)
    private Long tecnico;
    @Column(name = "tecnico_nome")
    private String tecnico_nome;

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

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "data_criacao")
    private OffsetDateTime dataCriacao;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    @Column(name = "data_atualizacao")
    private OffsetDateTime dataAtualizacao;

    public Avaliacao() {}
    

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaciente() {
        return paciente;
    }

    public void setPaciente(Long paciente) {
        this.paciente = paciente;
    }

    public Long getTecnico() {
        return tecnico;
    }

    public void setTecnico(Long tecnico) {
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

    public OffsetDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(OffsetDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public OffsetDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(OffsetDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getPontuacaoMaxima() {
        return pontuacaoMaxima;
    }

    public void setPontuacaoMaxima(int pontuacaoMaxima) {
        this.pontuacaoMaxima = pontuacaoMaxima;
    }

	public String getPaciente_nome() {
		return paciente_nome;
	}

	public void setPaciente_nome(String paciente_nome) {
		this.paciente_nome = paciente_nome;
	}

	public int getPaciente_idade_avaliacao() {
		return paciente_idade_avaliacao;
	}

	public void setPaciente_idade_avaliacao(int paciente_idade_avaliacao) {
		this.paciente_idade_avaliacao = paciente_idade_avaliacao;
	}

	public float getPaciente_imc_avaliacao() {
		return paciente_imc_avaliacao;
	}

	public void setPaciente_imc_avaliacao(float paciente_imc_avaliacao) {
		this.paciente_imc_avaliacao = paciente_imc_avaliacao;
	}

	public String getTecnico_nome() {
		return tecnico_nome;
	}

	public void setTecnico_nome(String tecnico_nome) {
		this.tecnico_nome = tecnico_nome;
	}
	public TecnicoDTO getTecnicoDTO() {
	    return new TecnicoDTO(
	        this.tecnico,
	        this.tecnico_nome
	    );
	}

	public PacienteDTO getPacienteDTO() {
	    return new PacienteDTO(
	        this.paciente,
	        this.paciente_nome,
	        this.paciente_idade_avaliacao,
	        this.paciente_imc_avaliacao
	    );
	}
}