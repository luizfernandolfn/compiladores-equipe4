package compiler.syntaxtree;

import compiler.visitor.TypeVisitor;
import compiler.visitor.Visitor;

public abstract class Exp {
	public abstract void accept(Visitor v);

	public abstract Type accept(TypeVisitor v);
}
