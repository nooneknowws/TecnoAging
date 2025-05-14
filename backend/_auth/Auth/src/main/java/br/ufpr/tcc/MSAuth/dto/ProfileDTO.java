package br.ufpr.tcc.MSAuth.dto;


public class ProfileDTO {
	private Long id;
	private String nome;
	private String cpf;
	private String tipo; // "TECNICO" ou "PACIENTE"

	public ProfileDTO() {
	}

	public ProfileDTO(Long id, String nome, String cpf, String tipo) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.tipo = tipo;
	}

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}