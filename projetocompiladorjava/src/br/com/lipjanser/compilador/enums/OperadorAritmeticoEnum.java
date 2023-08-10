package br.com.lipjanser.compilador.enums;

public enum OperadorAritmeticoEnum {

    SOMA(7, "Soma"),
    SUBTRACAO(8, "Subtracao"),
    MULTIPLICACAO(9, "Multiplicacao"),
    DIVISAO(10, "Divisao"),
    ATRIBUICAO(11, "Atribuicao");

    private int codigoOperadorAritmetico;
    private String descricaoOperadorAritmetico;

    public int getCodigoOperadorAritmetico() {
        return codigoOperadorAritmetico;
    }

    public String getDescricaoOperadorAritmetico() {
        return descricaoOperadorAritmetico;
    }

    OperadorAritmeticoEnum(int codigoOperadorAritmetico, String descricaoOperadorAritmetico) {
        this.codigoOperadorAritmetico = codigoOperadorAritmetico;
        this.descricaoOperadorAritmetico = descricaoOperadorAritmetico;
    }
}
