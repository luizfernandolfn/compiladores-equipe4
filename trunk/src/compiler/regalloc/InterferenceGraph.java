package compiler.regalloc;

import compiler.graph.Graph;
import compiler.graph.Node;

abstract public class InterferenceGraph extends Graph {
   abstract public Node tnode(compiler.temp.Temp temp);
   abstract public compiler.temp.Temp gtemp(Node node);
   abstract public MoveList moves();
   public int spillCost(Node node) {return 1;}
}