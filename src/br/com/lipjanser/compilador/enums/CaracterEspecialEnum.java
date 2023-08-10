package br.com.lipjanser.compilador.enums;

public enum CaracterEspecialEnum {

    ABREPARENTESES(12, "Abre Parenteses"),
    FECHAPARENTESES(13, "Fecha Parenteses"),
    ABRECHAVES(14, "Abre Chaves"),
    FECHACHAVES(15, "Fecha Chaves"),
    VIRGULA(16, "Virgula"),
    PONTOEVIRGULA(17, "Ponto e Virgula");

    private int codigoCaracterEspecial;
    private String descricaoCaracterEspecial;

    public int getCodigoCaracterEspecial() {
        return codigoCaracterEspecial;
    }

    public String getDescricaoCaracterEspecial() {
        return descricaoCaracterEspecial;
    }

    CaracterEspecialEnum(int codigoCaracterEspecial, String descricaoCaracterEspecial) {
        this.codigoCaracterEspecial = codigoCaracterEspecial;
        this.descricaoCaracterEspecial = descricaoCaracterEspecial;
    }

}
