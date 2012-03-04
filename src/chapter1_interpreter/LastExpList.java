package chapter1_interpreter;

//última expressão da lista
public class LastExpList extends ExpList {
	public Exp head;
	public LastExpList( Exp e ){
		head = e;
	}
}
