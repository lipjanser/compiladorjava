package janserx2;

/**
 *
 * @author FELIPE
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Stack;

/**  Gramática
 * <programa> ::= int main"("")" <bloco>
 * <bloco> ::= “{“ {<decl_var>}* {<comando>}* “}”
 * <decl_var> ::= <tipo> <id> {,<id>}* ";"
 * <tipo> ::= int | float | char
 * <comando> ::= <comando_básico> | <iteração> | if "("<expr_relacional>")" <comando> {else <comando>}?
 * <comando_básico> ::= <atribuição> | <bloco>
 * <iteração> ::= while "("<expr_relacional>")" <comando> | do <comando> while "("<expr_relacional>")"";"
 * <atribuição> ::= <id> "=" <expr_arit> ";"
 * <expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>
 * <expr_arit> ::= <termo> <expr_arit'>
 * <expr_arit'> ::= "+" <termo> <expr_arit'>
 * <expr_arit'> ::= "-" <termo> <expr_arit'>
 * <expr_arit'> ::= VAZIO
 * <termo> ::= <fator> <termo'>
 * <termo'> ::= "*" <fator> <termo'>
 * <termo'> ::= “/” <fator> <termo'>
 * <termo'> ::= VAZIO
 * <fator> ::= “(“ <expr_arit> “)”
 * <fator> ::= <id>
 * <fator> ::= <real>
 * <fator> ::= <inteiro>
 * <fator> ::= <char>
 */

public class Compilador {

    Token tk;
    //Stack<TabelaSimbolos> tbSimbArray = new Stack<TabelaSimbolos>();
    //TabelaSimbolos tb = new TabelaSimbolos(), tbAux;
    //int posTab = 0;
    char lookahead = ' ';
    PalavrasReservadas palReserv = new PalavrasReservadas();
    int linhas = 1;
    int colunas = 1;

    public static final int COMENTARIO         = -3;
    public static final int MENSAGEM_ERRO      = -2;
    public static final int EOF                = -1;
    public static final int ID                 =  0;
    public static final int OP_REL_MENOR       =  1;
    public static final int OP_REL_MAIOR       =  2;
    public static final int OP_REL_MENOR_IGUAL =  3;
    public static final int OP_REL_MAIOR_IGUAL =  4;
    public static final int OP_REL_IGUAL       =  5;
    public static final int OP_REL_DIF         =  6;
    public static final int OP_ARIT_SOMA       =  7;
    public static final int OP_ARIT_SUB        =  8;
    public static final int OP_ARIT_MULT       =  9;
    public static final int OP_ARIT_DIV        = 10;
    public static final int OP_ARIT_ATRIBUICAO = 11;
    public static final int ESP_ABRE_PARENT    = 12;
    public static final int ESP_FECHA_PARENT   = 13;
    public static final int ESP_ABRE_CHAVES    = 14;
    public static final int ESP_FECHA_CHAVES   = 15;
    public static final int ESP_VIRGULA        = 16;
    public static final int ESP_PONTO_VIRGULA  = 17;
    public static final int CONST_INT          = 18;
    public static final int CONST_FLOAT        = 19;
    public static final int CONST_CHAR         = 20;
    public static final int PAL_RESERV_MAIN    = 21;
    public static final int PAL_RESERV_IF      = 22;
    public static final int PAL_RESERV_ELSE    = 23;
    public static final int PAL_RESERV_WHILE   = 24;
    public static final int PAL_RESERV_DO      = 25;
    public static final int PAL_RESERV_FOR     = 26;
    public static final int PAL_RESERV_INT     = 27;
    public static final int PAL_RESERV_FLOAT   = 28;
    public static final int PAL_RESERV_CHAR    = 29;
    
    private void getNoBlank(BufferedReader arq) throws IOException{
        while(lookahead == ' ' || lookahead == '\n' || lookahead == '\t' || lookahead == '\r'){
            if(lookahead == '\t') {
                colunas += 4;
            }
            if(lookahead == '\n'){//||lookahead == '\r'){
                colunas = 1;
                linhas++;
            }
            colunas++;
            lookahead = (char) arq.read();
        }
    }

