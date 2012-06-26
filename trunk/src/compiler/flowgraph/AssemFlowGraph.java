package compiler.flowgraph;

import java.util.Iterator;

public class AssemFlowGraph extends FlowGraph {
  java.util.Dictionary ht;

  public compiler.temp.Temp[] def(compiler.graph.Node node) {
    return instr(node).def();
  }
  public compiler.temp.Temp[] use(compiler.graph.Node node) {
    return instr(node).use();
  }
  public boolean isMove(compiler.graph.Node node) {
    return instr(node) instanceof compiler.assem.MOVE;
  }
  public compiler.assem.Instr instr( compiler.graph.Node n ) {
    return (compiler.assem.Instr)ht.get(n);
  }

  public AssemFlowGraph(compiler.assem.InstrList instrs) {
    ht=new java.util.Hashtable();    
    java.util.Dictionary l2n=new java.util.Hashtable();

    // Add all instructions as nodes
    for (compiler.assem.InstrList p=instrs; p!=null; p=p.tail) {
      compiler.graph.Node n=new compiler.graph.Node(this);
      ht.put(n, p.head);
      if (p.head instanceof compiler.assem.LABEL)
	l2n.put(((compiler.assem.LABEL)p.head).label, n);
    }
    // Add edges from each branch instruction node to target node(s)
    for (compiler.graph.NodeList p=nodes(); p!=null; p=p.tail) {
      compiler.graph.Node n=p.head;
      compiler.assem.Targets jumps = (compiler.assem.Targets) instr(n).jumps();
      if (jumps==null && p.tail!=null) {
	addEdge(n, p.tail.head);      // Fall through with edge to next instruction
      }
      else if (jumps!=null) {         // Jumps - Edge to target label node
	Iterator l = jumps.labels.iterator();		
	while( l.hasNext())
	  addEdge(n, (compiler.graph.Node)l2n.get(l.next()));
      }
    }
  }
}
