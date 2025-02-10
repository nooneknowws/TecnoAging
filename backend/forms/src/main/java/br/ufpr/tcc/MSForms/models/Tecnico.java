package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.*;

@Entity
public class Tecnico extends Pessoa {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String sexo;
    private int idade;
    @Embedded
    private Endereco endereco;
    private String dataNasc;
    private String telefone;
    private String matricula;
    private boolean ativo;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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
}