    private Token scanner(BufferedReader arq) throws IOException {//Enviar arquivo por parametro!
        Token token = new Token();
        String buffer = "";

        getNoBlank(arq);

        if(Character.isLetter(lookahead)||lookahead == '_'){
            buffer = buffer + lookahead;
            colunas++;
            lookahead = (char) arq.read();//Guarda no buffer e Lê do arquivo
            while(Character.isLetterOrDigit(lookahead)||lookahead == '_'){
             buffer = buffer + lookahead;
             colunas++;
             lookahead = (char)arq.read();//Guarda no buffer e Lê do arquivo
            }
            token.token = buffer;
            isPalavraReservada(token);
            // Guarda o buffer no Objeto tk, verifica se é palavra reservada e retorna!
            return token;
        }

        if(Character.isDigit(lookahead)){
           buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
           colunas++;
           lookahead = (char) arq.read();
           while(Character.isDigit(lookahead)){
               buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
               colunas++;
               lookahead = (char) arq.read();
               if(lookahead == '.'){
                   buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
                   colunas++;
                   lookahead = (char) arq.read();
                  if(Character.isDigit(lookahead)){
                      buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
                      colunas++;
                      lookahead = (char) arq.read();
                      while(Character.isDigit(lookahead)){
                          buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
                          colunas++;
                          lookahead = (char) arq.read();
                      }

                      token.token = buffer;
                      token.num = CONST_FLOAT;
                      token.classe = "Const. FLOAT";
                      return token;
                  }
                  else{
                     token.msgErro = "Erro: falta digitos após '.' .";
                     token.num = MENSAGEM_ERRO;
                     return token;//Erro: falta digitos após o ponto
                  }
               }
//               else{
//                    token.token = buffer;
//                    token.num = CONST_INT;
//                    token.classe = "Const. INT";
//                    return token;
//               }
            }
           if(lookahead != '.'){
            token.token = buffer;
            token.num = CONST_INT;
            token.classe = "Const. INT";
            return token;
           }

        }
        if(lookahead == '.'){
            buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
            colunas++;
            lookahead = (char) arq.read();
                  if(Character.isDigit(lookahead)){
                      buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
                      colunas++;
                      lookahead = (char) arq.read();
                      while(Character.isDigit(lookahead)){
                          buffer = buffer + lookahead; //Guarda no buffer e Lê do arquivo
                          colunas++;
                          lookahead = (char) arq.read();
                      }
                  }
                  else{
                  token.msgErro = "Erro: falta digitos após '.' .";
                  token.num = MENSAGEM_ERRO;
                  return token;//Erro: falta digitos após o ponto
                  }
            token.token = buffer;
            token.classe = "Const. FLOAT";
            token.num = CONST_FLOAT;// Guarda o buffer no Objeto tk, verifica se é constante int ou float e retorna!
            return token;
        }

        if(lookahead == '\''){
          colunas++;
          lookahead = (char) arq.read(); //Não armazena no buffer, apenas lê do arquivo
          if(Character.isLetterOrDigit(lookahead)){
             buffer = buffer + lookahead; //Armazena no buffer e lê do arquivo
             colunas++;
             lookahead = (char) arq.read();
             if(lookahead == '\''){
                 colunas++;
                 token.token = buffer;
                 token.num = CONST_CHAR;
                 token.classe = "Const. CHAR";// Guarda o buffer no Objeto tk, classifica-o como constante char e retorna!
                 lookahead = (char) arq.read();
                 return token;
             }
             else{
                token.token = buffer + lookahead;
                token.msgErro = "Erro:formacao incorreta de constante char";
                token.num = MENSAGEM_ERRO;
                return token;//Erro:formacao incorreta de constante char
             }
          }
          else{
                token.msgErro = "Erro:formacao incorreta de constante char";
                token.num = MENSAGEM_ERRO;
                return token;//Erro:formacao incorreta de constante char
          }
        }

        if(lookahead == '+'){
           buffer = buffer + lookahead; //Armazena no buffer e lê do arquivo
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.classe = "Soma";
           token.num = OP_ARIT_SOMA;
           return token;
        }
        if(lookahead == '-'){
           buffer = buffer + lookahead; //Armazena no buffer e lê do arquivo
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.classe = "Subtracao";
           token.num = OP_ARIT_SUB;
           return token;

        }
        if(lookahead == '*'){
           buffer = buffer + lookahead; //Armazena no buffer e lê do arquivo
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.classe = "Multiplicacao";
           token.num = OP_ARIT_MULT;
           return token;
        }

        if(lookahead == '/'){
           colunas++;
           lookahead = (char) arq.read();
           if(lookahead == '/'){
                 while(lookahead != '\n'){
                       lookahead = (char) arq.read();
                       colunas++;
                      if(lookahead == '\n'){
                        colunas = 1;
                        linhas++;
                        token.num = COMENTARIO;
                        lookahead = (char) arq.read();
                        return token;
                     }
                 }
           }
           else if (lookahead == '*') {
                 colunas++;
                 lookahead = (char) arq.read();
                 while(true){
                        while(lookahead != '*'){
                            colunas++;
                            lookahead = (char) arq.read();
                            if(lookahead == '*'){
                                break;
                            }
                            if(lookahead == '\n'){
                               colunas = 1;
                               linhas++;
                            }
                            if(arq.ready() == false){
                               token.msgErro = "(" + EOF + ") > Fim de arquivo encontrado antes do comentário ser fechado.";
                               token.num = MENSAGEM_ERRO;
                               return token;
                            }
                        }
                        while(lookahead == '*'){
                                    colunas++;
                                    lookahead = (char) arq.read();
                        }
                        if(lookahead == '/'){
                            token.num = COMENTARIO;
                            lookahead = (char) arq.read();
                            return token;
                        }
                        if(arq.ready() == false){
                           token.msgErro = "Fim de arquivo encontrado antes do comentário ser fechado!";
                           token.num = EOF;
                           return token;
                        }
                 }

              }
              else{
                  token.token = "/";
                  token.num = OP_ARIT_DIV;
                  token.classe = "Divisao";
                  return token;
              }
        }

        if(lookahead == '<'){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();//Guarda no Buffer e Lê do arquivo
           if(lookahead == '='){
               buffer = buffer + lookahead;
               colunas++;
               lookahead = (char) arq.read();
               token.token = buffer;
               token.num = OP_REL_MENOR_IGUAL;
               token.classe = "Menor ou Igual";
              //Guarda no Buffer e Lê do arquivo e retorna
               return token;
            }
           token.token = buffer;
           token.num = OP_REL_MENOR;
           token.classe = "Menor";
           return token;
        }
        if(lookahead == '>'){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();//Guarda no Buffer e Lê do arquivo
           if(lookahead == '='){
               buffer = buffer + lookahead;
               colunas++;
               lookahead = (char) arq.read();
               token.token = buffer;
               token.num = OP_REL_MAIOR_IGUAL;
               token.classe = "Maior ou Igual";
              //Guarda no Buffer e Lê do arquivo e retorna
               return token;
           }
           token.token = buffer;
           token.num = OP_REL_MAIOR;
           token.classe = "Maior";
           return token;
        }
        if(lookahead == '!'){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();//Guarda no Buffer e Lê do arquivo
            if(lookahead == '='){
               buffer = buffer + lookahead;
               colunas++;
               lookahead = (char) arq.read();
               token.token = buffer;
               token.num = OP_REL_DIF;
               token.classe = "Diferente";//Guarda no Buffer e Lê do arquivo
               return token;
            }
            else{
              token.msgErro = "Erro: O token '!' nao pode aparecer sozinho.";
              token.num = MENSAGEM_ERRO;
              return token;
            }
        }
        if(lookahead == '='){
            buffer = buffer + lookahead;
            colunas++;
            lookahead = (char) arq.read();
            if(lookahead == '='){
               buffer = buffer + lookahead;
               colunas++;
               lookahead = (char) arq.read();
               token.token = buffer;
               token.num = OP_REL_IGUAL;
               token.classe = "Igual";
               return token;//Lê do arquivo
            }
            token.token = buffer;
            token.num = OP_ARIT_ATRIBUICAO;
            token.classe = "Atribuicao";
            return token;
        }

        if(lookahead == ')'){
            buffer = buffer + lookahead;
            colunas++;
            lookahead = (char) arq.read();
            token.token = buffer;
            token.num = ESP_FECHA_PARENT;
            token.classe = "Fecha Parenteses";
            return token;
        }
        if(lookahead == '('){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.num = ESP_ABRE_PARENT;
           token.classe = "Abre Parenteses";
           return token;
        }
        if(lookahead == '{'){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.num = ESP_ABRE_CHAVES;
           token.classe = "Abre Chaves";
           return token;
        }
        if(lookahead == '}'){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.num = ESP_FECHA_CHAVES;
           token.classe = "Fecha Chaves";
           return token;
        }
        if(lookahead == ','){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.num = ESP_VIRGULA;
           token.classe = "Virgula";
           return token;
        }
        if(lookahead == ';'){
           buffer = buffer + lookahead;
           colunas++;
           lookahead = (char) arq.read();
           token.token = buffer;
           token.num = ESP_PONTO_VIRGULA;
           token.classe = "Ponto e Virgula";
           return token;
        }
        else{
           if(arq.ready() == false){
             token.msgErro = "Fim de arquivo encontrado!";
             token.num = EOF;
             return token;
           }
           token.msgErro = "Erro: caracter invalido '"+lookahead+"' encontrado.";
           token.num = MENSAGEM_ERRO;
           return token;
        }

    }
    
