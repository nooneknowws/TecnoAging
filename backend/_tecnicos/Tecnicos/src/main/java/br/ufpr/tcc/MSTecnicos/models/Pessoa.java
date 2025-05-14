package br.ufpr.tcc.MSTecnicos.models;

import jakarta.persistence.*;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
public class Pessoa {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(unique = true, nullable = false)
    @JsonProperty("cpf")
    protected String cpf;

    @Column(name = "nome", nullable = false)
    @JsonProperty("nome")
    protected String nome;

    @Column(name = "sexo", nullable = false)
    @JsonProperty("sexo")
    protected String sexo;

    @Column(name = "idade")
    @JsonProperty("idade")
    protected int idade;

    @Embedded
    @JsonProperty("endereco")
    protected Endereco endereco;

    @Column(name = "data_nasc")
    @JsonProperty("dataNasc")
    protected String dataNasc;

    @Column(name = "telefone")
    @JsonProperty("telefone")
    protected String telefone;

    @JsonProperty("senha")
    protected String senha;
    protected String salt;
    protected String passwordHash; 

    public Pessoa() {
        super();
    }
    
    public Pessoa(Long id, String cpf, String nome, String sexo, int idade, Endereco endereco, String dataNasc, String telefone) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.endereco = endereco;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
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

    protected void calcularIdade() {
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    public String getSenha() {
		return senha;
	}
    
    public void setSenha(String senha) {
        this.senha = senha; 
        String[] saltAndHash = hashPassword(senha);
        this.salt = saltAndHash[0];
        this.passwordHash = saltAndHash[1];
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean verificarSenha(String senha) {
        return checkPassword(senha, this.salt, this.passwordHash);
    }

    public void redefinirSenha(String novaSenha) {
        String[] saltAndHash = hashPassword(novaSenha);
        this.salt = saltAndHash[0];
        this.passwordHash = saltAndHash[1];
    }

    private boolean checkPassword(String senha, String salt, String hash) {
        try {
            byte[] saltBytes = java.util.Base64.getDecoder().decode(salt);
            
            KeySpec spec = new PBEKeySpec(
                senha.toCharArray(), 
                saltBytes, 
                10000, 
                256
            );
            
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashCalculado = f.generateSecret(spec).getEncoded();
            
            String hashCalculadoBase64 = java.util.Base64.getEncoder().encodeToString(hashCalculado);
            return hashCalculadoBase64.equals(hash);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar senha", e);
        }
    }

    private String[] hashPassword(String senha) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), salt, 10000, 256);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = f.generateSecret(spec).getEncoded();
            
            return new String[]{
                java.util.Base64.getEncoder().encodeToString(salt),
                java.util.Base64.getEncoder().encodeToString(hash)
            };
        } catch (Exception e) {
            throw new RuntimeException("Erro ao hashear senha", e);
        }
    }
}