package compiler.symbol;

import java.util.Dictionary;
import java.util.Hashtable;

public class Symbol {
	private String name;

	//Dicionário: compartilhado entre todos os objetos symbol.
	private static Dictionary<String, Symbol> dict = new Hashtable<String, Symbol>();

	private Symbol(String n) {
		name = n;
	}

	//Mapeia symbol -> string
	public String toString() {
		return name;
	}

	//Mapeia string -> symbol. se não existir, cria e adiciona ao dicionário.
	public static Symbol symbol(String n) {
		String u = n.intern();
		Symbol s = (Symbol) dict.get(u);
		if (s == null) {
			s = new Symbol(u);
			dict.put(u, s);
		}
		return s;
	}
}