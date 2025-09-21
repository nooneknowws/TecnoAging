package br.ufpr.tcc.MSForms.models.dto;

public class PerguntaValorDTO {
    private PerguntaDTO pergunta;
    private String valor;

    public PerguntaValorDTO() {}

    public PerguntaValorDTO(PerguntaDTO pergunta, String valor) {
        this.pergunta = pergunta;
        this.valor = valor;
    }

    public PerguntaDTO getPergunta() {
        return pergunta;
    }

    public void setPergunta(PerguntaDTO pergunta) {
        this.pergunta = pergunta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
