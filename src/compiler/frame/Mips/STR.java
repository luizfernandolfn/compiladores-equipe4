/*
 * Created on May 28, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package mips;

import tree.ExpList;
import tree.Stm;

/**
 * @author ra992237
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class STR extends Stm
{
	public String str;
	public temp.Label lab;

	STR(String s, temp.Label l)
	{
		str = s;
		lab = l;
	}

	public ExpList kids()
	{
		return null;
	}
	
	public tree.Stm build(ExpList kids)
	{
		return this;
	}

	public String toString()
	{
		return lab.toString()+":\n" + ".word " + str.length() + "\n.asciiz \""+str+"\"\n";
	}
}
