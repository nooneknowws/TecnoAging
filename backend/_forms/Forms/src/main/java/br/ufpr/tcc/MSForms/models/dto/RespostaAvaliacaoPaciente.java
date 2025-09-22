package br.ufpr.tcc.MSForms.models.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class RespostaAvaliacaoPaciente {
	
    public RespostaAvaliacaoPaciente(Long avaliacaoId, Long pacienteId, String paciente, int idade, float IMC, Long tecnicoId, String tecnico,
			String formulario, String formularioDesc, int pontuacaoTotal, int pontuacaoMaxima,
			OffsetDateTime offsetDateTime, OffsetDateTime offsetDateTime2, List<PerguntaValorDTO> respostas) {
		super();
		this.avaliacaoId = avaliacaoId;
		this.pacienteId = pacienteId;
		this.paciente = paciente;
		this.pacienteIDADE = idade;
		this.pacienteIMC = IMC;
		this.tecnicoId = tecnicoId;
		this.tecnico = tecnico;
		this.formulario = formulario;
		this.formularioDesc = formularioDesc;
		this.pontuacaoTotal = pontuacaoTotal;
		this.pontuacaoMaxima = pontuacaoMaxima;
		this.dataCriacao = offsetDateTime;
		this.dataAtualizacao = offsetDateTime2;
		this.respostas = respostas;
	}


	private Long avaliacaoId;
	private Long pacienteId;
    private String paciente;
    private float pacienteIMC;
    private int pacienteIDADE;
    private Long tecnicoId;
    private String tecnico;
    private String formulario;
    private String formularioDesc;
    private int pontuacaoTotal;
    private int pontuacaoMaxima;
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataAtualizacao;
    
    private List<PerguntaValorDTO> respostas;

    public List<PerguntaValorDTO> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<PerguntaValorDTO> respostas) {
        this.respostas = respostas;
    }

	public Long getPacienteId() {
		return pacienteId;
	}

	public void setPacienteId(Long pacienteId) {
		this.pacienteId = pacienteId;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public Long getTecnicoId() {
		return tecnicoId;
	}

	public void setTecnicoId(Long tecnicoId) {
		this.tecnicoId = tecnicoId;
	}

	public String getTecnico() {
		return tecnico;
	}

	public void setTecnico(String tecnico) {
		this.tecnico = tecnico;
	}

	public String getFormulario() {
		return formulario;
	}

	public void setFormulario(String formulario) {
		this.formulario = formulario;
	}

	public String getFormularioDesc() {
		return formularioDesc;
	}

	public void setFormularioDesc(String formularioDesc) {
		this.formularioDesc = formularioDesc;
	}

	public int getPontuacaoTotal() {
		return pontuacaoTotal;
	}

	public void setPontuacaoTotal(int pontuacaoTotal) {
		this.pontuacaoTotal = pontuacaoTotal;
	}

	public int getPontuacaoMaxima() {
		return pontuacaoMaxima;
	}

	public void setPontuacaoMaxima(int pontuacaoMaxima) {
		this.pontuacaoMaxima = pontuacaoMaxima;
	}

	public Long getAvaliacaoId() {
		return avaliacaoId;
	}

	public void setAvaliacaoId(Long avaliacaoId) {
		this.avaliacaoId = avaliacaoId;
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

	public float getPacienteIMC() {
		return pacienteIMC;
	}

	public void setPacienteIMC(float pacienteIMC) {
		this.pacienteIMC = pacienteIMC;
	}

	public int getPacienteIDADE() {
		return pacienteIDADE;
	}

	public void setPacienteIDADE(int pacienteIDADE) {
		this.pacienteIDADE = pacienteIDADE;
	}
}