# Alocação de Registradores #

Foi construído o grafo de interferência onde os nós são os temporários utilizados nas instruções e os registradores. Se existir uma aresta entre dois nós, os nós estão vivos ao mesmo tempo na execução do programa e eles não podem ser alocados no mesmo registrador. Obtemos essa informação devido a computação de longevidade executada na etapa anterior.

Essa fase tenta colorir o grafo com k cores, onde k é o número de registradores da máquina, de tal forma que os nós que interferem não possuam a mesma cor. Os nós que não poderem ser coloridos, serão colocados em memória. Outra função dessa fase é eliminar instruções MOVE quando possível, colocando a origem e o destino dessa instrução no mesmo registrador.


### Divisão de tarefas ###

Esta fase não foi completada, visto que os membros da equipe não tinham mais tempo para a finalização do compilador.

### Implementação ###

terminada (30%)

### Funcionalidade Implementadas ###

Nenhuma.

### Dificuldades Encontradas ###

Esta fase não foi completada, visto que os membros da equipe não tinham mais tempo para a finalização do compilador.

### Testes ###

Não foram realizados.