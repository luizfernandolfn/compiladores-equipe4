package compiler.frame.Mips;

import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import compiler.temp.Temp;
import compiler.temp.Label;
import compiler.frame.Frame;
import compiler.frame.Access;

public class MipsFrame extends Frame {

    //Mini Java Library will be appended to end of 
    //program
    public String programTail(){
	
	return      
	    "         .text            \n" +
	    "         .globl _malloc   \n" +
	    "_malloc:                  \n" +
	    "         li $v0, 9        \n" +
	    "         syscall          \n" +
	    "         j $ra            \n" +
	    "                          \n" +
	    "         .text            \n" +
	    "         .globl _printint \n" +
	    "_printint:                \n" +
	    "         li $v0, 1        \n" +
	    "         syscall          \n" +
	    "         la $a0, newl     \n" +
	    "         li $v0, 4        \n" +
	    "         syscall          \n" +
	    "         j $ra            \n" +
	    "                          \n" +
	    "         .data            \n" +
	    "         .align   0       \n" +
	    "newl:    .asciiz \"\\n\"  \n" +
	    "         .data            \n" +
	    "         .align   0       \n" +
	    "str_er:  .asciiz \" ERROR: abnormal termination\\n\" "+
	    "                          \n" +
	    "         .text            \n" +
	    "         .globl _error    \n" +
	    "_error:                   \n" +
	    "         li $v0, 4        \n" +
	    "         la $a0, str_er   \n" +
	    "         syscall          \n" +
	    "         li $v0, 10       \n" +
	    "         syscall          \n" ;
    }
    
    public Frame newFrame(String name, List formals) {
        if (this.name != null) name = this.name + "." + name;
        return new MipsFrame(name, formals);
    }

    public MipsFrame() {}

    private static HashMap  functions
	= new HashMap();
    private MipsFrame(String n, List  f) {
	Integer count = (Integer)functions.get(n);
	if (count == null) {
	    count = new Integer(0);
	    name = new Label(n);
	} else {
	    count = new Integer(count.intValue() + 1);
	    name = new Label(n + "." + count);
	}
	functions.put(n, count);
        actuals = new LinkedList();
	formals = new LinkedList();
	int offset = 0;
        Iterator  escapes = f.iterator();
	formals.add(allocLocal(((Boolean)escapes.next()).booleanValue()));
	actuals.add(new InReg(V0));
	for (int i = 0; i < argRegs.length; ++i) {
	    if (!escapes.hasNext())
		break;
	    offset += wordSize;
	    actuals.add(new InReg(argRegs[i]));
	    if (((Boolean)escapes.next()).booleanValue())
		formals.add(new InFrame(offset));
	    else
		formals.add(new InReg(new Temp()));
	}
	while (escapes.hasNext()) {
	    offset += wordSize;
            Access actual = new InFrame(offset);
	    actuals.add(actual);
	    if (((Boolean)escapes.next()).booleanValue())
		formals.add(actual);
	    else
		formals.add(new InReg(new Temp()));
	}
    }

    private static final int wordSize = 4;
    public int wordSize() { return wordSize; }

    private int offset = 0;
    public Access allocLocal(boolean escape) {
	if (escape) {
	    Access result = new InFrame(offset);
	    offset -= wordSize;
	    return result;
	} else
	    return new InReg(new Temp());
    }

    static final Temp ZERO = new Temp(); // zero reg
    static final Temp AT = new Temp(); // reserved for assembler
    static final Temp V0 = new Temp(); // function result
    static final Temp V1 = new Temp(); // second function result
    static final Temp A0 = new Temp(); // argument1
    static final Temp A1 = new Temp(); // argument2
    static final Temp A2 = new Temp(); // argument3
    static final Temp A3 = new Temp(); // argument4
    static final Temp T0 = new Temp(); // caller-saved
    static final Temp T1 = new Temp();
    static final Temp T2 = new Temp();
    static final Temp T3 = new Temp();
    static final Temp T4 = new Temp();
    static final Temp T5 = new Temp();
    static final Temp T6 = new Temp();
    static final Temp T7 = new Temp();
    static final Temp S0 = new Temp(); // callee-saved
    static final Temp S1 = new Temp();
    static final Temp S2 = new Temp();
    static final Temp S3 = new Temp();
    static final Temp S4 = new Temp();
    static final Temp S5 = new Temp();
    static final Temp S6 = new Temp();
    static final Temp S7 = new Temp();
    static final Temp T8 = new Temp(); // caller-saved
    static final Temp T9 = new Temp();
    static final Temp K0 = new Temp(); // reserved for OS kernel
    static final Temp K1 = new Temp(); // reserved for OS kernel
    static final Temp GP = new Temp(); // pointer to global area
    static final Temp SP = new Temp(); // stack pointer
    static final Temp S8 = new Temp(); // callee-save (frame pointer)
    static final Temp RA = new Temp(); // return address

