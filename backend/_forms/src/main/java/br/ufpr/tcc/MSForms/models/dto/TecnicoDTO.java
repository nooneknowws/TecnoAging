package br.ufpr.tcc.MSForms.models.dto;

public class TecnicoDTO {

	public TecnicoDTO(Long id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}
	
	private Long id;
	private String nome;
	
	
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
}
