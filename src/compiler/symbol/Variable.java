package compiler.symbol;

import compiler.syntaxtree.Type;

public class Variable {
	
	 public String id;
	 public Type type;
	    
	 public Variable(String id, Type type) {
		 
		 this.id = id;
		 this.type = type;
	 }
	    
	 public String id() { 
		 return id; 
	 }
	    
	 public Type type() { 
		 return type; 
	 }
}
