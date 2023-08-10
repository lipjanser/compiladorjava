package br.com.lipjanser.compilador.classes;

/**
 * @author lipjanser
 */

import br.com.lipjanser.compilador.enums.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Compilador {

    private Token tk;
    private char lookahead = ' ';
    private int linhas = 1;
    private int colunas = 1;

    /**
     * Método que ignora caracteres espaço em branco, quebras de linha e tabulação e incrementa o contador de linhas e colunas.
     *
     * @param arq Arquivo
     */
    private void getNoBlank(BufferedReader arq) throws IOException {
        while (lookahead == ' ' || lookahead == '\n' || lookahead == '\t' || lookahead == '\r') {
            if (lookahead == '\t') {
                colunas += 4;
            }
            if (lookahead == '\n') { //||lookahead == '\r'){
                colunas = 1;
                linhas++;
            }
            colunas++;
            lookahead = (char) arq.read();
        }
    }

    /**
     * Método que retorna o próximo token válido.
     *
     * @param arquivo Arquivo
     * @return Retorna o próximo token válido.
     */
    private Token scanner(BufferedReader arquivo) throws IOException {
        Token token = new Token();
        StringBuilder buffer = new StringBuilder("");

        this.getNoBlank(arquivo);

        if (Character.isLetter(lookahead) || lookahead == '_') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();// Guarda no buffer e Lê do arquivo
            while (Character.isLetterOrDigit(lookahead) || lookahead == '_') {
                buffer.append(lookahead);
                colunas++;
                lookahead = (char) arquivo.read();// Guarda no buffer e Lê do arquivo
            }
            token.setToken(buffer.toString());
            isPalavraReservada(token);
            // Guarda o buffer no Objeto tk, verifica se é palavra reservada e retorna!
            return token;
        }

        if (Character.isDigit(lookahead)) {
            buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
            colunas++;
            lookahead = (char) arquivo.read();
            while (Character.isDigit(lookahead)) {
                buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
                colunas++;
                lookahead = (char) arquivo.read();
                if (lookahead == '.') {
                    buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
                    colunas++;
                    lookahead = (char) arquivo.read();
                    //Erro: falta digitos após o ponto
                    if (Character.isDigit(lookahead)) {
                        buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
                        colunas++;
                        lookahead = (char) arquivo.read();
                        while (Character.isDigit(lookahead)) {
                            buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
                            colunas++;
                            lookahead = (char) arquivo.read();
                        }

                        token.setToken(buffer.toString());
                        token.setNum(ConstantesEnum.CONSTANTEFLOAT.getCodigoConstante());
                        token.setClasse("Const. FLOAT");
                    } else {
                        token.setMsgErro("Erro: falta digitos após '.' .");
                        token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
                    }
                    return token;
                }
            }
            if (lookahead != '.') {
                token.setToken(buffer.toString());
                token.setNum(ConstantesEnum.CONSTANTEINT.getCodigoConstante());
                token.setClasse("Const. INT");
                return token;
            }

        }
        if (lookahead == '.') {
            buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
            colunas++;
            lookahead = (char) arquivo.read();
            if (Character.isDigit(lookahead)) {
                buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
                colunas++;
                lookahead = (char) arquivo.read();
                while (Character.isDigit(lookahead)) {
                    buffer.append(lookahead); //Guarda no buffer e Lê do arquivo
                    colunas++;
                    lookahead = (char) arquivo.read();
                }
            } else {
                token.setMsgErro("Erro: falta digitos após '.' .");
                token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
                return token; //Erro: falta digitos após o ponto
            }
            token.setToken(buffer.toString());
            token.setClasse("Const. FLOAT");
            token.setNum(ConstantesEnum.CONSTANTEFLOAT.getCodigoConstante()); // Guarda o buffer no Objeto tk, verifica se é constante int ou float e retorna!
            return token;
        }

        if (lookahead == '\'') {
            colunas++;
            lookahead = (char) arquivo.read(); //Não armazena no buffer, apenas lê do arquivo
            //Erro: formacao incorreta de constante char
            if (Character.isLetterOrDigit(lookahead)) {
                buffer.append(lookahead); //Armazena no buffer e lê do arquivo
                colunas++;
                lookahead = (char) arquivo.read();
                //Erro: formacao incorreta de constante char
                if (lookahead == '\'') {
                    colunas++;
                    token.setToken(buffer.toString());
                    token.setNum(ConstantesEnum.CONSTANTECHAR.getCodigoConstante());
                    token.setClasse("Const. CHAR"); // Guarda o buffer no Objeto tk, classifica-o como constante char e retorna!
                    lookahead = (char) arquivo.read();
                } else {
                    token.setToken(buffer.append(lookahead).toString());
                    token.setMsgErro("Erro:formacao incorreta de constante char");
                    token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
                }
            } else {
                token.setMsgErro("Erro:formacao incorreta de constante char");
                token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
            }
            return token;
        }

        if (lookahead == '+') {
            buffer.append(lookahead); //Armazena no buffer e lê do arquivo
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setClasse("Soma");
            token.setNum(OperadorAritmeticoEnum.SOMA.getCodigoOperadorAritmetico());
            return token;
        }
        if (lookahead == '-') {
            buffer.append(lookahead); //Armazena no buffer e lê do arquivo
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setClasse("Subtracao");
            token.setNum(OperadorAritmeticoEnum.SUBTRACAO.getCodigoOperadorAritmetico());
            return token;
        }
        if (lookahead == '*') {
            buffer.append(lookahead); //Armazena no buffer e lê do arquivo
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setClasse("Multiplicacao");
            token.setNum(OperadorAritmeticoEnum.MULTIPLICACAO.getCodigoOperadorAritmetico());
            return token;
        }

        if (lookahead == '/') {
            colunas++;
            lookahead = (char) arquivo.read();
            if (lookahead == '/') {
                while (lookahead != '\n') {
                    lookahead = (char) arquivo.read();
                    colunas++;
                    if (lookahead == '\n') {
                        colunas = 1;
                        linhas++;
                        token.setNum(OutrosCodigosEnum.COMENTARIO.getCodigo());
                        lookahead = (char) arquivo.read();
                        return token;
                    }
                }
            } else if (lookahead == '*') {
                colunas++;
                lookahead = (char) arquivo.read();
                while (true) {
                    while (lookahead != '*') {
                        colunas++;
                        lookahead = (char) arquivo.read();
                        if (lookahead == '*') {
                            break;
                        }
                        if (lookahead == '\n') {
                            colunas = 1;
                            linhas++;
                        }
                        if (!arquivo.ready()) {
                            token.setMsgErro("(" + OutrosCodigosEnum.EOF.getDescricao() + ") > Fim de arquivo encontrado antes do comentário ser fechado.");
                            token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
                            return token;
                        }
                    }
                    while (lookahead == '*') {
                        colunas++;
                        lookahead = (char) arquivo.read();
                    }
                    if (lookahead == '/') {
                        token.setNum(OutrosCodigosEnum.COMENTARIO.getCodigo());
                        lookahead = (char) arquivo.read();
                        return token;
                    }
                    if (!arquivo.ready()) {
                        token.setMsgErro("Fim de arquivo encontrado antes do comentário ser fechado!");
                        token.setNum(OutrosCodigosEnum.EOF.getCodigo());
                        return token;
                    }
                }
            } else {
                token.setToken("/");
                token.setNum(OperadorAritmeticoEnum.DIVISAO.getCodigoOperadorAritmetico());
                token.setClasse("Divisao");
                return token;
            }
        }

        if (lookahead == '<') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read(); // Guarda no Buffer e Lê do arquivo
            if (lookahead == '=') {
                buffer.append(lookahead);
                colunas++;
                lookahead = (char) arquivo.read();
                token.setToken(buffer.toString());
                token.setNum(OperadorRelacionalEnum.MENOROUIGUAL.getCodigoOperadorRelacional());
                token.setClasse(OperadorRelacionalEnum.MENOROUIGUAL.getDescricaoOperadorRelacional());
                // Guarda no Buffer e Lê do arquivo e retorna
                return token;
            }
            token.setToken(buffer.toString());
            token.setNum(OperadorRelacionalEnum.MENORQUE.getCodigoOperadorRelacional());
            token.setClasse(OperadorRelacionalEnum.MENORQUE.getDescricaoOperadorRelacional());
            return token;
        }
        if (lookahead == '>') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read(); //Guarda no Buffer e Lê do arquivo
            if (lookahead == '=') {
                buffer.append(lookahead);
                colunas++;
                lookahead = (char) arquivo.read();
                token.setToken(buffer.toString());
                token.setNum(OperadorRelacionalEnum.MAIOROUIGUAL.getCodigoOperadorRelacional());
                token.setClasse(OperadorRelacionalEnum.MAIOROUIGUAL.getDescricaoOperadorRelacional());
                //Guarda no Buffer e Lê do arquivo e retorna
                return token;
            }
            token.setToken(buffer.toString());
            token.setNum(OperadorRelacionalEnum.MAIORQUE.getCodigoOperadorRelacional());
            token.setClasse(OperadorRelacionalEnum.MAIORQUE.getDescricaoOperadorRelacional());
            return token;
        }
        if (lookahead == '!') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read(); //Guarda no Buffer e Lê do arquivo
            if (lookahead == '=') {
                buffer.append(lookahead);
                colunas++;
                lookahead = (char) arquivo.read();
                token.setToken(buffer.toString());
                token.setNum(OperadorRelacionalEnum.DIFERENTEDE.getCodigoOperadorRelacional());
                token.setClasse(OperadorRelacionalEnum.DIFERENTEDE.getDescricaoOperadorRelacional()); //Guarda no Buffer e Lê do arquivo
            } else {
                token.setMsgErro("Erro: O token '!' nao pode aparecer sozinho.");
                token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
            }
            return token;
        }
        if (lookahead == '=') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            if (lookahead == '=') {
                buffer.append(lookahead);
                colunas++;
                lookahead = (char) arquivo.read();
                token.setToken(buffer.toString());
                token.setNum(OperadorRelacionalEnum.IGUALA.getCodigoOperadorRelacional());
                token.setClasse(OperadorRelacionalEnum.IGUALA.getDescricaoOperadorRelacional());
                return token; //Lê do arquivo
            }
            token.setToken(buffer.toString());
            token.setNum(OperadorAritmeticoEnum.ATRIBUICAO.getCodigoOperadorAritmetico());
            token.setClasse(OperadorAritmeticoEnum.ATRIBUICAO.getDescricaoOperadorAritmetico());
            return token;
        }

        if (lookahead == ')') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setNum(CaracterEspecialEnum.FECHAPARENTESES.getCodigoCaracterEspecial());
            token.setClasse(CaracterEspecialEnum.FECHAPARENTESES.getDescricaoCaracterEspecial());
            return token;
        }
        if (lookahead == '(') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setNum(CaracterEspecialEnum.ABREPARENTESES.getCodigoCaracterEspecial());
            token.setClasse(CaracterEspecialEnum.ABREPARENTESES.getDescricaoCaracterEspecial());
            return token;
        }
        if (lookahead == '{') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setNum(CaracterEspecialEnum.ABRECHAVES.getCodigoCaracterEspecial());
            token.setClasse(CaracterEspecialEnum.ABRECHAVES.getDescricaoCaracterEspecial());
            return token;
        }
        if (lookahead == '}') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setNum(CaracterEspecialEnum.FECHACHAVES.getCodigoCaracterEspecial());
            token.setClasse(CaracterEspecialEnum.FECHACHAVES.getDescricaoCaracterEspecial());
            return token;
        }
        if (lookahead == ',') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setNum(CaracterEspecialEnum.VIRGULA.getCodigoCaracterEspecial());
            token.setClasse(CaracterEspecialEnum.VIRGULA.getDescricaoCaracterEspecial());
            return token;
        }
        if (lookahead == ';') {
            buffer.append(lookahead);
            colunas++;
            lookahead = (char) arquivo.read();
            token.setToken(buffer.toString());
            token.setNum(CaracterEspecialEnum.PONTOEVIRGULA.getCodigoCaracterEspecial());
            token.setClasse(CaracterEspecialEnum.PONTOEVIRGULA.getDescricaoCaracterEspecial());
        } else {
            if (!arquivo.ready()) {
                token.setMsgErro("Fim de arquivo encontrado!");
                token.setNum(OutrosCodigosEnum.EOF.getCodigo());
                return token;
            }
            token.setMsgErro("Erro: caracter invalido '" + lookahead + "' encontrado.");
            token.setNum(OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo());
        }
        return token;
    }

    private void isPalavraReservada(Token tk) {

        if (tk.getToken().equals(PalavrasReservadasEnum.MAIN.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - main");
            tk.setNum(PalavrasReservadasEnum.MAIN.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.IF.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - if");
            tk.setNum( PalavrasReservadasEnum.IF.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.ELSE.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - else");
            tk.setNum(PalavrasReservadasEnum.ELSE.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.WHILE.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - while");
            tk.setNum(PalavrasReservadasEnum.WHILE.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.DO.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - do");
            tk.setNum(PalavrasReservadasEnum.DO.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.FOR.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - for");
            tk.setNum(PalavrasReservadasEnum.FOR.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.INT.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - int");
            tk.setNum(PalavrasReservadasEnum.INT.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.FLOAT.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - float");
            tk.setNum(PalavrasReservadasEnum.FLOAT.getCodigoPalavraReservada());
            return;
        }
        if (tk.getToken().equals(PalavrasReservadasEnum.CHAR.getDescricaoPalavraReservada())) {
            tk.setClasse("PR - char");
            tk.setNum(PalavrasReservadasEnum.CHAR.getCodigoPalavraReservada());
            return;
        }

        tk.setClasse("Identificador");
        tk.setNum(OutrosCodigosEnum.ID.getCodigo());
    }

    /**
     * Método utilizado para verificar a ocorrência de comentários, uso da palavra reservada 'for' e mensagem de erro.
     *
     * @param arquivo Arquivo.
     * @throws IOException
     */
    private void checkComentError(BufferedReader arquivo) throws IOException {

        while (tk.getNum() == OutrosCodigosEnum.COMENTARIO.getCodigo()) {
            tk = scanner(arquivo);
        }

        if (tk.getNum() == PalavrasReservadasEnum.FOR.getCodigoPalavraReservada()) {
            System.out.println("\nL/C: " + linhas + "/" + colunas + "\nErro: Operações com a palavra reservada 'for' não estão disponiveis nesta versão do aplicativo.");
            arquivo.close();
            System.exit(0);
        }

        if (tk.getNum() == OutrosCodigosEnum.MENSAGEM_ERRO.getCodigo()) {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " " + tk.getMsgErro());
            arquivo.close();
            System.exit(0);
        }

    }

    public void simboloInicial(BufferedReader arquivo) throws IOException {
        this.getNextToken(arquivo);
        this.programa(arquivo);
    }

    private void programa(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == PalavrasReservadasEnum.INT.getCodigoPalavraReservada()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Palavra 'int' era esperada.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        if (tk.getNum() == PalavrasReservadasEnum.MAIN.getCodigoPalavraReservada()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Palavra 'main' era esperada.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        if (tk.getNum() == CaracterEspecialEnum.ABREPARENTESES.getCodigoCaracterEspecial()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '(' era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        if (tk.getNum() == CaracterEspecialEnum.FECHAPARENTESES.getCodigoCaracterEspecial()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ')' era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        this.bloco(arquivo);

        if (tk.getNum() != OutrosCodigosEnum.EOF.getCodigo()) {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Há tokens fora do bloco principal.");
            System.out.println("Ultimo token lido -> " + tk.getToken());
            arquivo.close();
            System.exit(0);
        }

    }

    private void bloco(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == CaracterEspecialEnum.ABRECHAVES.getCodigoCaracterEspecial()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '{' era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        while (true) {
            if (tk.getNum() == PalavrasReservadasEnum.INT.getCodigoPalavraReservada()
                    || tk.getNum() == PalavrasReservadasEnum.FLOAT.getCodigoPalavraReservada()
                    || tk.getNum() == PalavrasReservadasEnum.CHAR.getCodigoPalavraReservada())
                this.declaracoes(arquivo);
            else
                break;
        }

        while (true) {
            if (tk.getNum() == PalavrasReservadasEnum.IF.getCodigoPalavraReservada()
                    || tk.getNum() == OutrosCodigosEnum.ID.getCodigo()
                    || tk.getNum() == CaracterEspecialEnum.ABRECHAVES.getCodigoCaracterEspecial()
                    || tk.getNum() == PalavrasReservadasEnum.DO.getCodigoPalavraReservada()
                    || tk.getNum() == PalavrasReservadasEnum.WHILE.getCodigoPalavraReservada())
                this.comando(arquivo);
            else
                break;
        }

        if (tk.getNum() == CaracterEspecialEnum.FECHACHAVES.getCodigoCaracterEspecial()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '}' era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }
    }

    private void declaracoes(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == PalavrasReservadasEnum.INT.getCodigoPalavraReservada()
                || tk.getNum() == PalavrasReservadasEnum.FLOAT.getCodigoPalavraReservada()
                || tk.getNum() == PalavrasReservadasEnum.CHAR.getCodigoPalavraReservada()) {
            this.getNextToken(arquivo);
        }

        if (tk.getNum() == OutrosCodigosEnum.ID.getCodigo()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um identificador era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        while (tk.getNum() != CaracterEspecialEnum.PONTOEVIRGULA.getCodigoCaracterEspecial()) {
            if (tk.getNum() == CaracterEspecialEnum.VIRGULA.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
                if (tk.getNum() == OutrosCodigosEnum.ID.getCodigo()) {
                    this.getNextToken(arquivo);
                } else {
                    System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um identificador era esperado.");
                    System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                    arquivo.close();
                    System.exit(0);
                }
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um identificador ou ';' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }
        }

        if (tk.getNum() == CaracterEspecialEnum.PONTOEVIRGULA.getCodigoCaracterEspecial()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ';' era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }
    }

    private void comando(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == PalavrasReservadasEnum.IF.getCodigoPalavraReservada()) {
            this.getNextToken(arquivo);
            if (tk.getNum() == CaracterEspecialEnum.ABREPARENTESES.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '(' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken());
                arquivo.close();
                System.exit(0);
            }

            this.exprRelacional(arquivo);

            if (tk.getNum() == CaracterEspecialEnum.FECHAPARENTESES.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ')' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }

            this.comando(arquivo);

            if (tk.getNum() == PalavrasReservadasEnum.ELSE.getCodigoPalavraReservada()) {
                this.getNextToken(arquivo);
                this.comando(arquivo);
                return;
            }
            return;
        }

        if (tk.getNum() == OutrosCodigosEnum.ID.getCodigo()
                || tk.getNum() == CaracterEspecialEnum.ABRECHAVES.getCodigoCaracterEspecial()) {
            this.comandoBasico(arquivo);
            return;
        }

        if (tk.getNum() == PalavrasReservadasEnum.DO.getCodigoPalavraReservada()
                || tk.getNum() == PalavrasReservadasEnum.WHILE.getCodigoPalavraReservada()) {
            this.iteracao(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um comando era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }
    }

    private void exprRelacional(BufferedReader arquivo) throws IOException {

        this.exprAritmetica(arquivo);

        if (tk.getNum() == OperadorRelacionalEnum.DIFERENTEDE.getCodigoOperadorRelacional()
                || tk.getNum() == OperadorRelacionalEnum.IGUALA.getCodigoOperadorRelacional()
                || tk.getNum() == OperadorRelacionalEnum.MAIORQUE.getCodigoOperadorRelacional()
                || tk.getNum() == OperadorRelacionalEnum.MAIOROUIGUAL.getCodigoOperadorRelacional()
                || tk.getNum() == OperadorRelacionalEnum.MENORQUE.getCodigoOperadorRelacional()
                || tk.getNum() == OperadorRelacionalEnum.MENOROUIGUAL.getCodigoOperadorRelacional()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um Op.Relacional era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }

        this.exprAritmetica(arquivo);
    }

    private void exprAritmetica(BufferedReader arquivo) throws IOException {
        this.termo(arquivo);
        this.exprArit(arquivo);
    }

    private void termo(BufferedReader arquivo) throws IOException {
        this.fator(arquivo);
        this.termoL(arquivo);
    }

    private void fator(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == CaracterEspecialEnum.ABREPARENTESES.getCodigoCaracterEspecial()) {
            this.getNextToken(arquivo);
            this.exprAritmetica(arquivo);
            if (tk.getNum() == CaracterEspecialEnum.FECHAPARENTESES.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
                return;
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ')' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }

        }

        if (tk.getNum() == OutrosCodigosEnum.ID.getCodigo()
                || tk.getNum() == ConstantesEnum.CONSTANTEINT.getCodigoConstante()
                || tk.getNum() == ConstantesEnum.CONSTANTEFLOAT.getCodigoConstante()
                || tk.getNum() == ConstantesEnum.CONSTANTECHAR.getCodigoConstante()) {
            this.getNextToken(arquivo);
        } else {
            System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um identificador ou constante era esperado.");
            System.out.println("Ultimo token lido '" + tk.getToken() + "'");
            arquivo.close();
            System.exit(0);
        }
    }

    private void exprArit(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == OperadorAritmeticoEnum.SOMA.getCodigoOperadorAritmetico()) {
            this.getNextToken(arquivo);
            this.termo(arquivo);
            this.exprArit(arquivo);
        } else if (tk.getNum() == OperadorAritmeticoEnum.SUBTRACAO.getCodigoOperadorAritmetico()) {
            this.getNextToken(arquivo);
            this.termo(arquivo);
            this.exprArit(arquivo);
        }
    }

    private void termoL(BufferedReader arquivo) throws IOException {
        if (tk.getNum() == OperadorAritmeticoEnum.MULTIPLICACAO.getCodigoOperadorAritmetico()) {
            this.getNextToken(arquivo);
            this.fator(arquivo);
            this.termoL(arquivo);
        } else if (tk.getNum() == OperadorAritmeticoEnum.DIVISAO.getCodigoOperadorAritmetico()) {
            this.getNextToken(arquivo);
            this.fator(arquivo);
            this.termoL(arquivo);
        }
    }

    private void atribuicao(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == OutrosCodigosEnum.ID.getCodigo()) {
            this.getNextToken(arquivo);
            if (tk.getNum() == OperadorAritmeticoEnum.ATRIBUICAO.getCodigoOperadorAritmetico()) {
                this.getNextToken(arquivo);
                this.exprAritmetica(arquivo);
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '=' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }

            if (tk.getNum() == CaracterEspecialEnum.PONTOEVIRGULA.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ';' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }
        }
    }

    private void comandoBasico(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == OutrosCodigosEnum.ID.getCodigo()) {
            this.atribuicao(arquivo);
            return;
        }

        if (tk.getNum() == CaracterEspecialEnum.ABRECHAVES.getCodigoCaracterEspecial()) {
            this.bloco(arquivo);
        }
    }

    private void iteracao(BufferedReader arquivo) throws IOException {

        if (tk.getNum() == PalavrasReservadasEnum.DO.getCodigoPalavraReservada()) {
            this.getNextToken(arquivo);
            this.comando(arquivo);
            if (tk.getNum() == PalavrasReservadasEnum.WHILE.getCodigoPalavraReservada()) {
                this.getNextToken(arquivo);
                if (tk.getNum() == CaracterEspecialEnum.ABREPARENTESES.getCodigoCaracterEspecial()) {
                    this.getNextToken(arquivo);
                } else {
                    System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '(' era esperado.");
                    System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                    arquivo.close();
                    System.exit(0);
                }

                this.exprRelacional(arquivo);

                if (tk.getNum() == CaracterEspecialEnum.FECHAPARENTESES.getCodigoCaracterEspecial()) {
                    this.getNextToken(arquivo);
                } else {
                    System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ')' era esperado.");
                    System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                    arquivo.close();
                    System.exit(0);
                }

                if (tk.getNum() == CaracterEspecialEnum.PONTOEVIRGULA.getCodigoCaracterEspecial()) {
                    this.getNextToken(arquivo);
                    return;
                } else {
                    System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ';' era esperado.");
                    System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                    arquivo.close();
                    System.exit(0);
                }
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Palavra 'while' era esperada para contrução da estrutura DO-WHILE.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }

        } //Iteracao DO WHILE

        if (tk.getNum() == PalavrasReservadasEnum.WHILE.getCodigoPalavraReservada()) {
            this.getNextToken(arquivo);
            if (tk.getNum() == CaracterEspecialEnum.ABREPARENTESES.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um '(' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }

            this.exprRelacional(arquivo);

            if (tk.getNum() == CaracterEspecialEnum.FECHAPARENTESES.getCodigoCaracterEspecial()) {
                this.getNextToken(arquivo);
            } else {
                System.out.println("\nL/C: " + linhas + "/" + colunas + " Erro: Um ')' era esperado.");
                System.out.println("Ultimo token lido '" + tk.getToken() + "'");
                arquivo.close();
                System.exit(0);
            }

            this.comando(arquivo);
        } // Iteracao WHILE
    }

    /**
     * Método utilizado para obter o próximo token,
     *
     * @param arquivo Arquivo.
     */
    private void getNextToken(BufferedReader arquivo) throws IOException {
        this.tk = this.scanner(arquivo);
        this.checkComentError(arquivo);
        System.out.println("\n-> " + this.tk.getToken());
    }

}