package compiler.regalloc;

public class MoveList {
	
   public compiler.graph.Node src, dst;
   public MoveList tail;
   
   public MoveList(compiler.graph.Node s, compiler.graph.Node d, MoveList t) {
		src=s; 
		dst=d; 
		tail=t;
   }
}