    private void isPalavraReservada(Token tk){

        if(tk.token.equals(palReserv.getpMain())){
            tk.classe = "PR - main";
            tk.num = PAL_RESERV_MAIN;
            return;
        }
        if(tk.token.equals(palReserv.getpIf())){
            tk.classe = "PR - if";
            tk.num = PAL_RESERV_IF;
            return;
        }
        if(tk.token.equals(palReserv.getpElse())){
            tk.classe = "PR - else";
            tk.num = PAL_RESERV_ELSE;
            return;
        }
        if(tk.token.equals(palReserv.getpWhile())){
            tk.classe = "PR - while";
            tk.num = PAL_RESERV_WHILE;
            return;
        }
        if(tk.token.equals(palReserv.getpDo())){
            tk.classe = "PR - do";
            tk.num = PAL_RESERV_DO;
            return;
        }
        if(tk.token.equals(palReserv.getpFor())){
            tk.classe = "PR - for";
            tk.num = PAL_RESERV_FOR;
            return;
        }
        if(tk.token.equals(palReserv.getpInt())){
            tk.classe = "PR - int";
            tk.num = PAL_RESERV_INT;
            return;
        }
        if(tk.token.equals(palReserv.getpFloat())){
            tk.classe = "PR - float";
            tk.num = PAL_RESERV_FLOAT;
            return;
        }
        if(tk.token.equals(palReserv.getpChar())){
            tk.classe = "PR - char";
            tk.num = PAL_RESERV_CHAR;
            return;
        }

        tk.classe = "Identificador";
        tk.num = ID;
    }

