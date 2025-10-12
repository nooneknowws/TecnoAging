package br.ufpr.tcc.MSForms.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String texto;
    private String tipo;
    private String resposta;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pergunta_opcoes", joinColumns = @JoinColumn(name = "pergunta_id"))
    @Column(name = "opcao")
    private List<String> opcoes = new ArrayList<>();

    @Embedded
    private Validacao validacao;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "tipoPontuacao", column = @Column(name = "tipo_pontuacao")),
        @AttributeOverride(name = "pontosMinimos", column = @Column(name = "pontos_minimos")),
        @AttributeOverride(name = "pontosMaximos", column = @Column(name = "pontos_maximos"))
    })
    private ConfiguracaoPontuacao configuracaoPontuacao;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "subTipo", column = @Column(name = "sub_tipo")),
        @AttributeOverride(name = "multiplaEscolha", column = @Column(name = "multipla_escolha")),
        @AttributeOverride(name = "minOpcoes", column = @Column(name = "min_opcoes")),
        @AttributeOverride(name = "maxOpcoes", column = @Column(name = "max_opcoes"))
    })
    private MetadadosCampo metadadosCampo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etapa_id")
    @JsonBackReference
    private Etapa etapa;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
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

    public Etapa getEtapa() {
        return etapa;
    }

    public void setEtapa(Etapa etapa) {
        this.etapa = etapa;
    }

    public ConfiguracaoPontuacao getConfiguracaoPontuacao() {
        return configuracaoPontuacao;
    }

    public void setConfiguracaoPontuacao(ConfiguracaoPontuacao configuracaoPontuacao) {
        this.configuracaoPontuacao = configuracaoPontuacao;
    }

    public MetadadosCampo getMetadadosCampo() {
        return metadadosCampo;
    }

    public void setMetadadosCampo(MetadadosCampo metadadosCampo) {
        this.metadadosCampo = metadadosCampo;
    }
}