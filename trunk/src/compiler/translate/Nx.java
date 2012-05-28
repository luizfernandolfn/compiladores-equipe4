package compiler.translate;

import compiler.temp.Label;

public class Nx extends Exp {
    compiler.tree.Stm stm;
    
    Nx(compiler.tree.Stm s) { stm = s; }

    compiler.tree.Exp unEx() { return null; }

    compiler.tree.Stm unNx() { return stm; }

    compiler.tree.Stm unCx(Label t, Label f) { return null; }
}
