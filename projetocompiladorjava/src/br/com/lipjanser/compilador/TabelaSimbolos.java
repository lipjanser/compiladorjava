package br.com.lipjanser.compilador;

public class TabelaSimbolos {

    private int tipo = 0;
    private String id = "";
    private int escopo = 0;

    public TabelaSimbolos() {
        this.tipo = 0;
        this.id = "";
        this.escopo = 0;
    }

    public TabelaSimbolos(int tipo, String id, int escopo) {
        this.tipo = tipo;
        this.id = id;
        this.escopo = escopo;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getEscopo() {
        return escopo;
    }

    public void setEscopo(int escopo) {
        this.escopo = escopo;
    }
}