    private void checkComentError(BufferedReader arquivo) throws IOException{

        while(tk.num == COMENTARIO) {
            tk = scanner(arquivo);
        }

//        if(tk.num == EOF){
//           System.out.println("Fim de Arquivo!");
//           /*for(int i = 0; i < tbSimbArray.size();i++ ) {
//                System.out.println(tbSimbArray.get(i).tipo +" "+tbSimbArray.get(i).id+" "+tbSimbArray.get(i).escopo);
//           }*/
//           arquivo.close();
//           System.exit(0);
//        }
        
        if(tk.num == PAL_RESERV_FOR){
           System.out.println("\nL/C: "+linhas+"/"+colunas+"\nErro: Operações com a palavra reservada 'for' não estão disponiveis nesta versão do aplicativo.");
           arquivo.close();
           System.exit(0);
        }
        
        if(tk.num == MENSAGEM_ERRO){
           System.out.println("\nL/C: "+linhas+"/"+colunas+" "+tk.msgErro);
           arquivo.close();
           System.exit(0);
        }

    }
    

    public void simboloInicial(BufferedReader arquivo) throws FileNotFoundException, IOException{
        tk = scanner(arquivo);
        checkComentError(arquivo);
        System.out.println("\n-> "+tk.token);
        programa(arquivo);
        
    }

