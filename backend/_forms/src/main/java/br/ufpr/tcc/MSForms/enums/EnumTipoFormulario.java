package br.ufpr.tcc.MSForms.enums;

public enum EnumTipoFormulario {
    SEDENTARISMO("sedentarismo"),
    IVCF20("ivcf20"),
    PFS("pfs"),
    MINIMENTAL("minimental"),
    FACTF("factf");

    private final String valor;

    EnumTipoFormulario(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}