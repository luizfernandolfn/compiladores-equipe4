# Seleção de Instruções #

Nesta etapa foram utilizadas as classes do pacote Assem do framework, que correspondem a instruções mais próximas do nível de máquina, em conjunto com a classe Codegen, que é especfíca de uma arquitetura, que é responsável por percorrer a árvore na linguagem IR e reconhecer padrões (ladrilhos) na árvore para selecionar as instruções relativas a sua arquitetura.

Para testar a emissão de código assembly, foi utilizada a arquitetura proposta no livro, a Jouette Assembly, que contem a maioria das instruções utilizadas em processadores habituais como ADD, MOVE, STORE, etc. O algoritmo utilizado para o reconhecimento de padrões na árvore foi o Maximal Munch, implementado na classe Codegen, que busca por uma solução otimal, ou seja, seleciona o maior ladrilho possível a partir do nó atual.

### Divisão de tarefas ###

Grande parte desse módulo foi implementado pelo aluno Rômulo Gadelha. A documentação foi realizada pelos outros três membros.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Geração da lista de instruções através dos fragmentos do programa.

### Dificuldades Encontradas ###

Não houve maiores dificuldades na implementação deste módulo.

### Testes ###

Não foram realizados.