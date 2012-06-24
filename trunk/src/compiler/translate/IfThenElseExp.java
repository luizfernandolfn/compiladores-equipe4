package compiler.translate;

import compiler.temp.Temp;
import compiler.temp.Label;
import compiler.tree.*;

public class IfThenElseExp extends Exp {
	Exp cond, a, b;
	Label t = new Label();
	Label f = new Label();
	Label join = new Label();

	IfThenElseExp(Exp cc, Exp aa, Exp bb) {
		cond = cc;
		a = aa;
		b = bb;
	}

	private static compiler.tree.Stm SEQ(compiler.tree.Stm left, compiler.tree.Stm right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		return new compiler.tree.SEQ(left, right);
	}

	private static compiler.tree.LABEL LABEL(Label l) {
		return new compiler.tree.LABEL(l);
	}

	private static compiler.tree.Exp ESEQ(compiler.tree.Stm stm, compiler.tree.Exp exp) {
		if (stm == null)
			return exp;
		return new compiler.tree.ESEQ(stm, exp);
	}

	private static compiler.tree.Stm MOVE(compiler.tree.Exp dst,
			compiler.tree.Exp src) {
		return new compiler.tree.MOVE(dst, src);
	}

	private static compiler.tree.Stm JUMP(Label l) {
		return new compiler.tree.JUMP(l);
	}

	private static compiler.tree.Exp TEMP(Temp t) {
		return new compiler.tree.TEMP(t);
	}

	public compiler.tree.Stm unCx(Label tt, Label ff) {
		compiler.tree.Stm aStm = a.unCx(tt, ff);
		if (aStm instanceof compiler.tree.JUMP) {
			compiler.tree.JUMP aJump = (compiler.tree.JUMP) aStm;
			if (aJump.exp instanceof compiler.tree.NAME) {
				compiler.tree.NAME aName = (compiler.tree.NAME) aJump.exp;
				aStm = null;
				t = aName.label;
			}
		}
		compiler.tree.Stm bStm = b.unCx(tt, ff);
		if (bStm instanceof compiler.tree.JUMP) {
			compiler.tree.JUMP bJump = (compiler.tree.JUMP) bStm;
			if (bJump.exp instanceof compiler.tree.NAME) {
				compiler.tree.NAME bName = (compiler.tree.NAME) bJump.exp;
				bStm = null;
				f = bName.label;
			}
		}

		compiler.tree.Stm condStm = cond.unCx(t, f);

		if (aStm == null && bStm == null)
			return condStm;
		if (aStm == null)
			return SEQ(condStm, SEQ(LABEL(f), bStm));
		if (bStm == null)
			return SEQ(condStm, SEQ(LABEL(t), aStm));
		return SEQ(condStm, SEQ(SEQ(LABEL(t), aStm), SEQ(LABEL(f), bStm)));
	}

	public compiler.tree.Exp unEx() {
		compiler.tree.Exp aExp = a.unEx();
		if (aExp == null)
			return null;
		compiler.tree.Exp bExp = b.unEx();
		if (bExp == null)
			return null;
		Temp r = new Temp();
		return ESEQ(
				SEQ(SEQ(cond.unCx(t, f),
						SEQ(SEQ(LABEL(t), SEQ(MOVE(TEMP(r), aExp), JUMP(join))),
								SEQ(LABEL(f),
										SEQ(MOVE(TEMP(r), bExp), JUMP(join))))),
										LABEL(join)), TEMP(r));
	}

	public compiler.tree.Stm unNx() {
		compiler.tree.Stm aStm = a.unNx();
		if (aStm == null)
			t = join;
		else
			aStm = SEQ(SEQ(LABEL(t), aStm), JUMP(join));

		compiler.tree.Stm bStm = b.unNx();
		if (bStm == null)
			f = join;
		else
			bStm = SEQ(SEQ(LABEL(f), bStm), JUMP(join));

		if (aStm == null && bStm == null)
			return cond.unNx();

		compiler.tree.Stm condStm = cond.unCx(t, f);

		if (aStm == null)
			return SEQ(SEQ(condStm, bStm), LABEL(join));

		if (bStm == null)
			return SEQ(SEQ(condStm, aStm), LABEL(join));

		return SEQ(SEQ(condStm, SEQ(aStm, bStm)), LABEL(join));
	}
}
