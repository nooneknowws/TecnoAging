package br.ufpr.tcc.MSForms.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Entity
@Table(name = "paciente")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "sexo", nullable = false)
    private String sexo;

    @Column(name = "idade")
    private int idade;

    @Column(name = "peso", nullable = false)
    private float peso;

    @Column(name = "altura", nullable = false)
    private float altura;

    @Column(name = "imc")
    private float imc;

    @Column(name = "socioeconomico")
    private String socioeconomico;

    @Column(name = "escolaridade")
    private String escolaridade;

    @Column(name = "estado_civil")
    private String estadoCivil;

    @Column(name = "nacionalidade")
    private String nacionalidade;

    @Column(name = "municipio_nasc")
    private String municipioNasc;

    @Column(name = "uf_nasc")
    private String ufNasc;

    @Column(name = "cor_raca")
    private String corRaca;

    @Column(name = "rg")
    private String rg;
    
    @Column(name = "data_expedicao")
    private String dataExpedicao;
    
    @Column(name = "orgao_emissor")
    private String orgaoEmissor;
    
    @Column(name = "uf_emissor")
    private String ufEmissor;
    
    @Embedded
    private Endereco endereco;

    @Column(name = "data_nasc")
    private String dataNasc;
    
    @Column(name = "cpf", unique = true, nullable = false)
    private String cpf;
    
    @Column(name = "telefone")
    private String telefone;

    @ElementCollection
    @CollectionTable(name = "paciente_contatos", joinColumns = @JoinColumn(name = "paciente_id"))
    private List<Contato> contatos;

    public Paciente() {
        super();
    }

    public Paciente(Long id, String nome, String sexo, int idade, float peso, float altura, float imc, String socioeconomico,
            String escolaridade, String estadoCivil, String nacionalidade, String municipioNasc, String ufNasc,
            String corRaca, String rg, String dataExpedicao, String orgaoEmissor, String ufEmissor, 
            Endereco endereco, String dataNasc, String cpf, String telefone, List<Contato> contatos) {
	    this.id = id;
	    this.nome = nome;
	    this.sexo = sexo;
	    this.idade = idade;
	    this.peso = peso;
	    this.altura = altura;
	    this.imc = imc;
	    this.socioeconomico = socioeconomico;
	    this.escolaridade = escolaridade;
	    this.estadoCivil = estadoCivil;
	    this.nacionalidade = nacionalidade;
	    this.municipioNasc = municipioNasc;
	    this.ufNasc = ufNasc;
	    this.corRaca = corRaca;
	    this.rg = rg;
	    this.dataExpedicao = dataExpedicao;
	    this.orgaoEmissor = orgaoEmissor;
	    this.ufEmissor = ufEmissor;
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

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getMunicipioNasc() {
		return municipioNasc;
	}

	public void setMunicipioNasc(String municipioNasc) {
		this.municipioNasc = municipioNasc;
	}

	public String getUfNasc() {
		return ufNasc;
	}

	public void setUfNasc(String ufNasc) {
		this.ufNasc = ufNasc;
	}

	public String getCorRaca() {
		return corRaca;
	}

	public void setCorRaca(String corRaca) {
		this.corRaca = corRaca;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}
	public String getDataExpedicao() {
	    return dataExpedicao;
	}

	public void setDataExpedicao(String dataExpedicao) {
	    this.dataExpedicao = dataExpedicao;
	}

	public String getOrgaoEmissor() {
	    return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
	    this.orgaoEmissor = orgaoEmissor;
	}

	public String getUfEmissor() {
	    return ufEmissor;
	}

	public void setUfEmissor(String ufEmissor) {
	    this.ufEmissor = ufEmissor;
	}
}
