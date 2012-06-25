package RegAlloc;
import Tree.*;
import Temp.*;
import munch.Codegen;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Tests AssemFlowGraph and Liveness.
 * 
 * @author Ray Wisman
 * @version 1.0
 */

public class test {
	
	public static void main(String [] args) {
		Assem.InstrList il;
		Codegen cg=new Codegen(new LinkedList().listIterator());

/*		System.out.println("Test of Mips Code generator - Codegen\n");
		System.out.println("____FIGURE 9.2 of Appel______\n"+
                "    MOVE(					\n"+
		"	MEM(					\n"+
		"	   BINOP(				\n"+
		"		BINOP.PLUS, 			\n"+
		"		MEM(				\n"+
		"		  BINOP(			\n"+
		"		     BINOP.PLUS,		\n"+
		"		     TEMP(new Temp(8)), 	\n"+
		"		     CONST(8)			\n"+
		"		  )				\n"+
		"		),				\n"+
		"	        BINOP(				\n"+
		"		  BINOP.MUL,			\n"+
		"		  TEMP(new Temp(4)),		\n"+
		"		  CONST(4)			\n"+
                "                )				\n"+
                "            )					\n"+
 		"	),					\n"+
		"	MEM(					\n"+
		"	   BINOP(				\n"+
		"		BINOP.PLUS,			\n"+
		"		TEMP(new Temp(8)),		\n"+
		"		CONST(10)			\n"+
		"	   )					\n"+
		"	)					\n"+
		"    )						\n"
		);
		
		    MOVE(
			MEM(
			   BINOP(
				BINOP.PLUS, 
				MEM(
				  BINOP(
				     BINOP.PLUS,
				     TEMP(new Temp(8)), 
				     CONST(8)
				  )
				),
			        BINOP(
				  BINOP.MUL,
				  TEMP(new Temp(4)),
				  CONST(4)
                                )
                            )
 			),
			MEM(
			   BINOP(
				BINOP.PLUS,
				TEMP(new Temp(8)),
				CONST(10)
			   )
			)
		    ).accept(cg);
*/

		Label T = new Label("T");
		Label F = new Label("F");
		TEMP a = new TEMP(new Temp(1));
		TEMP b = new TEMP(new Temp(2));
		TEMP c = new TEMP(new Temp(3));
		int N=8;
								     
		MOVE(a,CONST(0)).accept(cg);                
                		                               
		MOVE(b,a).accept(cg);                        
		LABEL(F).accept(cg);			
		MOVE(c,BINOP(BINOP.PLUS,c,b)).accept(cg);            	
		JUMP(F).accept(cg);		
		MOVE(c,c).accept(cg);

		il=cg.codegen();
		System.out.println(il.toString());
		new AssemFlowGraph.AssemFlowGraph(il).show(System.out);
		RegAlloc.Liveness la = new RegAlloc.Liveness( new AssemFlowGraph.AssemFlowGraph(il));
	        System.out.println("Live Interval\n"+la.toString());
		System.out.println("Interference Graph");
		la.show(System.out);
	}


   public static Exp CONST(int value)  { return new CONST(value); }
   public static Exp TEMP(Temp temp)   {  return new TEMP(temp); }
   public static Exp BINOP(int binop, Exp left, Exp right) {
       return new BINOP(binop, left, right);
   }
   public static Exp MEM(Exp exp) {  return new MEM(exp); }
   public static Stm MOVE(Exp dst, Exp src) {
       return new MOVE(dst, src);
   }
   public static Stm EXP(Exp exp) {  return new EXP1(exp); }

   private static Exp NAME(Label label) {  return new NAME(label); }
   private static Exp CALL(Exp func, ExpList args) {
       return new CALL(func, args);
   }
   private static Stm JUMP(Label target) { return new JUMP(target); }
   private static Stm CJUMP(int relop, Exp l, Exp r, Label t, Label f) {
       return new CJUMP(relop, l, r, t, f);
   }
   private static Stm LABEL(Label label) { return new LABEL(label); }
}