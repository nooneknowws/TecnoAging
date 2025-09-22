package br.ufpr.tcc.MSAuth.dto;

public class ResetPasswordDTO {
    private String cpf;
    private String codigo;
    private String novaSenha;
    private String confirmarSenha;

    public ResetPasswordDTO() {}

    public ResetPasswordDTO(String cpf, String codigo, String novaSenha, String confirmarSenha) {
        this.cpf = cpf;
        this.codigo = codigo;
        this.novaSenha = novaSenha;
        this.confirmarSenha = confirmarSenha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}