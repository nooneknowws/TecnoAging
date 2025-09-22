package br.ufpr.tcc.MSForms.models.dto;

public class RespostaDTO {

    private Long perguntaId;
    private String valor;

    public RespostaDTO() {}

    public RespostaDTO(Long perguntaId, String valor) {
        this.perguntaId = perguntaId;
        this.valor = valor;
    }

    public Long getPerguntaId() {
        return perguntaId;
    }

    public void setPerguntaId(Long perguntaId) {
        this.perguntaId = perguntaId;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
