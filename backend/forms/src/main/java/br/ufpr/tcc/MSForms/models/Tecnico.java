package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.*;

@Entity
public class Tecnico {
	public Tecnico() {
		super();
	}
	public Tecnico(Long id, String nome, String sexo, int idade, Endereco endereco, String dataNasc, String cpf,
			String telefone, int matricula, boolean ativo) {
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
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sexo;
    private int idade;

    @Embedded
    private Endereco endereco;

    private String dataNasc;
    
    @Column(unique = true, nullable = false)
    private String cpf;
    
    private String telefone;
    
    @Column(unique = true, nullable = false)
    private int matricula;
    
    private boolean ativo;
    
    
   
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public int getMatricula() {
		return matricula;
	}
	public void setMatricula(int matricula) {
		this.matricula = matricula;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

    // Getters and Setters
}

