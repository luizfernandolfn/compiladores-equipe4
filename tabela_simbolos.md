# Tabela de Símbolos #

Uma tabela de símbolos é uma estrutura de dados contendo um registro para cada identificador, com os campos contendo os atributos do identificador. As informações sobre o identificador são coletadas pelas fases de análise de um compilador e usada pelas fases de síntese de forma a gerar o código-alvo. Durante a análise léxica a cadeia de caracteres que forma um identificador é armazenada em uma entrada da tabela de símbolos. As fases seguintes do compilador acrescentam informações a esta entrada. A fase de geração utiliza as informações armazenadas para gerar o código adequado.

Como trata-se de uma linguagem imperativa (já que é uma restrição da linguagem Java), a tabela de símbolos para a Mini Java foi implementada utilizando-se tabelas de dispersão (hashtables).

As classes referentes à implementação da tabela de símbolos podem ser encontrados nos pacotes: compiler.symboltable, onde são encontradas as classes que representam a estrutura de dados que armazena a tabela de símbolos e o _Visitor_ que, a partir da árvore sintática constrói a tabela; Type Checking , onde é definido o Visitor que faz a checagem de tipos.

### Divisão de tarefas ###

A parte referente a tabela de símbolos foi implementada pelos alunos Vanessa Karla e Ricardo Bustamante.

### Implementação ###

Terminada (100%)

### Funcionalidade Implementadas ###

Construção da tabela de tipos e efetuação da checagem de tipos do programa.

### Dificuldades Encontradas ###

Nessa parte da implementação todos fizeram uma parte, principalmente na parte de verificação de tipos, que foi o que mais tivemos dificuldades para a implementação, pois, primeiro estávamos com dificuldades para entender como construir a tabelas de símbolos e verificar cada tipo.

### Testes ###

Completados com sucesso.