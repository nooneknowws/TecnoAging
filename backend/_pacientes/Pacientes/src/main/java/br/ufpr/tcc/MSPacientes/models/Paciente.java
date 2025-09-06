package br.ufpr.tcc.MSPacientes.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "paciente")
public class Paciente extends Pessoa {
    
    @Column(name = "peso", nullable = false)
    private float peso;
    

    @Version
    private Long version;
    
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

    @ElementCollection
    @CollectionTable(name = "paciente_contatos", joinColumns = @JoinColumn(name = "paciente_id"))
    private List<Contato> contatos;

    @Lob
    @Column(name = "foto_perfil")
    @Basic(fetch = FetchType.LAZY)
    private byte[] fotoPerfil;

    public Paciente() {
        super();
    }

    public Paciente(Long id, String nome, String sexo, int idade, float peso, float altura, float imc, String socioeconomico,
            String escolaridade, String estadoCivil, String nacionalidade, String municipioNasc, String ufNasc,
            String corRaca, String rg, String dataExpedicao, String orgaoEmissor, String ufEmissor, 
            Endereco endereco, String dataNasc, String cpf, String telefone, List<Contato> contatos) {
        super(id, cpf, nome, sexo, endereco, dataNasc, telefone);
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
        this.contatos = contatos;
        calcularIMC();
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
        calcularIMC();
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = Math.round(altura * 100) / 100f;
        calcularIMC();
    }

    public float getImc() {
        return imc;
    }

    public void setImc(float imc) {
        this.imc = imc;
    }

    private void calcularIMC() {
        if (altura > 0) {
            this.imc = peso / (altura * altura);
        } else {
            this.imc = 0; 
        }
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

    public List<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}