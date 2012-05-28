package compiler.tree;

public interface Hospitable {
    public     void accept(IntVisitor v, int d);
//    public     Tree accept(ResultVisitor v);
}
