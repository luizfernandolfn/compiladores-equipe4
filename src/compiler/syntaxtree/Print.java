package compiler.syntaxtree;

import compiler.visitor.TypeVisitor;
import compiler.visitor.Visitor;

public class Print extends Statement {
	public Exp e;

	public Print(Exp ae) {
		e = ae;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
}
