package compiler.symbol;

public class Bucket {
	Symbol key;
	Object binding;
	Bucket next;
	
	public Bucket(Symbol k, Object b, Bucket n){
		key = k;
		binding = b;
		next = n;
	}

}
