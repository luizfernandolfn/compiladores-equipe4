package compiler.translate;

public abstract class Exp {
    abstract public compiler.tree.Exp unEx();
    abstract public compiler.tree.Stm unNx();
    abstract public compiler.tree.Stm unCx(compiler.temp.Label t, compiler.temp.Label f);
}
