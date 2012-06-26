package compiler.assem;

import java.util.List;
import java.util.LinkedList;
import compiler.temp.*;

public class OPER extends Instr {
   public Temp[] dst;   
   public Temp[] src;
   public Targets jump;

   public OPER(String a, Temp[] d, Temp[] s, LabelList j) {
	LinkedList list = new LinkedList();
	while(j!=null){
		list.add(j.head);
		j=j.tail;
	}

      assem=a; dst=d; src=s; jump=new Targets(list);
   }
   
   public OPER(String a, Temp[] d, Temp[] s, List<Label> j) {
      assem=a; dst=d; src=s; jump=new Targets(j);
   }
   
   public OPER(String a, Temp[] d, Temp[] s) {
      assem=a; dst=d; src=s; jump=null;
   }

   public Temp[] use() {return src;}
   public Temp[] def() {return dst;}
   public Targets jumps() { return jump;}

   public String toString() {
	return "OPER["+assem+"]"+"["+toString(dst)+"]"+"["+toString(src)+"]"+"["+toString(jump)+"]";
   }
}
