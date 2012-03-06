package compiler.syntaxtree;

import compiler.visitor.TypeVisitor;
import compiler.visitor.Visitor;

public class IntArrayType extends Type {
	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}
}