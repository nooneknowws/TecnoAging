package br.ufpr.tcc.MSForms.models.dto;

public class PacienteDTO {

	public PacienteDTO(Long id, String nome, Long idade, Long imc) {
		super();
		this.id = id;
		this.nome = nome;
		this.idade = idade;
		this.imc = imc;
	}
	
	private Long id;
	private String nome;
	private Long idade;
	private Long imc;
	
	
	
	
	
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
	public Long getIdade() {
		return idade;
	}
	public void setIdade(Long idade) {
		this.idade = idade;
	}
	public Long getImc() {
		return imc;
	}
	public void setImc(Long imc) {
		this.imc = imc;
	}
	
}
