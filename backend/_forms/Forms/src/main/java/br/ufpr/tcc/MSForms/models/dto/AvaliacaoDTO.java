package br.ufpr.tcc.MSForms.models.dto;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AvaliacaoDTO {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private int pacienteIdade;
    private float pacienteIMC;
    private Long tecnicoId;
    private String tecnicoNome;
    private Long formularioId;
    private int pontuacaoTotal;
    private int pontuacaoMaxima;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime dataCriacao;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
    private OffsetDateTime dataAtualizacao;
    
    private List<RespostaDTO> respostas;

    public AvaliacaoDTO() {}

    public AvaliacaoDTO(Long id, Long pacienteId, String pacienteNome, int pacienteIdade, float pacienteIMC,
                       Long tecnicoId, String tecnicoNome, Long formularioId, int pontuacaoTotal,
                       int pontuacaoMaxima, OffsetDateTime dataCriacao, OffsetDateTime dataAtualizacao,
                       List<RespostaDTO> respostas) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.pacienteNome = pacienteNome;
        this.pacienteIdade = pacienteIdade;
        this.pacienteIMC = pacienteIMC;
        this.tecnicoId = tecnicoId;
        this.tecnicoNome = tecnicoNome;
        this.formularioId = formularioId;
        this.pontuacaoTotal = pontuacaoTotal;
        this.pontuacaoMaxima = pontuacaoMaxima;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.respostas = respostas;
    }

    // Getters and Setters for all fields
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

    public String getPacienteNome() {
        return pacienteNome;
    }

    public void setPacienteNome(String pacienteNome) {
        this.pacienteNome = pacienteNome;
    }

    public int getPacienteIdade() {
        return pacienteIdade;
    }

    public void setPacienteIdade(int pacienteIdade) {
        this.pacienteIdade = pacienteIdade;
    }

    public float getPacienteIMC() {
        return pacienteIMC;
    }

    public void setPacienteIMC(float pacienteIMC) {
        this.pacienteIMC = pacienteIMC;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getTecnicoNome() {
        return tecnicoNome;
    }

    public void setTecnicoNome(String tecnicoNome) {
        this.tecnicoNome = tecnicoNome;
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

    public int getPontuacaoMaxima() {
        return pontuacaoMaxima;
    }

    public void setPontuacaoMaxima(int pontuacaoMaxima) {
        this.pontuacaoMaxima = pontuacaoMaxima;
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

    public List<RespostaDTO> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<RespostaDTO> respostas) {
        this.respostas = respostas;
    }
}