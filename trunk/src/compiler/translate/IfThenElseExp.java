package Translate;

import Temp.Temp;
import Temp.Label;

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

    private static Tree.Stm SEQ(Tree.Stm left, Tree.Stm right) {
	if (left == null)
	    return right;
	if (right == null)
	    return left;
	return new Tree.SEQ(left, right);
    }
    private static Tree.LABEL LABEL(Label l) { return new Tree.LABEL(l); }
    private static Tree.Exp ESEQ(Tree.Stm stm, Tree.Exp exp) {
	if (stm == null) return exp;
	return new Tree.ESEQ(stm, exp);
    }
    private static Tree.Stm MOVE(Tree.Exp dst, Tree.Exp src) {
	return new Tree.MOVE(dst, src);
    }
    private static Tree.Stm JUMP(Label l) { return new Tree.JUMP(l); }
    private static Tree.Exp TEMP(Temp t)   { return new Tree.TEMP(t); }

    Tree.Stm unCx(Label tt, Label ff) {
        Tree.Stm aStm = a.unCx(tt, ff);
        if (aStm instanceof Tree.JUMP) {
	    Tree.JUMP aJump = (Tree.JUMP)aStm;
	    if (aJump.exp instanceof Tree.NAME) {
		Tree.NAME aName = (Tree.NAME)aJump.exp;
		aStm = null;
		t = aName.label;
	    }
	}
        Tree.Stm bStm = b.unCx(tt, ff);
        if (bStm instanceof Tree.JUMP) {
	    Tree.JUMP bJump = (Tree.JUMP)bStm;
	    if (bJump.exp instanceof Tree.NAME) {
		Tree.NAME bName = (Tree.NAME)bJump.exp;
		bStm = null;
		f = bName.label;
	    }
	}

        Tree.Stm condStm = cond.unCx(t, f);

        if (aStm == null && bStm == null)
	    return condStm;
	if (aStm == null)
	    return SEQ(condStm, SEQ(LABEL(f), bStm));
	if (bStm == null)
	    return SEQ(condStm, SEQ(LABEL(t), aStm));
	return SEQ(condStm,
		   SEQ(SEQ(LABEL(t), aStm),
		       SEQ(LABEL(f), bStm)));
    }

    Tree.Exp unEx() {
        Tree.Exp aExp = a.unEx();
        if (aExp == null)
	    return null;
	Tree.Exp bExp = b.unEx();
	if (bExp == null)
	    return null;
	Temp r = new Temp();
	return ESEQ(SEQ(SEQ(cond.unCx(t, f),
			    SEQ(SEQ(LABEL(t),
				    SEQ(MOVE(TEMP(r), aExp),
					JUMP(join))),
				SEQ(LABEL(f),
				    SEQ(MOVE(TEMP(r), bExp),
					JUMP(join))))),
			LABEL(join)),
		    TEMP(r));
    }

    Tree.Stm unNx() {
        Tree.Stm aStm = a.unNx();
        if (aStm == null)
	    t = join;
	else
	    aStm = SEQ(SEQ(LABEL(t), aStm), JUMP(join));

	Tree.Stm bStm = b.unNx();
	if (bStm == null)
	    f = join;
	else
	    bStm = SEQ(SEQ(LABEL(f), bStm), JUMP(join));

	if (aStm == null && bStm == null)
	    return cond.unNx();

	Tree.Stm condStm = cond.unCx(t, f);

	if (aStm == null)
	    return SEQ(SEQ(condStm, bStm), LABEL(join));

	if (bStm == null)
	    return SEQ(SEQ(condStm, aStm), LABEL(join));

	return SEQ(SEQ(condStm, SEQ(aStm, bStm)), LABEL(join));
    }
}