    private void programa(BufferedReader arquivo) throws IOException{

        if(tk.num == PAL_RESERV_INT){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Palavra 'int' era esperada.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }

        if(tk.num == PAL_RESERV_MAIN){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Palavra 'main' era esperada.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }

        if(tk.num == ESP_ABRE_PARENT){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '(' era esperado.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }

        if(tk.num == ESP_FECHA_PARENT){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ')' era esperado.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }

        //System.out.println("\nPassou doido, chama bloco()!!");

        bloco(arquivo);
        //System.out.println("Olaaa");
        if(tk.num != EOF){
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Há tokens fora do bloco principal.");
           System.out.println("Ultimo token lido -> "+tk.token);
           arquivo.close();
           System.exit(0);
        }
        
//        for(int i = 0; i < tbSimbArray.size();i++ ) {
//            System.out.println(tbSimbArray.get(i).tipo+" "+tbSimbArray.get(i).id+" "+tbSimbArray.get(i).escopo);
//        }

    }

    private void bloco(BufferedReader arquivo) throws IOException{

        if(tk.num == ESP_ABRE_CHAVES){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '{' era esperado.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }
        //tb.escopo++;
        while(true){
            if(tk.num == PAL_RESERV_INT || tk.num == PAL_RESERV_FLOAT || tk.num == PAL_RESERV_CHAR){
          //     tb.tipo = tk.num;
               declaracoes(arquivo);
            }
            else{
               break;
            }
        }

        while(true){
            if(tk.num == PAL_RESERV_IF || tk.num == ID ||tk.num == ESP_ABRE_CHAVES
                 ||tk.num == PAL_RESERV_DO||tk.num == PAL_RESERV_WHILE){
               comando(arquivo);
            }
            else{
               break;
            }
        }

        if(tk.num == ESP_FECHA_CHAVES){
        //   tb.escopo--;
            
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '}' era esperado.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }

    }

    private void declaracoes(BufferedReader arquivo) throws IOException{

        if(tk.num == PAL_RESERV_INT || tk.num == PAL_RESERV_FLOAT || tk.num == PAL_RESERV_CHAR){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }

        if(tk.num == ID){
          // tb.id = tk.token;
//           for(int i = 0;i < tbSimbArray.size();i++) {
//                if(tb.id.equals(tbSimbArray.get(i).id)&& tb.escopo == tbSimbArray.get(i).escopo){
//                   System.out.println("O identificador '"+tb.id+"' já foi declarado neste escopo!");
//                   System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um identificador era esperado.'");
//                   System.out.println("Ultimo token lido '"+tk.token+"'");
//                   arquivo.close();
//                   System.exit(0);
//                }
//            }
//           tbAux = new TabelaSimbolos();
//           tbAux.id = tb.id;
//           tbAux.tipo = tb.tipo;
//           tbAux.escopo = tb.escopo;
//           tbSimbArray.push(tbAux);
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
           System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um identificador era esperado.");
           System.out.println("Ultimo token lido '"+tk.token+"'");
           arquivo.close();
           System.exit(0);
        }

        while(tk.num != ESP_PONTO_VIRGULA){
             if(tk.num == ESP_VIRGULA){
                tk = scanner(arquivo);
                checkComentError(arquivo);
                System.out.println("\n-> "+tk.token);
                 if(tk.num == ID){
//                    tb.id = tk.token;
//                    for(int i = 0;i < tbSimbArray.size();i++) {
//                         if(tb.id.equals(tbSimbArray.get(i).id)&& tb.escopo == tbSimbArray.get(i).escopo){
//                             System.out.println("O identificador '"+tb.id+"' já foi declarado neste escopo!");
//                             System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um identificador era esperado.'");
//                             System.out.println("Ultimo token lido -> "+tk.token);
//                             arquivo.close();
//                             System.exit(0);
//                         }
//                    }
//                    tbAux = new TabelaSimbolos();
//                    tbAux.id = tb.id;
//                    tbAux.tipo = tb.tipo;
//                    tbAux.escopo = tb.escopo;
//                    tbSimbArray.push(tbAux); 
                    tk = scanner(arquivo);
                    checkComentError(arquivo);
                    System.out.println("\n-> "+tk.token);
                 }
                 else{
                    System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um identificador era esperado.");
                    System.out.println("Ultimo token lido '"+tk.token+"'");
                    arquivo.close();
                    System.exit(0);
                 }
             }else{
                    System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um identificador ou ';' era esperado.");
                    System.out.println("Ultimo token lido '"+tk.token+"'");
                    arquivo.close();
                    System.exit(0);
             }
        }

        if(tk.num == ESP_PONTO_VIRGULA){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
          System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ';' era esperado.");
          System.out.println("Ultimo token lido '"+tk.token+"'");
          arquivo.close();
          System.exit(0);
        }
    }

