package compiler.assem;

public class InstrList {
  public Instr head;
  public InstrList tail;
  public InstrList(Instr h, InstrList t) {
    head=h; tail=t;
  }
  public String toString() {
	String result = head.toString();
	InstrList il = tail;
	while (il != null) {
		result = result + "\n"+ il.head;
		il=il.tail;
	} 
	return result;
  }
}
