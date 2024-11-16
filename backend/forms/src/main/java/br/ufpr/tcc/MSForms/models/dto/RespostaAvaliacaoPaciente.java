package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;

public class RespostaAvaliacaoPaciente {
	
    public RespostaAvaliacaoPaciente() {
		super();
	}

	public RespostaAvaliacaoPaciente(Long tecnicoId, String tecnico, List<PerguntaValorDTO> perguntasValores) {
		super();
		this.tecnicoId = tecnicoId;
		this.tecnico = tecnico;
		this.perguntasValores = perguntasValores;
	}

	private Long tecnicoId;
    private String tecnico;
    private List<PerguntaValorDTO> perguntasValores;


    public List<PerguntaValorDTO> getPerguntasValores() {
        return perguntasValores;
    }

    public void setPerguntasValores(List<PerguntaValorDTO> perguntasValores) {
        this.perguntasValores = perguntasValores;
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
}

