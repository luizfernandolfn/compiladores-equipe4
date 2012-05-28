package compiler.frame.Mips;

import compiler.tree.CodeVisitor;
import compiler.tree.ExpList;
import compiler.tree.IntVisitor;
import compiler.tree.Stm;
import compiler.temp.Label;

public class STR extends Stm{
	
	public String str;
	public compiler.temp.Label lab;

	STR(String s, Label l){
		
		str = s;
		lab = l;
	}

	public ExpList kids()
	{
		return null;
	}
	
	public compiler.tree.Stm build(ExpList kids)
	{
		return this;
	}

	public String toString()
	{
		return lab.toString()+":\n" + ".word " + str.length() + "\n.asciiz \""+str+"\"\n";
	}

	@Override
	public void accept(IntVisitor v, int d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accept(CodeVisitor v) {
		// TODO Auto-generated method stub
		
	}
}
