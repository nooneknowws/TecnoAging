package br.ufpr.tcc.MSTecnicos.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
public class Tecnico extends Pessoa {

    @Column(name = "matricula")
    @JsonProperty("matricula")
    private String matricula;

    @Column(name = "ativo")
    @JsonProperty("ativo")
    private boolean ativo;

    @Lob
    @Column(name = "foto_perfil")
    @Basic(fetch = FetchType.LAZY)
    private byte[] fotoPerfil;

	public Tecnico() {
		super();
	}
	
	public Tecnico(Long id, String nome, String sexo, int idade, Endereco endereco, String dataNasc, String cpf,
			String telefone, String matricula, boolean ativo) {
		super();
		this.id = id;
		this.nome = nome;
		this.sexo = sexo;
		this.idade = idade;
		this.endereco = endereco;
		this.dataNasc = dataNasc;
		this.cpf = cpf;
		this.telefone = telefone;
		this.matricula = matricula;
		this.ativo = ativo;
	}

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}