    // Register lists: must not overlap and must include every register that
    // might show up in code
    private static final Temp[]
	// registers dedicated to special purposes
	specialRegs = { ZERO, AT, K0, K1, GP, SP },
	// registers to pass outgoing arguments
	argRegs	= { A0, A1, A2, A3 },
        // registers that a callee must preserve for its caller
	calleeSaves = { RA, S0, S1, S2, S3, S4, S5, S6, S7, S8 },
	// registers that a callee may use without preserving
	callerSaves = { T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, V0, V1 };

    static final Temp FP = new Temp();
    public Temp FP() { return FP; }
    public Temp RV() { return V0; }

    private static HashMap labels = new HashMap(); 
    public compiler.tree.Exp externalCall(String s, List args) {
	String func = s.intern();
	Label l = (Label)labels.get(func);
	if (l == null) {
	    l = new Label("_" + func);
	    labels.put(func, l);
	}
	args.add(0, new compiler.tree.CONST(0));
	return new compiler.tree.CALL(new compiler.tree.NAME(l), args);
    }

    public String string(Label lab, String string) { // to translate string fragments. There are no such in minijava!
	int length = string.length();
	String lit = "";
	for (int i = 0; i < length; i++) {
	    char c = string.charAt(i);
	    switch(c) {
	    case '\b': lit += "\\b"; break;
	    case '\t': lit += "\\t"; break;
	    case '\n': lit += "\\n"; break;
	    case '\f': lit += "\\f"; break;
	    case '\r': lit += "\\r"; break;
	    case '\"': lit += "\\\""; break;
	    case '\\': lit += "\\\\"; break;
	    default:
		if (c < ' ' || c > '~') {
		    int v = (int)c;
		    lit += "\\" + ((v>>6)&7) + ((v>>3)&7) + (v&7);
		} else
		    lit += c;
		break;
	    }
	}
	return "\t.data\n\t.word " + length + "\n" + lab.toString()
	    + ":\t.asciiz\t\"" + lit + "\"";
    }

    private static final Label badPtr = new Label("BADPTR");
    public Label badPtr() {
	return badPtr;
    }

    private static final Label badSub = new Label("BADSUB");
    public Label badSub() {
	return badSub;
    }

    private static final
	HashMap tempMap = new HashMap(32);
    static {
        tempMap.put(ZERO, "$0");
	tempMap.put(AT,   "$at");
	tempMap.put(V0,   "$v0");
	tempMap.put(V1,   "$v1");
	tempMap.put(A0,   "$a0");
	tempMap.put(A1,   "$a1");
	tempMap.put(A2,   "$a2");
	tempMap.put(A3,   "$a3");
	tempMap.put(T0,   "$t0");
	tempMap.put(T1,   "$t1");
	tempMap.put(T2,   "$t2");
	tempMap.put(T3,   "$t3");
	tempMap.put(T4,   "$t4");
	tempMap.put(T5,   "$t5");
	tempMap.put(T6,   "$t6");
	tempMap.put(T7,   "$t7");
	tempMap.put(S0,   "$s0");
	tempMap.put(S1,   "$s1");
	tempMap.put(S2,   "$s2");
	tempMap.put(S3,   "$s3");
	tempMap.put(S4,   "$s4");
	tempMap.put(S5,   "$s5");
	tempMap.put(S6,   "$s6");
	tempMap.put(S7,   "$s7");
	tempMap.put(T8,   "$t8");
	tempMap.put(T9,   "$t9");
	tempMap.put(K0,   "$k0");
	tempMap.put(K1,   "$k1");
	tempMap.put(GP,   "$gp");
	tempMap.put(SP,   "$sp");
	tempMap.put(S8,   "$fp");
	tempMap.put(RA,   "$ra");
    }
    public String tempMap(Temp temp) {
	return (String)tempMap.get(temp);
    }

