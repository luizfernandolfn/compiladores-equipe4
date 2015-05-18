# Análise Sintática #

O Analisador Sintático obtém uma cadeia de tokens proveniente do analisador léxico e verifica se o mesmo pode ser gerado pela gramática da linguagem-fonte.

Espera-se que o analisador sintático relate quaisquer erros de sintaxe e também recupere erros que ocorrem mais comumente, a fim de poder continuar processando o resto de sua entrada.

Modificamos as derivações da gramática disponibilizada (presente no livro texto da disciplina), para que esta não tivesse ambigüidades e fosse utilizado lookahead 1 no parser preditivo. A gramática disponibilizada necessitaria de lookahead maior que 1. Os
tokens gerados foram baseados nas regras dessa gramática.

### Divisão de tarefas ###

Grande parte da implementação foi realizada pelo aluno Ricardo Bustamante. A documentação foi realizada pelos outros três alunos.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Parser da sequência de tokens de entrada e validação do programa de entrada.

### Dificuldades Encontradas ###

Transformar a gramática dada pelo livro na sintaxe do JavaCC. A forma que a gramática de Exp está dificultou a transformação, mas que no final deu certo eliminando a recursão à esquerda nos três casos especiais de Exp.

### Testes ###

Completados com sucesso.