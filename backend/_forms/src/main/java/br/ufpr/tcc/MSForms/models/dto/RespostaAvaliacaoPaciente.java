package br.ufpr.tcc.MSForms.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RespostaAvaliacaoPaciente {
	
    public RespostaAvaliacaoPaciente(Long avaliacaoId, Long pacienteId, String paciente, Long idade, Long IMC, Long tecnicoId, String tecnico,
			String formulario, String formularioDesc, int pontuacaoTotal, int pontuacaoMaxima,
			LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<PerguntaValorDTO> respostas) {
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
		this.dataCriacao = dataCriacao;
		this.dataAtualizacao = dataAtualizacao;
		this.respostas = respostas;
	}


	private Long avaliacaoId;
	private Long pacienteId;
    private String paciente;
    private Long pacienteIMC;
    private Long pacienteIDADE;
    private Long tecnicoId;
    private String tecnico;
    private String formulario;
    private String formularioDesc;
    private int pontuacaoTotal;
    private int pontuacaoMaxima;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    private List<PerguntaValorDTO> respostas;

    public List<PerguntaValorDTO> getPerguntasValores() {
        return respostas;
    }

    public void setPerguntasValores(List<PerguntaValorDTO> perguntasValores) {
        this.respostas = perguntasValores;
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

	public Long getPacienteIMC() {
		return pacienteIMC;
	}

	public void setPacienteIMC(Long pacienteIMC) {
		this.pacienteIMC = pacienteIMC;
	}

	public Long getPacienteIDADE() {
		return pacienteIDADE;
	}

	public void setPacienteIDADE(Long pacienteIDADE) {
		this.pacienteIDADE = pacienteIDADE;
	}
}