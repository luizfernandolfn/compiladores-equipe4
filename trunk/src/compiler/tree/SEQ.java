package compiler.tree;

public class SEQ extends Stm {
	public Stm left, right;

	public SEQ(Stm l, Stm r) {
		
		super();
        
        if ( l == null )
            throw new Error("l");
        
        if ( r == null )
            throw new Error("r");
        
		left = l;
		right = r;
	}

	public ExpList kids() {
		throw new Error("kids() not applicable to SEQ");
	}

	public Stm build(ExpList kids) {
		throw new Error("build() not applicable to SEQ");
	}
}
