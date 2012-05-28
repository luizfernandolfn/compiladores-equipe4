package compiler.translate;

public class ProcFrag extends Frag {
    
	public compiler.tree.Stm body;
    
    public compiler.frame.Frame frame;
    
    public ProcFrag(compiler.tree.Stm b, compiler.frame.Frame f) {
		body = b;
		frame = f;
    }
}
