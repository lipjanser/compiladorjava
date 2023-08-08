# projetocompiladorjava
Compilador construido na linguagem Java baseada na gramática definida abaixo para reconhecer alguns padrões da linguagem C. 
Eu construi esse programa durante a minha graduação então talvez contenha erros, mas espero que possa servir de ajuda para aqueles que estão estudando a matéria de compiladores.

Abraço!

# Gramática do Compilador

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

