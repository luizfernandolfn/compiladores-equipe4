# Representação Intermediária #

Após a transformação do código fonte em uma árvore sintática abstrata e da execução de ações semânticas para cada nó, o compilador gera uma nova árvore que representa instruções abstratas simples de código de máquina. Essa árvore é construída utilizando a classe IRVisitor, que, da mesma forma que os outros visitors implementados, percorre a AST executando ações correspondentes a cada nó.

Posteriormente, A IRTree será utilizada em conjunto com as classes Frame e Temp para gerar o código alvo para um processador específico. Porém nessa fase ainda não estamos preocupados com detalhes de processador, mas sim em um conjunto de instruções primitivas que possam ser compatíveis com qualquer arquitetura. As classes Frame e Temp também abstraem dos detalhes do frame da arquitetura e da quantidade de registradores da arquitetura alvo.

### Divisão de tarefas ###

Essa fase foi dividida da seguinte forma: Rômulo e Ricardo ficaram responsáveis pela implementação do código; Vanessa e Henrique ficaram responsáveis pela Documentação.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Geração da árvore IR para a árvore abstrata dada como entrada e os fragmentos do código

### Dificuldades Encontradas ###

A maior dificuldade encontrada nessa etapa foi construir a IRTree gerando os fragmentos utilizando o _Frame_ e entender como mostrar o resultado dessa fase.

### Testes ###

Completados com sucesso.