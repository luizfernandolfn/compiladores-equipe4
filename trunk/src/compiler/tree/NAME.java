package compiler.tree;

import compiler.temp.Label;

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
}
