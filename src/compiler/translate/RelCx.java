package compiler.translate;

import compiler.temp.Label;

public class RelCx extends Cx {
    int op;
    compiler.tree.Exp left, right;

    RelCx(int o, compiler.tree.Exp l, compiler.tree.Exp r) {
		op = o;
		left = l;
		right = r;
    }

    compiler.tree.Stm unCx(Label t, Label f) {
    	return new compiler.tree.CJUMP(op, left, right, t, f);
    }
}
