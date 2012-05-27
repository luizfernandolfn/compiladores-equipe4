package compiler.tree;

abstract public class Stm {
	
	public Stm(){
		super();
	}
	
	abstract public ExpList kids();

	abstract public Stm build(ExpList kids);
}
