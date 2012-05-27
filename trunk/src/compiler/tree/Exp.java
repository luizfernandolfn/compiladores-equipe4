package compiler.tree;

public abstract class Exp {
	
	public Exp(){
		super();
	}
	
	abstract public ExpList kids();

	abstract public Exp build(ExpList kids);
}
