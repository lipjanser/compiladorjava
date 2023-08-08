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

    public void setCodigoOperadorAritmetico(int codigoOperadorAritmetico) {
        this.codigoOperadorAritmetico = codigoOperadorAritmetico;
    }

    public String getDescricaoOperadorAritmetico() {
        return descricaoOperadorAritmetico;
    }

    public void setDescricaoOperadorAritmetico(String descricaoOperadorAritmetico) {
        this.descricaoOperadorAritmetico = descricaoOperadorAritmetico;
    }

    OperadorAritmeticoEnum(int codigoOperadorAritmetico, String descricaoOperadorAritmetico) {
        this.codigoOperadorAritmetico = codigoOperadorAritmetico;
        this.descricaoOperadorAritmetico = descricaoOperadorAritmetico;
    }
}
