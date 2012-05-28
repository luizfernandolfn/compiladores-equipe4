package compiler.tree;

public abstract class Exp {
	
	public Exp(){
		super();
	}
	
	abstract public ExpList kids();

	abstract public Exp build(ExpList kids);
	
	abstract public void accept(IntVisitor v, int d);
	
	abstract public compiler.temp.Temp accept(CodeVisitor v);
}
