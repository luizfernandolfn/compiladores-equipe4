package compiler.symbol;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;

public class Table {

	private Hashtable<Symbol, BucketHead> hashtable;
	private Stack<Symbol> stack;
	private static Symbol beginScope = Symbol.symbol(null);

	public Table() {

	}

	public void put(Symbol key, Object value) {
		stack.add(key);
		hashtable.get(key).add(key, value);
	}

	public Object get(Symbol key) {
		return hashtable.get(key).get().binding;
	}

	public void beginScope() {
		stack.add(beginScope);
	}

	public void endScope() {
		
		Symbol first = stack.firstElement();
		while(first != beginScope){
			hashtable.get(first).pop();
			stack.pop();
			first = stack.firstElement();
		}
		if(first == beginScope){
			stack.pop();
		}
	}

	public Enumeration keys() {
		return null;
	}
}
