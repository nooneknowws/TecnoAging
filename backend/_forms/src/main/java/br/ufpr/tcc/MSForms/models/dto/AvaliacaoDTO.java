package br.ufpr.tcc.MSForms.models.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AvaliacaoDTO {

    private Long id;
    private Long pacienteId;
    private Long tecnicoId;
    private Long formularioId;
    private int pontuacaoTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private List<RespostaDTO> respostas;

    public AvaliacaoDTO() {}

    public AvaliacaoDTO(Long id, Long pacienteId, Long tecnicoId, Long formularioId, int pontuacaoTotal, 
                        LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, List<RespostaDTO> respostas) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.tecnicoId = tecnicoId;
        this.formularioId = formularioId;
        this.pontuacaoTotal = pontuacaoTotal;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.respostas = respostas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public Long getFormularioId() {
        return formularioId;
    }

    public void setFormularioId(Long formularioId) {
        this.formularioId = formularioId;
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

    public List<RespostaDTO> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<RespostaDTO> respostas) {
        this.respostas = respostas;
    }
}
