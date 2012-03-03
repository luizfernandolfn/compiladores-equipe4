package interpretador;

//suporte
//serve para que a função InterpExp possa retornar um inteiro e o estado(table)
public class IntAndTable {
	public int i;
	public Table t;
	public IntAndTable( int ii, Table tt ){
		i = ii;
		t = tt;
	}
}
