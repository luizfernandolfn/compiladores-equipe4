# Árvore de Sintaxe Abstrata #

Devido às próprias funcionalidades nativas do JavaCC, não foi necessário programar o código para gerar a Árvore de Sintaxe Abstrata correspondente ao código-fonte sendo compilado.

Foi utilizado algumas classes (syntaxtree, visitor) do framework (http://www.cambridge.org/resources/052182060X/)

A partir dessa definição, o próprio JavaCC se encarrega de gerar o código citado e as respectivas classes representativas dos nós da árvore. Com isso, através de comparações instanceof do próprio Java, podemos determinar com qual das regras da AST estamos lidando.

Como o código gerado é automático, nenhum teste foi realizado, bastando verificar a corretude das regras de transformação aplicadas.

### Divisão de tarefas ###

Todos implementaram algumas funções e métodos relacionados a esse módulo do compilador.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Geração da árvore abstrata

### Dificuldades Encontradas ###

Não houve maiores dificuldades na implementação deste módulo.

### Testes ###

Completados com sucesso.