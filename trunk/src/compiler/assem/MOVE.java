package compiler.assem;

import compiler.temp.Temp;
import compiler.temp.TempList;
import compiler.util.List;

public class MOVE extends Instr {
	public Temp dst;
	public Temp src;

	public MOVE(String a, Temp d, Temp s) {
		assem = a;
		dst = d;
		src = s;
	}

	public TempList use() {
		return new TempList(src, null);
	}

	public TempList def() {
		return new TempList(dst, null);
	}

	public Targets jumps() {
		return null;
	}

}
