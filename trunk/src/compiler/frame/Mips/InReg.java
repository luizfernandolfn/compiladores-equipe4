package compiler.frame.Mips;

import compiler.temp.Temp;
import compiler.tree.Exp;
import compiler.frame.Access;

public class InReg extends Access {
    Temp temp;
    
    InReg(Temp t) {
    	temp = t;
    }

    public Exp exp( Exp fp) {
        return new compiler.tree.TEMP(temp);
    }

    public String toString() {
        return temp.toString();
    }
}

