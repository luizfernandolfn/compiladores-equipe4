package compiler.tree;

import java.util.Iterator;
import java.util.LinkedList;

public class ExpList {
	public Exp head;
	public ExpList tail;
	private LinkedList list;

	public ExpList(Exp h, ExpList t) {
		head = h;
		tail = t;
	}
	
	public Iterator iterator() {
		list = new LinkedList();
		Exp h=head;
		ExpList t = tail;
		list.add(h);
		while(t!=null){
			h=head;
			list.add(h);
			t=t.tail;
		}
		return list.iterator();
	  } 	
}
