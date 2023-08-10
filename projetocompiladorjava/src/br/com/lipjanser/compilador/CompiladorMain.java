package br.com.lipjanser.compilador;

import br.com.lipjanser.compilador.classes.Compilador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * GRAMÁTICA <br/>
 * -programa- ::= int main"("")" -bloco- <br/>
 * -bloco- ::= "{" {-decl_var-}* {-comando-}* "}" <br/>
 * -decl_var- ::= -tipo- -id- {,-id-}* ";" <br/>
 * -tipo- ::= int | float | char <br/>
 * -comando- ::= -comando_básico- | -iteração- | if "("-expr_relacional-")" -comando- {else -comando-}? <br/>
 * -comando_básico- ::= -atribuição- | -bloco- <br/>
 * -iteração- ::= while "("-expr_relacional-")" -comando- | do -comando- while "("-expr_relacional-")"";" <br/>
 * -atribuição- ::= -id- "=" -expr_arit- ";" <br/>
 * -expr_relacional- ::= -expr_arit- -op_relacional- -expr_arit- <br/>
 * -expr_arit- ::= -termo- -expr_arit'- <br/>
 * -expr_arit'- ::= "+" -termo- -expr_arit'- <br/>
 * -expr_arit'- ::= "-" -termo- -expr_arit'- <br/>
 * -expr_arit'- ::= VAZIO <br/>
 * -termo- ::= -fator- -termo'- <br/>
 * -termo'- ::= "*" -fator- -termo'- <br/>
 * -termo'- ::= “/” -fator- -termo'- <br/>
 * -termo'- ::= VAZIO <br/>
 * -fator- ::= “(“ -expr_arit- “)” <br/>
 * -fator- ::= -id- <br/>
 * -fator- ::= -real- <br/>
 * -fator- ::= -inteiro- <br/>
 * -fator- ::= -char- <br/>
 *
 * @author lipjanser
 */
public class CompiladorMain {

    public static void main(String[] args) throws IOException {

        if(args[0] != null) {
            if(!args[0].isEmpty()) {
                BufferedReader arquivo = new BufferedReader(new FileReader(args[0]));
                Compilador comp = new Compilador();

                comp.simboloInicial(arquivo);

                arquivo.close();
                System.out.println("Encerrado com sucesso.");
                System.exit(0);
            }
        }
        System.out.println("Arquivo não informado.");
        System.exit(0);

    }

}