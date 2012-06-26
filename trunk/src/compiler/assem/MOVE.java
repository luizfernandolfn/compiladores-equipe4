package compiler.assem;

public class MOVE extends Instr {
   public compiler.temp.Temp dst;   
   public compiler.temp.Temp src;

   public MOVE(String a, compiler.temp.Temp d, compiler.temp.Temp s) {
      assem=a; dst=d; src=s;
   }
   public compiler.temp.Temp[] use() {return new compiler.temp.Temp[]{src};}
   public compiler.temp.Temp[] def() {return new compiler.temp.Temp[]{dst};}
   
   public Targets jumps()     {return null;}
   public String toString() {
	return "MOVE[" + assem + "]["+dst + "]["+src+"]";
   }
}
