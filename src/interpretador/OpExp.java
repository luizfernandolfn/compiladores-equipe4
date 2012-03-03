package interpretador;

//Operação
//realiza uma operação
public class OpExp extends Exp {
	public Exp left, right;
	public int operador;
	final public static int plus=1, minus=2, times=3, div=4;
	public OpExp( Exp esq, Exp dir, int op ){
		left = esq;
		right = dir;
		operador = op;		
	}
}
