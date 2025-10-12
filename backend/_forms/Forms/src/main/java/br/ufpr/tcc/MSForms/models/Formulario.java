package br.ufpr.tcc.MSForms.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String titulo;
    private String descricao;

    private Boolean calculaPontuacao = false;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "tipoCalculo", column = @Column(name = "tipo_calculo")),
        @AttributeOverride(name = "formulaCustom", column = @Column(name = "formula_custom"))
    })
    private RegraCalculo regraCalculoFinal;

    @OneToMany(mappedBy = "formulario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Etapa> etapas;

    // Construtor padrão exigido pelo JPA
    public Formulario() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Etapa> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<Etapa> etapas) {
        this.etapas = etapas;
    }

    public Boolean getCalculaPontuacao() {
        return calculaPontuacao;
    }

    public void setCalculaPontuacao(Boolean calculaPontuacao) {
        this.calculaPontuacao = calculaPontuacao;
    }

    public RegraCalculo getRegraCalculoFinal() {
        return regraCalculoFinal;
    }

    public void setRegraCalculoFinal(RegraCalculo regraCalculoFinal) {
        this.regraCalculoFinal = regraCalculoFinal;
    }
}