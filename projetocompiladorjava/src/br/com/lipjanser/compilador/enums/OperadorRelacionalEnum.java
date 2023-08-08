package br.com.lipjanser.compilador.enums;

public enum OperadorRelacionalEnum {

    MENORQUE(1, "Menor"),
    MAIORQUE(2, "Maior"),
    MENOROUIGUAL(3, "Menor ou Igual"),
    MAIOROUIGUAL(4, "Maior ou Igual"),
    IGUALA(5, "Igual"),
    DIFERENTEDE(6, "Diferente");

    private int codigoOperadorRelacional;
    private String descricaoOperadorRelacional;

    public int getCodigoOperadorRelacional() {
        return codigoOperadorRelacional;
    }

    public void setCodigoOperadorRelacional(int codigoOperadorRelacional) {
        this.codigoOperadorRelacional = codigoOperadorRelacional;
    }

    public String getDescricaoOperadorRelacional() {
        return descricaoOperadorRelacional;
    }

    public void setDescricaoOperadorRelacional(String descricaoOperadorRelacional) {
        this.descricaoOperadorRelacional = descricaoOperadorRelacional;
    }

    OperadorRelacionalEnum(int codigoOperadorRelacional, String descricaoOperadorRelacional) {
        this.codigoOperadorRelacional = codigoOperadorRelacional;
        this.descricaoOperadorRelacional = descricaoOperadorRelacional;
    }
}
