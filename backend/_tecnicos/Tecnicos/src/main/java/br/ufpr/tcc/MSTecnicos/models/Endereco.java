package br.ufpr.tcc.MSTecnicos.models;

import jakarta.persistence.*;

@Embeddable
public class Endereco {
	public Endereco() {
		super();
	}

	public Endereco(String logradouro, int numero, String bairro, String municipio, String uf, String cep, String complemento) {
		super();
		this.logradouro = logradouro;
		this.numero = numero;
		this.bairro = bairro;
		this.municipio = municipio;
		this.uf = uf;
		this.cep = cep;
		this.complemento = complemento;
	}

	private String logradouro;
	private int numero;
	private String bairro;
	private String municipio;
	private String uf;
	private String cep;
	private String complemento;

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUF() {
		return uf;
	}

	public void setUF(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

}
