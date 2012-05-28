package compiler.assem;

import compiler.temp.LabelList;
import compiler.temp.TempList;

public class OPER extends Instr {
	public TempList dst;
	public TempList src;
	public Targets jump;

	public OPER(String a, TempList d, TempList s, LabelList j) {
		assem = a;
		dst = d;
		src = s;
		jump = new Targets(j);
	}

	public OPER(String a, TempList d, TempList s) {
		assem = a;
		dst = d;
		src = s;
		jump = null;
	}

	@Override
	public TempList use() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TempList def() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Targets jumps() {
		// TODO Auto-generated method stub
		return null;
	}
}