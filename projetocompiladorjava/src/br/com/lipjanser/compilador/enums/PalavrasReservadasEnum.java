package br.com.lipjanser.compilador.enums;

public enum PalavrasReservadasEnum {

    MAIN(21, "main"),
    IF(22, "if"),
    ELSE(23, "else"),
    WHILE(24, "while"),
    DO(25, "do"),
    FOR(26, "for"),
    INT(27, "int"),
    FLOAT(28, "float"),
    CHAR(29, "char");

    private int codigoPalavraReservada;
    private String descricaoPalavraReservada;

    public int getCodigoPalavraReservada() {
        return codigoPalavraReservada;
    }

    public void setCodigoPalavraReservada(int codigoPalavraReservada) {
        this.codigoPalavraReservada = codigoPalavraReservada;
    }

    public String getDescricaoPalavraReservada() {
        return descricaoPalavraReservada;
    }

    public void setDescricaoPalavraReservada(String descricaoPalavraReservada) {
        this.descricaoPalavraReservada = descricaoPalavraReservada;
    }

    PalavrasReservadasEnum(int codigoPalavraReservada, String descricaoPalavraReservada) {
        this.codigoPalavraReservada = codigoPalavraReservada;
        this.descricaoPalavraReservada = descricaoPalavraReservada;
    }
}
