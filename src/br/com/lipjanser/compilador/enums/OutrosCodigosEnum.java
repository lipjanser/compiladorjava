package br.com.lipjanser.compilador.enums;

public enum OutrosCodigosEnum {

    COMENTARIO(-3, "Comentário"),
    MENSAGEM_ERRO(-2, "Mensagem de Erro"),
    EOF(-1, "End of File"),
    ID(0, "id");

    private int codigo;
    private String descricao;

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    OutrosCodigosEnum(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

}
