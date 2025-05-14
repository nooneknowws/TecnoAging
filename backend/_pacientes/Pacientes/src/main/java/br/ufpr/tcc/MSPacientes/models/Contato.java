package br.ufpr.tcc.MSPacientes.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.ufpr.tcc.MSPacientes.enums.EnumParentesco;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Contato {

    private String nome;
    private String telefone;
    
    @Enumerated(EnumType.STRING)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    private EnumParentesco parentesco;

    public Contato() {
    }

    public Contato(String nome, String telefone, EnumParentesco parentesco) {
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

    public EnumParentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(EnumParentesco parentesco) {
        this.parentesco = parentesco;
    }
    
    public String getParentescoValor() {
        return parentesco != null ? parentesco.getValor() : null;
    }
}