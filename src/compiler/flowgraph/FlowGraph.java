package compiler.flowgraph;

/**
 * A control flow graph is a directed graph in which each edge
 * indicates a possible flow of control.  Also, each node in
 * the graph defines a set of temporaries; each node uses a set of
 * temporaries; and each node is, or is not, a <strong>move</strong>
 * instruction.
 *
 * @see AssemFlowGraph
 */

public abstract class FlowGraph extends compiler.graph.Graph {
 /**
  * The set of temporaries defined by this instruction or block 
  */
	public abstract compiler.temp.Temp[] def(compiler.graph.Node node);

 /**
  * The set of temporaries used by this instruction or block 
  */
	public abstract compiler.temp.Temp[] use(compiler.graph.Node node);

 /**
  * True if this node represents a <strong>move</strong> instruction,
  * i.e. one that can be deleted if def=use. 
  */
	public abstract boolean isMove(compiler.graph.Node node);

 /**
  * Print a human-readable dump for debugging.
  */
     public void show(java.io.PrintStream out) {
	for (compiler.graph.NodeList p=nodes(); p!=null; p=p.tail) {
	  compiler.graph.Node n = p.head;
	  out.print(n.toString());
	  out.print(": ");
	  compiler.temp.Temp[] q=def(n);
	  for(int i=0; q != null && i<q.length; i++) {
	     out.print(q[i].toString());
	     out.print(" ");
	  }
	  out.print(isMove(n) ? "<= " : "<- ");
	  q=use(n);
	  for(int i=0; q != null && i<q.length; i++) {
	     out.print(q[i].toString());
	     out.print(" ");
	  }
	  out.print("; goto ");
	  for(compiler.graph.NodeList nl=n.succ(); nl!=null; nl=nl.tail) {
	     out.print(nl.head.toString());
	     out.print(" ");
	  }
	  out.println();
	}
     }

}
    
    