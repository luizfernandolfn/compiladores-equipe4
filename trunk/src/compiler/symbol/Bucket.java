package compiler.symbol;

public class Bucket {
	
	Symbol key;
	Object binding;
	Bucket next;
	
	public Bucket(Symbol key, Object binding, Bucket next){
		this.key = key;
		this.binding = binding;
		this.next = next;
	}

}
