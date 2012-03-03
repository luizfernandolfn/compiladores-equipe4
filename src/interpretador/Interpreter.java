package interpretador;

/**
 * Trabalho de Compiladores
 * 
 * Equipe:							- Matricula
 * 	Henrique Siqueira Araújo 		- XXXXXXX
 * 	Ricardo Bustamante de Queiroz 	- 0301383
 * 	Romulo Gadelha Lima 			- XXXXXXX
 * 	Vanessa Karla da Silva Oliveira - 0315257
 */

// Classe do interpretador. 
// A partir de uma árvore de Statement, interpreta seu significado.

public class Interpreter {
	
	public static void main(String args[]){
		
		/*Stm prog = new AssignStm("a",new OpExp(new NumExp(10), OpExp.plus, new NumExp(20)));//*/
		//Stm prog = new CompoundStm(new AssignStm( "a", new OpExp( new NumExp(2), new NumExp(3), OpExp.plus ) ), new AssignStm("b", new OpExp( new IdExp("a"), new IdExp("a"), OpExp.times)));
		//Stm prog = new CompoundStm(new AssignStm("a", new NumExp(2)), new AssignStm("a", new OpExp(new IdExp("a"), new NumExp(10), OpExp.times)));
		Stm prog = new CompoundStm(new AssignStm("a",
										new OpExp(new NumExp(5), OpExp.plus, new NumExp(3))),
				   new CompoundStm(new AssignStm("b",
						   new EseqExp(new PrintStm(new PairExpList(new IdExp("a"),
								   new LastExpList(new OpExp(new IdExp("a"),
										   		OpExp.minus, new NumExp(1))))),
						new OpExp(new NumExp(10), OpExp.times,
								new IdExp("a")))),
					new PrintStm(new LastExpList(new IdExp("b")))));//*/
		
		System.out.println("Program Output:");
		Interpreter i = new Interpreter();
		Table t = i.interpStm(prog, null);
		
		System.out.println("Program code:");
		i.printTree(prog);
		System.out.println("\nProgram Environment Table:");
		i.printTable(t);
	}
	
	public void printTable (Table t){
		if (t != null)
		{
			System.out.println(t.id + " -> " + t.val);
			printTable(t.tail);
		}
	}
	
	// Função de teste. Imprime a arvore na forma de um programa de uma linha só.
	public void printTree (Stm s){
		if( s instanceof CompoundStm ){
			printTree (((CompoundStm) s).stm1);
			System.out.print(" ; ");
			printTree (((CompoundStm) s).stm2);
		}
		else if( s instanceof AssignStm ){
			System.out.print(((AssignStm) s).i + " := ");
			printTree(((AssignStm) s).exp);
		}
		else if( s instanceof PrintStm ){
			System.out.print("Print(");
			printTree(((PrintStm) s).list);
			System.out.print(")");
			/*TODO print not working*/
		}
	}
	
	//Função de teste. Imprime a arvore na forma de um programa de uma linha só.
	public void printTree (ExpList l){
		if ( l instanceof PairExpList ){
			printTree(((PairExpList) l).head);
			System.out.print(" , ");
			printTree(((PairExpList) l).list);
		}else if(l instanceof LastExpList ){
			printTree(((LastExpList) l).head);
		}
	}
	
	//Função de teste. Imprime a arvore na forma de um programa de uma linha só.
	public void printTree (Exp e){		
		if( e instanceof IdExp ) {
			System.out.print(((IdExp) e).id);
		}
		
		else if( e instanceof NumExp ){
			System.out.print(((NumExp) e).num);
		}
		
		else if( e instanceof OpExp){
			String c = " ? ";
			
			switch(((OpExp) e).operador){
			case 1:
				// Plus
				c = " + ";
				break;
			case 2:
				// Minus
				c = " - ";
				break;
			case 3:
				// Times
				c = " * ";
				break;
			case 4:
				// Div
				c = " / ";
				break;
			default:
				break;
			}
			
			printTree(((OpExp) e).left);
			System.out.print(c);
			printTree(((OpExp) e).right);
		}
		
		else if( e instanceof EseqExp){
			System.out.print("(");
			printTree(((EseqExp) e).stm);
			System.out.print(",");
			printTree(((EseqExp) e).exp);
			System.out.print(")");
		}		
	}
	
