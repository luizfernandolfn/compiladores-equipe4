package chapter1_interpreter;

//par
//recebe uma expressão e uma lista de expressões
public class PairExpList extends ExpList {
	public Exp head;
	public ExpList list;
	public PairExpList( Exp e, ExpList l ){
		head = e;
		list = l;
	}
}
