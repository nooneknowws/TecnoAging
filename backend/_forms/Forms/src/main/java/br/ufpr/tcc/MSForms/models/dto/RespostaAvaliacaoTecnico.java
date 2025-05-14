package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;

public class RespostaAvaliacaoTecnico {
    private Long pacienteId;
    private String paciente;
    private List<PerguntaValorDTO> perguntasValores;

    // Constructor
    public RespostaAvaliacaoTecnico(Long pacienteId, String paciente, List<PerguntaValorDTO> perguntasValores) {
        this.pacienteId = pacienteId;
        this.paciente = paciente;
        this.perguntasValores = perguntasValores;
    }
    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long clienteId) {
        this.pacienteId = clienteId;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public List<PerguntaValorDTO> getPerguntasValores() {
        return perguntasValores;
    }

    public void setPerguntasValores(List<PerguntaValorDTO> perguntasValores) {
        this.perguntasValores = perguntasValores;
    }
}

