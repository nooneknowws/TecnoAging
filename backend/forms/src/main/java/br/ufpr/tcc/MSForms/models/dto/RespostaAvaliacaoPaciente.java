package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;

public class RespostaAvaliacaoPaciente {
	
    public RespostaAvaliacaoPaciente(Long tecnicoId, String tecnico, String formulario, String formularioDesc,
			List<PerguntaValorDTO> perguntasValores) {
		super();
		this.tecnicoId = tecnicoId;
		this.tecnico = tecnico;
		this.formulario = formulario;
		this.formularioDesc = formularioDesc;
		this.perguntasValores = perguntasValores;
	}

	public RespostaAvaliacaoPaciente() {
		super();
	}

	private Long tecnicoId;
    private String tecnico;
    private String formulario;
    private String formularioDesc;
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
}

