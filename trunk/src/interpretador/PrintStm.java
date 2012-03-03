package interpretador;

//Imprimir
//recebe uma lista de expressões
public class PrintStm extends Stm {
	public ExpList list;
	public PrintStm( ExpList e ){
		list = e;
	}
}
