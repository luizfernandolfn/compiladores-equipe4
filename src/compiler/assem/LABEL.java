package compiler.assem;

public class LABEL extends Instr {
   public compiler.temp.Label label;

   public LABEL(String a, compiler.temp.Label l) {
      assem=a; label=l;
   }

   public compiler.temp.Temp[] use() {return null;}
   public compiler.temp.Temp[] def() {return null;}
   public Targets jumps() {return null;}

   public String toString() {
	return "LABEL["+assem +"]["+ label+"]";
   }
}
