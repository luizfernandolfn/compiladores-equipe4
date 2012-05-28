package compiler.tree;

import compiler.temp.Label;
import compiler.temp.Temp;

public class NAME extends Exp {
	public Label label;

	public NAME(Label l) {
		super();
		label = l;
	}

	public ExpList kids() {
		return null;
	}

	public Exp build(ExpList kids) {
		return this;
	}
	
	public void accept(IntVisitor v, int d) { 
		v.visit(this, d); 
	}
	
	public Temp accept(CodeVisitor v) { 
		return v.visit(this); 
	}
}
