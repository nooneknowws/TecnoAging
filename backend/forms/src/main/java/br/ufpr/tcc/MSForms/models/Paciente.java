package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
public class Paciente {

    public Paciente(Long id, String nome, String sexo, int idade, float peso, float altura, float imc, String socioeconomico,
			String escolaridade, Endereco endereco, String dataNasc, String cpf, String telefone,
			List<Contato> contatos) {
		super();
		this.id = id;
		this.nome = nome;
		this.sexo = sexo;
		this.idade = idade;
		this.peso = peso;
		this.altura = altura;
		this.imc = imc;
		this.socioeconomico = socioeconomico;
		this.escolaridade = escolaridade;
		this.endereco = endereco;
		this.dataNasc = dataNasc;
		this.cpf = cpf;
		this.telefone = telefone;
		this.contatos = contatos;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sexo;
    private int idade;
    
    @Column(nullable = false)
    private float peso;
    
    @Column(nullable = false)
    private float altura;
    
    private float imc;
    private String socioeconomico;
    private String escolaridade;
    
    @Embedded
    private Endereco endereco;

    private String dataNasc;
    
    @Column(unique = true, nullable = false)
    private String cpf;
    
    private String telefone;

    @ElementCollection
    @CollectionTable(name = "paciente_contatos", joinColumns = @JoinColumn(name = "paciente_id"))
    private List<Contato> contatos;

    public Paciente() {
        super();
    }

    public Paciente(Long id, String nome, String sexo, int idade, Endereco endereco, String dataNasc, String cpf,
                    String telefone, List<Contato> contatos) {
        super();
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.telefone = telefone;
        this.contatos = contatos;
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
        calcularIdade();
    }
    private void calcularIdade() {
        if (this.dataNasc != null) {
            try {
                LocalDate nascimento = LocalDate.parse(this.dataNasc, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                this.idade = Period.between(nascimento, LocalDate.now()).getYears();
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Data de nascimento inválida. Use o formato yyyy-MM-dd.");
            }
        }
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

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getAltura() {
		return altura;
	}

	private float setAltura(float altura) {
	    return this.altura = Math.round(altura * 100) / 100f;
	}

	public float getImc() {
		return imc;
	}

	public void setImc(float imc) {
		this.imc = imc;
	}

	public String getSocioeconomico() {
		return socioeconomico;
	}

	public void setSocioeconomico(String socioeconomico) {
		this.socioeconomico = socioeconomico;
	}

	public String getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}
}
