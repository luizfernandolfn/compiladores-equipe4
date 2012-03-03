package interpretador;

public class Interpretador {
	public static void main(String args[]){
		System.out.println("Yo mon");
	}
	
	//adiciona o elemento novo como cabeça da lista anterior
	public Table update ( Table t, String i, int v ){
		Table t2 = new Table(i, v, t);
		return t2;
	}

	//recebe uma tabela no estado t1 e retorna uma tabela t2
	public Table interpStm (Stm s, Table t){
		Table tt = t;
		
		//CompoundStm composto de 2 Stm.
		//Chamada recursiva para os dois, alterando as tabelas.
		if( s instanceof CompoundStm ){
			tt = interpStm(((CompoundStm) s).stm1, tt);
			tt = interpStm(((CompoundStm) s).stm2, tt);
		}
		//AssignStm atribuição do valor de uma expressao à um identificador.
		//calcula o valor da expressão com interpExp e atualiza a tabela.
		else if( s instanceof AssignStm ){
			IntAndTable ret = interpExp( ((AssignStm) s).exp, t );
			tt = update(ret.t, ((AssignStm) s).i, ret.i);
		}
		
		else if( s instanceof PrintStm ){
			//fazer depois, imprime statement.
		}
		
		return tt;
	}
	public IntAndTable interpExp( Exp e, Table t ){
		return new IntAndTable(0, null); //provisório
	}
}