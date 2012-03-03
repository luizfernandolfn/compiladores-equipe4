package interpretador;

//Atribuição
//atribui a uma variável o resultado de uma expressão
public class AssignStm extends Stm {
	public String i;
	public Exp exp;
	public AssignStm( String b, Exp e ) {
		i = b;
		exp = e;
	}
}
