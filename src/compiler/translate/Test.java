package compiler.translate;

import compiler.temp.SimpleExp;
import compiler.temp.Temp;

public class Test {

	public void test() {
		SimpleExp se = new Temp(1);
		if (se instanceof Temp)
			System.exit(-1);
	}
}