	// Adiciona o elemento novo como cabeça da lista anterior.
	// Inserção feita na cabeça da lista.
	public Table update ( Table t, String i, int v ){
		Table t2 = new Table(i, v, t);
		return t2;
	}
	
	// Procura valor atribuído a um identificador especificado.
	// Caso não encontre um valor para aquele identificador, assume o valor 0.
	public int lookup(Table t, String key){
		// Se chegou no fim da lista, não encontrou o identificador.
		// Vamos assumir que seu valor é zero.
		if(t==null){
			//TODO "erro"
			return 0;
		}
		
		// Se o identificador procurado ocorre na cabeça da lista, retorna seu valor.
		// Caso contrário continua a busca recursivamente.
		if(t.id == key){
			return t.val;
		}else{
			return lookup(t.tail, key);
		}
	}

	public Table printExpList(ExpList l, Table t){
		if(l instanceof LastExpList ){
			IntAndTable iat = interpExp(((LastExpList) l).head,t);
			System.out.print(iat.i);
			return iat.t;
		}else if(l instanceof PairExpList ){
			IntAndTable iat = interpExp(((PairExpList) l).head,t);
			System.out.print(iat.i + ", ");
			return printExpList(((PairExpList) l).list, iat.t);
		}
		return null;
	}
	
	// Recebe uma tabela no estado t1 como entrada
	// Interpreta o Statement "s", e retorna uma tabela t2 após a execução.
	public Table interpStm (Stm s, Table t){
		Table tt = t;
		
		// CompoundStm composto de 2 Stm.
		// Chamada recursiva para os dois, alterando a tabela tt em sequencia.
		if( s instanceof CompoundStm ){
			tt = interpStm(((CompoundStm) s).stm1, tt);
			tt = interpStm(((CompoundStm) s).stm2, tt);
		}
		
		// AssignStm atribui valor numérico da expressão ao identificador especificado.
		// Calcula o valor da expressão com interpExp e atualiza a tabela.
		else if( s instanceof AssignStm ){
			IntAndTable ret = interpExp( ((AssignStm) s).exp, t );
			tt = update(ret.t, ((AssignStm) s).i, ret.i);
		}
		
		// Imprime uma lista de expressões.
		else if( s instanceof PrintStm ){
			System.out.print("[ ");
			Table ttt = printExpList(((PrintStm) s).list,t);
			System.out.print(" ]\n");
			tt = ttt;
		}
		
		// Retorna tabela depois das alterações de estados causados pelo Statement.
		return tt;
	}
	
	public IntAndTable interpExp( Exp e, Table t ){
		IntAndTable res = new IntAndTable(0, t);
		
		// A partir de um identificador, encontrar seu valor na tabela t.
		if( e instanceof IdExp ) {
			res = new IntAndTable(lookup(t, ((IdExp) e).id), t);
		}
		
		// Um número. Seu valor já é o retorno.
		else if( e instanceof NumExp ){
			res = new IntAndTable(((NumExp) e).num, t);
		}
		
		// Operação matemática. *,+,-,/
		// Retorna o valor resultado da expressão.
		else if( e instanceof OpExp){
			// Recebe o resultado da operação.
			int temp;
			// Interpreta a 1º expressão.
			// Altera o estado do programa.
			IntAndTable op1=interpExp(((OpExp) e).left, t);
			// Interpreta a 2º expressão.
			// Recebe a tabela resultante da execução da 1º expressão como entrada.
			IntAndTable op2=interpExp(((OpExp) e).right, op1.t);
			// Analisar qual o operador e executar uma operação diferente para cada um.
			switch(((OpExp) e).operador){
			case 1:
				// Plus
				temp = op1.i + op2.i;
				break;
			case 2:
				// Minus
				temp = op1.i - op2.i;
				break;
			case 3:
				// Times
				temp = op1.i * op2.i;
				break;
			case 4:
				// Div
				temp = op1.i / op2.i;
				break;
			default:
				temp = 0;
				break;
			}
				
			res = new IntAndTable(temp, op2.t);
		}
		
		// Executa um statement antes de calcular a expressão interna.
		// Retorna o valor da expressão interna após o statement.
		else if( e instanceof EseqExp){
			Table tt = interpStm(((EseqExp) e).stm, t);
			res = interpExp(((EseqExp) e).exp, tt);
		}		
		
		return res; //provisório
	}
}