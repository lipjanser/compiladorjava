package br.com.lipjanser.compilador.enums;

public enum ConstantesEnum {

    CONSTANTEINT(18, "Constante int"),
    CONSTANTEFLOAT(19, "Constante float"),
    CONSTANTECHAR(20, "Constante char");

    private int codigoConstante;
    private String descricaoConstante;

    public int getCodigoConstante() {
        return codigoConstante;
    }

    public String getDescricaoConstante() {
        return descricaoConstante;
    }

    ConstantesEnum(int codigoConstante, String descricaoConstante) {
        this.codigoConstante = codigoConstante;
        this.descricaoConstante = descricaoConstante;
    }

}