    int maxArgOffset = 0;

    public List codegen(List stms) { // takes a list of tree.Stm returns a list of assembler instrs
	List insns = new java.util.LinkedList(); // for the assembler instructions.
	Codegen cg = new Codegen(this, insns.listIterator()); // what you have to program is the Codegen!
	java.util.Iterator s = stms.iterator();
	while(s.hasNext())
	    ((compiler.tree.Stm)s.next()).accept(cg);
	return insns;
    }

    private static  void addAll(java.util.Collection c, Object[] a) {
	for (int i = 0; i < a.length; i++)
	    c.add(a[i]);
    }

    // Registers live on return
    private static Temp[] returnSink = {};
    {
	LinkedList l = new LinkedList(); // of temporaries!
	l.add(V0);
	addAll(l, specialRegs);
	addAll(l, calleeSaves);
	returnSink = (Temp[])l.toArray(returnSink);
    }

    // Registers defined by a call
    static Temp[] calldefs = {};
    {
	LinkedList l = new LinkedList(); // of temporaries
	l.add(RA);
	addAll(l, argRegs);
	addAll(l, callerSaves);
	calldefs =  (Temp[])l.toArray(calldefs);
    }

    private void
	assignFormals(Iterator formals,
		      Iterator actuals,
		      List body)
    {
	if (!formals.hasNext() || !actuals.hasNext())
	    return;
	Access formal = (Access)formals.next();
	Access actual = (Access)actuals.next();
	assignFormals(formals, actuals, body);
	body.add(0, new compiler.tree.MOVE(formal.exp(new compiler.tree.TEMP(FP)), actual.exp(new compiler.tree.TEMP(FP))));
    }

    private void assignCallees(int i, List  body) // the body is a list with tree statements
    {
	if (i >= calleeSaves.length)
	    return;
	Access a = allocLocal(!spilling);
	assignCallees(i+1, body);
	body.add(0, new compiler.tree.MOVE(a.exp(new compiler.tree.TEMP(FP)), new compiler.tree.TEMP(calleeSaves[i])));
	body.add(new compiler.tree.MOVE(new compiler.tree.TEMP(calleeSaves[i]), a.exp(new compiler.tree.TEMP(FP))));
    }




    private List actuals; // list of accesses

    public void procEntryExit1(List body) {
	assignFormals(formals.iterator(), actuals.iterator(), body);
	assignCallees(0, body);
    }

    private static compiler.assem.Instr OPER(String a, Temp[] d, Temp[] s) {
    	//return new compiler.assem.OPER(a, d, s, null);
    	return null;
    }



    public void procEntryExit2(List body) { // body is now a list  of assembler instructions
	body.add(OPER("#\treturn", null, returnSink));
    }

    public void procEntryExit3(List body) { // body is now a list  of assembler instructions
    	int frameSize = maxArgOffset - offset;
	ListIterator  cursor = body.listIterator();
	cursor.add(OPER("\t.text", null, null));
	cursor.add(OPER(name + ":", null, null));
	cursor.add(OPER(name + "_framesize=" + frameSize, null, null));
	if (frameSize != 0) {
	    cursor.add(OPER("\tsubu $sp " + name + "_framesize",
			    new Temp[]{SP}, new Temp[]{SP}));
	    body.add(OPER("\taddu $sp " + name + "_framesize",
			  new Temp[]{SP}, new Temp[]{SP}));
	}
	body.add(OPER("\tj $ra", null, new Temp[]{RA}));
    }


    private static Temp[] registers = {};
    {
	LinkedList l = new LinkedList();
	addAll(l, callerSaves);
	addAll(l, calleeSaves);
	addAll(l, argRegs);
	addAll(l, specialRegs);
	registers = (Temp[])l.toArray(registers);
    }
  
    public  compiler.temp.Temp[] registers(){
	return registers;
    }
    
    private static boolean spilling = false;
    public  void spill(List insns, compiler.temp.Temp[] spills){
    }
}
