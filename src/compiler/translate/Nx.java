package compiler.translate;

import compiler.temp.Label;

public class Nx extends Exp {
	compiler.tree.Stm stm;

	Nx(compiler.tree.Stm s) {
		stm = s;
	}

	public compiler.tree.Exp unEx() {
		return null;
	}

	public compiler.tree.Stm unNx() {
		return stm;
	}

	public compiler.tree.Stm unCx(Label t, Label f) {
		return null;
	}
}
