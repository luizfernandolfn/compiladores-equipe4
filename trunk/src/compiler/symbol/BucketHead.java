package compiler.symbol;

public class BucketHead {
	
	private Bucket head;
	
	public void add(Symbol key, Object binding){
		
		head = new Bucket(key, binding, head);
	}
	
	public void pop(){
		
		if( head != null ){
			head=head.next;
		}
	}
	
	public Bucket get(){
		
		return head;
	}
}
