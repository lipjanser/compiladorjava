# projetocompiladorjava
Compilador construido na linguagem Java baseada na gramática definida abaixo para reconhecer alguns padrões da linguagem C. 
Eu construi esse programa durante a minha graduação então talvez contenha erros, mas espero que possa servir de ajuda para aqueles que estão estudando a matéria de compiladores.

Abraço!

# Gramática do Compilador

-programa- ::= int main"("")" -bloco- <br/>
-bloco- ::= "{" {-decl_var-}* {-comando-}* "}" <br/>
-decl_var- ::= -tipo- -id- {,-id-}* ";" <br/>
-tipo- ::= int | float | char <br/>
-comando- ::= -comando_básico- | -iteração- | if "("-expr_relacional-")" -comando- {else -comando-}? <br/>
-comando_básico- ::= -atribuição- | -bloco- <br/>
-iteração- ::= while "("-expr_relacional-")" -comando- | do -comando- while "("-expr_relacional-")"";" <br/>
-atribuição- ::= -id- "=" -expr_arit- ";" <br/>
-expr_relacional- ::= -expr_arit- -op_relacional- -expr_arit- <br/>
-expr_arit- ::= -termo- -expr_arit'- <br/>
-expr_arit'- ::= "+" -termo- -expr_arit'- <br/>
-expr_arit'- ::= "-" -termo- -expr_arit'- <br/>
-expr_arit'- ::= VAZIO <br/>
-termo- ::= -fator- -termo'- <br/>
-termo'- ::= "*" -fator- -termo'- <br/>
-termo'- ::= “/” -fator- -termo'- <br/>
-termo'- ::= VAZIO <br/>
-fator- ::= “(“ -expr_arit- “)” <br/>
-fator- ::= -id- <br/>
-fator- ::= -real- <br/>
-fator- ::= -inteiro- <br/>
-fator- ::= -char- <br/>


