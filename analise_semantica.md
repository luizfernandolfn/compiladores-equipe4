# Análise Semântica #


A principal função da análise semântica é criar, a partir do texto fonte, uma interpretação deste texto fonte, expressa em alguma notação adequada - geralmente uma linguagem intermediária do compilador.

Um importante componente da análise semântica é a verificação de tipos. Nela o compilador checa se cada operando recebe os operandos que são permitidos na especificação da linguagem fonte. Por exemplo, muitas definições nas linguagens de programação requerem que o compilador relate um erro a cada vez que um número real seja usado para indexar um array.

Através da nossa gramática, para cada derivação, nós temos ações semânticas que nos ajudam a construir a Árvore Sintática Abstrata. Ela representa as regras da gramática. A motivação de utilizarmos essa árvore é facilitar o processo de construção da tabela de símbolos e da checagem de tipos, que fazem parte da Análise Semântica.

Ao percorrer a árvore sintática do programa e verificar se os tipos das expressões estão condizentes com as informações armazenadas na tabela de símbolos, realizamos a “checagem de tipos”. Implementamos tanto a construção da tabela de símbolos quanto a checagem de tipos utilizando o padrão Visitor.

O padrão Visitor foi utilizado por ser um padrão comportamental em que
podemos representar que operação vai ser executada sobre elementos de uma estrutura, no nosso caso a árvore. Assim, podemos definir novas operações sem a necessidade de modificar a classe dos elementos em que opera. As operações necessitam tratar cada tipo da árvore de maneira diferente.

### Divisão de tarefas ###

A parte referente a tabela de símbolos foi implementada pelos alunos Vanessa Karla e Ricardo Bustamante. A parte referente a checagem de tipos foi implementada pelos alunos Rômulo Gadelha e Henrique Araújo.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Geração da árvore abstrata, construção da tabela de tipos e implementação da checagem de tipos do programa de entrada.

### Dificuldades Encontradas ###

Após o perfeito entendimento de como o padrão _Visitor_ funcionava, não houve maiores dificuldades na implementação deste módulo.

### Testes ###

Completados com sucesso.