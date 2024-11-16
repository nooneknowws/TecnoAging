package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class Validacao {

	private boolean required;

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}
	  public Validacao(boolean required) {
			super();
			this.required = required;
		}

}

