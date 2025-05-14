package br.ufpr.tcc.MSForms.models.dto;

public class PacienteDTO {
    private Long id;
    private String nome;
    private int idade;
    private float imc;
    
    public PacienteDTO() {
    }

    public PacienteDTO(Long id, String nome, int i, float f) {
        this.id = id;
        this.nome = nome;
        this.idade = i;
        this.imc = f;
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
    public int getIdade() {
        return idade;
    }
    public void setIdade(int idade) {
        this.idade = idade;
    }
    public float getImc() {
        return imc;
    }
    public void setImc(float imc) {
        this.imc = imc;
    }
}