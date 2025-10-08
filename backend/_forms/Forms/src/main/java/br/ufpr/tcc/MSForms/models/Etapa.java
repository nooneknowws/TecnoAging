package br.ufpr.tcc.MSForms.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "tipoCalculo", column = @Column(name = "tipo_calculo")),
        @AttributeOverride(name = "formulaCustom", column = @Column(name = "formula_custom"))
    })
    private RegraCalculo regraCalculoEtapa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formulario_id")
    @JsonBackReference
    private Formulario formulario;

    @OneToMany(mappedBy = "etapa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Pergunta> perguntas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public RegraCalculo getRegraCalculoEtapa() {
        return regraCalculoEtapa;
    }

    public void setRegraCalculoEtapa(RegraCalculo regraCalculoEtapa) {
        this.regraCalculoEtapa = regraCalculoEtapa;
    }
}