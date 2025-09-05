package br.ufpr.tcc.MSForms.models.dto;

import java.util.List;
import br.ufpr.tcc.MSForms.models.Validacao;

public class PerguntaValorDTO {
    private String pergunta;
    private String valor;
    private String tipo;
    private Validacao validacao;
    private List<String> opcoes;

    // Constructor
    public PerguntaValorDTO(String pergunta, String valor) {
        this.pergunta = pergunta;
        this.valor = valor;
    }

    // Constructor with all fields
    public PerguntaValorDTO(String pergunta, String valor, String tipo, Validacao validacao, List<String> opcoes) {
        this.pergunta = pergunta;
        this.valor = valor;
        this.tipo = tipo;
        this.validacao = validacao;
        this.opcoes = opcoes;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Validacao getValidacao() {
        return validacao;
    }

    public void setValidacao(Validacao validacao) {
        this.validacao = validacao;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes;
    }
}
