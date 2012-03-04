package chapter1_interpreter;

//Composto Stm
//uma afirmação composta de duas afirmações
public class CompoundStm extends Stm {
	public Stm stm1, stm2;
	public CompoundStm( Stm s1, Stm s2 ) {
		stm1 = s1;
		stm2 = s2;
	}
}
