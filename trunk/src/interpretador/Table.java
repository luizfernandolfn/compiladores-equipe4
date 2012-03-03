package interpretador;

public class Table {
	public String id;
	public int val;
	public Table tail;
	public Table( String i, int v, Table t ){
		id = i;
		val = v;
		tail = t;
	}
}

//a