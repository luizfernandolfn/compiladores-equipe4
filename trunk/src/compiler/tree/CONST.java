package compiler.tree;

import compiler.temp.Temp;

public class CONST extends Exp {
	public int value;

	public CONST(int v) {
		value = v;
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
