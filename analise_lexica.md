# Análise Léxica #

A tarefa principal do analisador léxico é ler os caracteres da entrada do programa fonte, agrupá-los em lexemas e produzir como saída uma sequência de tokens para cada lexema no programa fonte.

  * Token: é um par consistindo em um nome e um valor de atributo opcional.
  * Padrão: é uma descrição da forma que os lexemas de um token podem assumir
  * Lexema: é uma sequência de caracteres no programa fonte que casa com o padrão para um token e é identificado pelo analisador léxico como uma instância desse token.

Ele pode realizar outras tarefas, como remover comentários e o espaço em branco. Outra tarefa é correlacionar as mensagens de erro geradas pelo compilador com o programa fonte.

Nesta etapa, toda nossa programação concentrou-se no arquivo _Minijavalexer_ onde foi declarado a Gramática e as suas produções correspondentes. Através do JavaCC foram criados os tokens para uso posterior do Analisador Sintático, vide _Minijavaparserconstants_.

### Divisão de tarefas ###

Todos os membros ajudaram na implementação desse módulo.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Geração dos _tokens_ e validação dos _tokens_.

### Dificuldades Encontradas ###

Não houve maiores dificuldades na implementação deste módulo.

### Testes ###

Completados com sucesso.