# compiladorjava
Compilador construido na linguagem Java para a gramática definida no arquivo "Compilador.java" para reconhecer alguns padrões da linguagem C. 
Eu construi esse programa durante a minha graduação então talvez contenha erros, mas espero que possa servir de ajuda para aqueles que estão estudando a matéria de compiladores.

Abraço!

P.S.: Perdoem pelo nome no projeto! xD


**Gramática
<programa> ::= int main"("")" <bloco>
  
<bloco> ::= “{“ {<decl_var>}* {<comando>}* “}”
  
<decl_var> ::= <tipo> <id> {,<id>}* ";"
  
<tipo> ::= int | float | char
  
<comando> ::= <comando_básico> | <iteração> | if "("<expr_relacional>")" <comando> {else <comando>}?
  
<comando_básico> ::= <atribuição> | <bloco>
  
<iteração> ::= while "("<expr_relacional>")" <comando> | do <comando> while "("<expr_relacional>")"";"
  
<atribuição> ::= <id> "=" <expr_arit> ";"
  
<expr_relacional> ::= <expr_arit> <op_relacional> <expr_arit>
  
<expr_arit> ::= <termo> <expr_arit'>
  
<expr_arit'> ::= "+" <termo> <expr_arit'>
  
<expr_arit'> ::= "-" <termo> <expr_arit'>
  
<expr_arit'> ::= VAZIO
  
<termo> ::= <fator> <termo'>
  
<termo'> ::= "*" <fator> <termo'>
  
<termo'> ::= “/” <fator> <termo'>
  
<termo'> ::= VAZIO
  
<fator> ::= “(“ <expr_arit> “)”
  
<fator> ::= <id>
  
<fator> ::= <real>
  
<fator> ::= <inteiro>
  
<fator> ::= <char>
