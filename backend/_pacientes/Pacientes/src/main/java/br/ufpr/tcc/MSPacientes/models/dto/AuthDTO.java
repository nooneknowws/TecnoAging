package br.ufpr.tcc.MSPacientes.models.dto;

public class AuthDTO {
	public AuthDTO() {
		super();
	}
	public AuthDTO(String cpf, String senha) {
		super();
		this.cpf = cpf;
		this.senha = senha;
	}
	String cpf;
	String senha;
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
