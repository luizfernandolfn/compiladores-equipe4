package compiler.regalloc;

import compiler.graph.Node;

import java.util.Hashtable;
import java.util.List;

import compiler.temp.Temp;
import compiler.temp.TempMap;

class Color implements TempMap{
	
    Color(InterferenceGraph ig, TempMap initial, List<Temp> registers, 
            Hashtable<Node, Integer> cost){
        super();
    }

    public String tempMap(Temp t){
    	
        return null;
    }

    List<Temp> spills(){
    	
        return null;
    }
}