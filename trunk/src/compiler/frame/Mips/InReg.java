package compiler.frame.Mips;

import compiler.tree.Exp;
import compiler.tree.TEMP;

public class InReg extends frame.Access
{
    compiler.temp.Temp temp;

    public void print(){
	System.out.println(temp.toString());
    }

    public Exp exp(Exp frameptr)
    {
	return new TEMP(temp);
    }
}
