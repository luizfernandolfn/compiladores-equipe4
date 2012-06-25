package compiler.temp;

import compiler.symbol.Symbol;

public class Label  {
	private String name;
	private static int count;

	public String toString() {return name;}

	public Label(String n) {
		name=n;
	}

	public Label() {
		this("L" + count++);
	}

	public Label(Symbol s) {
		this(s.toString());
	}
}
