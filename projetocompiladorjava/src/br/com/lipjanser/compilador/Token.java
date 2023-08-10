package br.com.lipjanser.compilador;

public class Token {
    private String token;
    private String classe;
    private String msgErro;
    private int num;

    public Token() {
        this.token = "";
        this.classe = "";
        this.msgErro = "";
        this.num = -1;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}