    private void comando(BufferedReader arquivo) throws IOException{
        
        if(tk.num == PAL_RESERV_IF){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           if(tk.num == ESP_ABRE_PARENT){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
           }
           else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '(' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token);
              arquivo.close();
              System.exit(0);
           }

           exprRelacional(arquivo);

           if(tk.num == ESP_FECHA_PARENT){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
           }
           else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ')' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
           }

           comando(arquivo);

           if(tk.num == PAL_RESERV_ELSE){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
              comando(arquivo);
              return;
           }
           return;
        }

        if(tk.num == ID||tk.num == ESP_ABRE_CHAVES){
           comandoBasico(arquivo);
           return;
        }

        if(tk.num == PAL_RESERV_DO||tk.num == PAL_RESERV_WHILE){
           iteracao(arquivo);
           return;
        }
        else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um comando era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
        }
        
    }
    

    private void exprRelacional(BufferedReader arquivo) throws IOException{

        exprAritmetica(arquivo);

        if(tk.num == OP_REL_DIF||tk.num == OP_REL_IGUAL||tk.num == OP_REL_MAIOR
            ||tk.num == OP_REL_MAIOR_IGUAL||tk.num == OP_REL_MENOR||tk.num == OP_REL_MENOR
                ||tk.num == OP_REL_MENOR_IGUAL){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
          System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um Op.Relacional era esperado.");
          System.out.println("Ultimo token lido '"+tk.token+"'");
          arquivo.close();
          System.exit(0);
        }

        exprAritmetica(arquivo);

    }

    private void exprAritmetica(BufferedReader arquivo) throws IOException{
        termo(arquivo);
        exprArit(arquivo);
    }

    private void termo(BufferedReader arquivo) throws IOException {
        fator(arquivo);
        termoL(arquivo);
    }

    private void fator(BufferedReader arquivo) throws IOException {

        if(tk.num == ESP_ABRE_PARENT){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           exprAritmetica(arquivo);
           if(tk.num == ESP_FECHA_PARENT){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
              return;
           }
           else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ')' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
           }

        }

        if(tk.num == ID||tk.num == CONST_INT
            ||tk.num == CONST_FLOAT||tk.num == CONST_CHAR){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
        }
        else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um identificador ou constante era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
        }
    }

    private void exprArit(BufferedReader arquivo) throws IOException {

        if(tk.num == OP_ARIT_SOMA){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           termo(arquivo);
           exprArit(arquivo);
        }
        else if(tk.num == OP_ARIT_SUB){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           termo(arquivo);
           exprArit(arquivo);
        }
        else{
           return;
        }

    }

    private void termoL(BufferedReader arquivo) throws IOException {
        if(tk.num == OP_ARIT_MULT){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           fator(arquivo);
           termoL(arquivo);
        }
        else if(tk.num == OP_ARIT_DIV){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           fator(arquivo);
           termoL(arquivo);
        }
        else{
           return;
        }
    }

    private void atribuicao(BufferedReader arquivo) throws IOException {
      if(tk.num == ID){
         tk = scanner(arquivo);
         checkComentError(arquivo);
         System.out.println("\n-> "+tk.token);
         if(tk.num == OP_ARIT_ATRIBUICAO){
            tk = scanner(arquivo);
            checkComentError(arquivo);
            System.out.println("\n-> "+tk.token);
            exprAritmetica(arquivo);
         }
         else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '=' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
         }

         if(tk.num == ESP_PONTO_VIRGULA){
            tk = scanner(arquivo);
            checkComentError(arquivo);
            System.out.println("\n-> "+tk.token);
         }
         else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ';' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
         }
      }

    }

    private void comandoBasico(BufferedReader arquivo) throws IOException {

      if(tk.num == ID){
         atribuicao(arquivo);
         return;
      }

      if(tk.num == ESP_ABRE_CHAVES){
         bloco(arquivo);
         return;
      }
    }

    private void iteracao(BufferedReader arquivo) throws IOException {

        if(tk.num == PAL_RESERV_DO){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           comando(arquivo);
           if(tk.num == PAL_RESERV_WHILE){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
              if(tk.num == ESP_ABRE_PARENT){
                 tk = scanner(arquivo);
                 checkComentError(arquivo);
                 System.out.println("\n-> "+tk.token);
              }
              else{
                 System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '(' era esperado.");
                 System.out.println("Ultimo token lido '"+tk.token+"'");
                 arquivo.close();
                 System.exit(0);
              }

              exprRelacional(arquivo);

              if(tk.num == ESP_FECHA_PARENT){
                 tk = scanner(arquivo);
                 checkComentError(arquivo);
                 System.out.println("\n-> "+tk.token);
              }
              else{
                 System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ')' era esperado.");
                 System.out.println("Ultimo token lido '"+tk.token+"'");
                 arquivo.close();
                 System.exit(0);
              }

              if(tk.num == ESP_PONTO_VIRGULA){
                 tk = scanner(arquivo);
                 checkComentError(arquivo);
                 System.out.println("\n-> "+tk.token);
                 return;
                 
              }
              else{
                 System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ';' era esperado.");
                 System.out.println("Ultimo token lido '"+tk.token+"'");
                 arquivo.close();
                 System.exit(0);
              }

           }
           else{
                 System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Palavra 'while' era esperada para contrução da estrutura DO-WHILE.");
                 System.out.println("Ultimo token lido '"+tk.token+"'");
                 arquivo.close();
                 System.exit(0);
              }

        }//Iteracao DO WHILE

        if(tk.num == PAL_RESERV_WHILE){
           tk = scanner(arquivo);
           checkComentError(arquivo);
           System.out.println("\n-> "+tk.token);
           if(tk.num == ESP_ABRE_PARENT){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
           }
           else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um '(' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
           }

           exprRelacional(arquivo);

           if(tk.num == ESP_FECHA_PARENT){
              tk = scanner(arquivo);
              checkComentError(arquivo);
              System.out.println("\n-> "+tk.token);
           }
           else{
              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ')' era esperado.");
              System.out.println("Ultimo token lido '"+tk.token+"'");
              arquivo.close();
              System.exit(0);
           }
           
           comando(arquivo);
           
//           if(tk.num == ESP_PONTO_VIRGULA){
//              tk = scanner(arquivo);
//              checkComentError(arquivo);
//              System.out.println("\n-> "+tk.token);
//           }
//           else{
//              System.out.println("\nL/C: "+linhas+"/"+colunas+" Erro: Um ';' era esperado.'");
//              System.out.println("Ultimo token lido '"+tk.token+"'");
//              arquivo.close();
//              System.exit(0);
//           }

        }// Iteracao WHILE
    }
}