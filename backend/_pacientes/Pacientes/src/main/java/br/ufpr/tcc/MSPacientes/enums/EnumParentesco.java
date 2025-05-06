package br.ufpr.tcc.MSPacientes.enums;

public enum EnumParentesco {
    PAI("Pai"),
    MAE("Mãe"),
    FILHO("Filho"),
    FILHA("Filha"),
    ESPOSA("Esposa"),
    MARIDO("Marido"),
    CONJUGUE("Conjuguê"),
    COMPANHEIRO("Companheiro"),
    COMPANHEIRA("Companheira"),
    NAMORADO("Namorado"),
    NAMORADA("Namorada"),

    AVO_PATERNO("Avô Paterno"),
    AVO_MATERNO("Avô Materno"),
    AVO_PATERNA("Avó Paterna"),
    AVO_MATERNA("Avó Materna"),
    BISAVO_PATERNO("Bisavô Paterno"),
    BISAVO_MATERNO("Bisavô Materno"),
    BISAVO_PATERNA("Bisavó Paterna"),
    BISAVO_MATERNA("Bisavó Materna"),

    NETO("Neto"),
    NETA("Neta"),
    BISNETO("Bisneto"),
    BISNETA("Bisneta"),

    IRMAO("Irmão"),
    IRMA("Irmã"),

    TIO_PATERNO("Tio Paterno"),
    TIO_MATERNO("Tio Materno"),
    TIA_PATERNA("Tia Paterna"),
    TIA_MATERNA("Tia Materna"),

    SOBRINHO("Sobrinho"),
    SOBRINHA("Sobrinha"),

    PRIMO("Primo"),
    PRIMA("Prima"),

    GENRO("Genro"),
    NORA("Nora"),
    SOGRO("Sogro"),
    SOGRA("Sogra"),

    CUIDADOR("Cuidador"),
    AMIGO("Amigo"),
    OUTRO("Outro");

    private final String valor;

    EnumParentesco(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}