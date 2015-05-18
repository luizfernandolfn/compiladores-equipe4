# Blocos Básicos e Traços #

O resultado da fase de tradução para a representação intermediária são vários fragmentos de códigos associados a cada frame da pilha. Cada fragmento tem uma árvore que corresponde a uma expressão, porém na linguagem alvo não serão avaliadas expressões, alguns aspectos da linguagem IR não correspondem exatamente a realidade das linguagens de máquina.

Nesta fase, o compilador irá reescrever a árvore para que ela possa ser transformada em uma lista de instruções. A classe Canon utiliza o método linearize para esta tarefa, o método recebe como entrada as árvores associadas a cada fragmento e a retorna no formato apropriado para a tradução em código de máquina. A classe Basic Blocks transforma essa lista de instruções em um conjunto de blocos, onde cada bloco é começado por um label e pode terminar com um salto.

### Divisão de tarefas ###

No site do framework do livro, os arquivos para a geração da árvore canônica, dos blocos e dos traços foram disponibilizados.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Canonização dos fragmentos.

### Dificuldades Encontradas ###

Não houve maiores dificuldades na implementação deste módulo.

### Testes ###

Completados com sucesso.