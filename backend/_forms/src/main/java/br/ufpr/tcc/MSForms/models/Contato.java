package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Contato {

    private String nome;
    private String telefone;
    private String parentesco;

    public Contato() {
    }

    public Contato(String nome, String telefone, String parentesco) {
        this.nome = nome;
        this.telefone = telefone;
        this.parentesco = parentesco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
