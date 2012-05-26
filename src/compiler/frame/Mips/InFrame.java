package compiler.frame.Mips;

import compiler.tree.BINOP;
import compiler.tree.CONST;
import compiler.tree.Exp;
import compiler.tree.MEM;

class InFrame extends frame.Access
{
    int offset;

    InFrame(int off)
    {
	offset = off;
    }

    public String toString()
    {
	if (offset > 0)
	    return "+"+Integer.toString(offset);
	if (offset == 0)
	    return "";
	return Integer.toString(offset);
	    
    }

    public Exp exp(Exp frameptr)
    {
	return new MEM(new BINOP(BINOP.PLUS, new CONST(offset), frameptr));
    }
}
