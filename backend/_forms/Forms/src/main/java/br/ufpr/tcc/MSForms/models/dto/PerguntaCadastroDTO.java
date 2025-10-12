package br.ufpr.tcc.MSForms.models.dto;

import br.ufpr.tcc.MSForms.models.Validacao;
import java.util.List;

public class PerguntaCadastroDTO {

    private String texto;
    private String tipo;
    private List<String> opcoes;
    private Validacao validacao;
    private ConfiguracaoPontuacaoDTO configuracaoPontuacao;
    private MetadadosCampoDTO metadadosCampo;

    public PerguntaCadastroDTO() {
    }

    public PerguntaCadastroDTO(String texto, String tipo, List<String> opcoes, Validacao validacao,
                               ConfiguracaoPontuacaoDTO configuracaoPontuacao, MetadadosCampoDTO metadadosCampo) {
        this.texto = texto;
        this.tipo = tipo;
        this.opcoes = opcoes;
        this.validacao = validacao;
        this.configuracaoPontuacao = configuracaoPontuacao;
        this.metadadosCampo = metadadosCampo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<String> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<String> opcoes) {
        this.opcoes = opcoes;
    }

    public Validacao getValidacao() {
        return validacao;
    }

    public void setValidacao(Validacao validacao) {
        this.validacao = validacao;
    }

    public ConfiguracaoPontuacaoDTO getConfiguracaoPontuacao() {
        return configuracaoPontuacao;
    }

    public void setConfiguracaoPontuacao(ConfiguracaoPontuacaoDTO configuracaoPontuacao) {
        this.configuracaoPontuacao = configuracaoPontuacao;
    }

    public MetadadosCampoDTO getMetadadosCampo() {
        return metadadosCampo;
    }

    public void setMetadadosCampo(MetadadosCampoDTO metadadosCampo) {
        this.metadadosCampo = metadadosCampo;
    }
}
