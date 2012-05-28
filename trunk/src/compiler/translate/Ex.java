package compiler.translate;

import compiler.temp.Label;

public class Ex extends Exp {
    compiler.tree.Exp exp;
    
    Ex(compiler.tree.Exp e) { 
    	exp = e; 
    }
    
    public compiler.tree.Exp unEx() { 
    	return exp; 
    }
    
    public compiler.tree.Stm unNx() { 
    	return new compiler.tree.EXPE(exp); 
    }
    
    public compiler.tree.Stm unCx(Label t, Label f) {
	// if the exp is a constant, emit JUMP statement.
	if (exp instanceof compiler.tree.CONST) {
	    compiler.tree.CONST c = (compiler.tree.CONST)exp;
	    if (c.value == 0)
		return new compiler.tree.JUMP(f);
	    else
		return new compiler.tree.JUMP(t);
	}
	return new compiler.tree.CJUMP(compiler.tree.CJUMP.NE, exp, new compiler.tree.CONST(0), t, f);
    }
}
