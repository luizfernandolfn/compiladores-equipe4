package interpretador;

//última expressão da lista
public class LastExpList extends ExpList {
	public Exp head;
	public LastExpList( Exp e ){
		head = e;
	}
}
