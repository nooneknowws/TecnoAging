package br.ufpr.tcc.shared.dto;

public class PerguntaValorDTO {
    private String pergunta;
    private String valor;

    // Constructor
    public PerguntaValorDTO(String pergunta, String valor) {
        this.pergunta = pergunta;
        this.valor = valor;
    }

    // Getters and Setters
    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
