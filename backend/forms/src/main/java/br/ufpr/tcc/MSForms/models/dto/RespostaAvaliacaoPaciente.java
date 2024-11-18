package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;

public class RespostaAvaliacaoPaciente {
	
    public RespostaAvaliacaoPaciente(Long avaliacaoId, Long pacienteId, String paciente, Long tecnicoId, String tecnico, String formulario, String formularioDesc,
			int pontuacaoTotal, int pontuacaoMaxima, List<PerguntaValorDTO> perguntasValores) {
		this.avaliacaoId = avaliacaoId;
		this.pacienteId = pacienteId;
		this.paciente = paciente;
		this.tecnicoId = tecnicoId;
		this.tecnico = tecnico;
		this.formulario = formulario;
		this.formularioDesc = formularioDesc;
		this.pontuacaoTotal = pontuacaoTotal;
		this.pontuacaoMaxima = pontuacaoMaxima;
		this.perguntasValores = perguntasValores;
	}

	private Long avaliacaoId;
	private Long pacienteId;
    private String paciente;
    private Long tecnicoId;
    private String tecnico;
    private String formulario;
    private String formularioDesc;
    private int pontuacaoTotal;
    private int pontuacaoMaxima;
    private List<PerguntaValorDTO> perguntasValores;

    public List<PerguntaValorDTO> getPerguntasValores() {
        return perguntasValores;
    }

    public void setPerguntasValores(List<PerguntaValorDTO> perguntasValores) {
        this.perguntasValores = perguntasValores;
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
}