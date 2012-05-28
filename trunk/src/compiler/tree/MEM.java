package compiler.tree;

import compiler.temp.Temp;

public class MEM extends Exp {
	public Exp exp;

	public MEM(Exp e) {
		exp = e;
	}

	public ExpList kids() {
		return new ExpList(exp, null);
	}

	public Exp build(ExpList kids) {
		return new MEM(kids.head);
	}
	
	public void accept(IntVisitor v, int d) { 
		v.visit(this, d); 
	}
	
	public Temp accept(CodeVisitor v) { 
		return v.visit(this); 
	}
}
