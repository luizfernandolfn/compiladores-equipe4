package compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import compiler.javacc.MiniJavaParser;
import compiler.syntaxtree.Program;
import compiler.visitor.PrettyPrintVisitor;

public class Compiler {
	public static void main(String args[]) {
		System.out.println("It's Working?");
		// testando
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("test.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MiniJavaParser parser = new MiniJavaParser(reader);
		Program p = parser.parse();
		PrettyPrintVisitor printer = new PrettyPrintVisitor();
		printer.visit(p);
	}
}
