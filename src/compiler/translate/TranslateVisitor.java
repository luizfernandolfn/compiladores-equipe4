package compiler.translate;

import minijava.node.*;
import minijava.analysis.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.List;
import compiler.symbol.SymbolTable;
import compiler.symbol.Class;
import compiler.symbol.Method;
import compiler.symbol.Variable;
import compiler.temp.Label;
import compiler.temp.Temp;
import compiler.temp.Offset;
import compiler.temp.SimpleExp;
import compiler.symbol.Symbol;

public class TranslateVisitor extends DepthFirstAdapter {

	boolean debug = true;
	static Class currClass;
	static Method currMethod;
	static SymbolTable symbolTable;
	public Label exit = null;
	private Level topLevel;
	public Level level;

	public TranslateVisitor(SymbolTable s) {
		JVM.JVMFrame frame = new JVM.JVMFrame();
		level = topLevel = new Level(frame);
		symbolTable = s;
	}

	public TranslateVisitor(compiler.frame.Frame frame) {
		level = topLevel = new Level(frame);
	}

	public Level newLevel(Symbol name, boolean isLeaf, List formals) {
		// we add the static link here
		formals.add(0, new Boolean(!isLeaf));
		return new Level(level, name, formals);
	}

	public Iterator formals() {
		Iterator formals = level.formals.iterator();
		formals.next(); // strip static link
		return formals;
	}

	private LinkedList frags = new LinkedList();

	public void procEntryExit(Exp body) {
		compiler.frame.Frame myframe = level.frame;
		compiler.tree.Exp bodyExp = body.unEx();
		compiler.tree.Stm bodyStm;
		if (bodyExp != null)
			bodyStm = MOVE(TEMP(myframe.RV()), bodyExp);
		else
			bodyStm = body.unNx();
		ProcFrag frag = new ProcFrag(bodyStm, myframe);
		frags.add(frag);
	}

	public Iterator getResults() {
		return frags.iterator();
	}

	// MainClass ClassDeclList
	public void caseAProgram(AProgram node) {
		if (debug)
			System.out.println("TranslateVisitor.AProgram <" + node + ">");
		node.getMainClass().apply(this);
		LinkedList t = node.getClassDecl();
		Iterator i = t.iterator();
		while (i.hasNext())
			((PClassDecl) i.next()).apply(this);

		DataFrag frag = new DataFrag(topLevel.frame.programTail());
		frags.add(frag);
	}

	// Classname Arg Statement
	public void caseAMainClass(AMainClass node) {
		if (debug)
			System.out.println("TranslateVisitor.AMainClass <" + node + ">");
		currClass = symbolTable.getClass(node.getClassname().toString());
		currMethod = currClass.getMethod("main");

		Symbol sym = Symbol.symbol("main");
		Level oldlevel = level;

		LinkedList params = new LinkedList();
		level = newLevel(sym, false, params);
		node.getStatement().apply(this);
		procEntryExit(getExp(node.getStatement()));
		level = oldlevel;
	}

	// Id VarDecl* MethodDecl*
	public void caseAClassDecl(AClassDecl node) {
		if (debug)
			System.out.println("TranslateVisitor.AClassDecl <" + node + ">");
		currClass = symbolTable.getClass(node.getId().toString());
		LinkedList v = node.getVarDecl();
		Iterator iv = v.iterator();
		while (iv.hasNext())
			((AVarDecl) iv.next()).apply(this);
		LinkedList m = node.getMethodDecl();
		Iterator im = m.iterator();
		while (im.hasNext())
			((AMethodDecl) im.next()).apply(this);
	}

	// Classname Extend VarDecl* MethodDecl*
	public void caseAExtendsClassDecl(AExtendsClassDecl node) {
		if (debug)
			System.out.println("TranslateVisitor.AExtendsClassDecl <" + node
					+ ">");
		currClass = symbolTable.getClass(node.getClassname().toString());

		LinkedList v = node.getVarDecl();
		Iterator iv = v.iterator();
		while (iv.hasNext())
			((AVarDecl) iv.next()).apply(this);
		LinkedList m = node.getMethodDecl();
		Iterator im = m.iterator();
		while (im.hasNext())
			((AMethodDecl) im.next()).apply(this);
	}

	// Type Id
	public void caseAVarDecl(AVarDecl node) {
		if (debug)
			System.out.println("TranslateVisitor.AVarDecl <" + node + ">");
		node.getType().apply(this);
		node.getId().apply(this);
	}

	// Type Id FormalList VarDecl* StatementList Exp
	public void caseAMethodDecl(AMethodDecl node) {
		if (debug)
			System.out.println("TranslateVisitor.AMethodDecl <" + node + ">");
		node.getType().apply(this);
		String id = node.getId().toString();

		currMethod = currClass.getMethod(id);

		node.getFormalList().apply(this);

		Node retType = currMethod.Type();

		String cname = currClass.getId();
		String mname = currMethod.getId();
		String mlabel = cname + "_" + mname;
		int arg_number = currMethod.sizeParams();

		Level oldlevel = level;
		LinkedList params = new LinkedList();
		Symbol sym = Symbol.symbol(mlabel);

		for (int i = 0; i <= arg_number; i++)
			params.add(new Boolean(false));

		level = newLevel(sym, false, params);

		compiler.tree.Stm stms;
		Iterator e = node.getStatement().iterator();

		// empty block
		if (!e.hasNext()) {

			stms = null;
			node.getExp().apply(this);
			Exp retexp = getExp(node.getExp());
			procEntryExit(new Ex(ESEQ(stms, retexp.unEx())));
			level = oldlevel;
		}
		PStatement s = ((PStatement) e.next());
		s.apply(this);
		Exp exp = getExp(s);

		// only one stm int block
		if (!e.hasNext())
			stms = exp.unNx();
		stms = exp.unNx();

		// more than one stmt int block
		while (e.hasNext()) {
			s = ((PStatement) e.next());
			s.apply(this);
			exp = getExp(s);
			stms = SEQ(stms, exp.unNx());
		}

		node.getExp().apply(this);
		Exp retexp = getExp(node.getExp());
		procEntryExit(new Ex(ESEQ(stms, retexp.unEx())));
		level = oldlevel;

		setExp(node, retexp);
	}

	// Type Id
	public void caseAFormalList(AFormalList node) {
		if (debug)
			System.out.println("TranslateVisitor.AFormalList <" + node + ">");
		node.getType().apply(this);
		node.getId().apply(this);
		Iterator fr = ((LinkedList) node.getFormalRest()).iterator();
		while (fr.hasNext())
			((AFormalRest) fr.next()).apply(this);
	}

	public void caseAFormalRest(AFormalRest node) {
		if (debug)
			System.out.println("TranslateVisitor.AFormalRest <" + node + ">");
		node.getType().apply(this);
		node.getId().apply(this);
	}

	// Statementlist
	public void caseAStatementlistStatement(AStatementlistStatement node) {
		if (debug)
			System.out.println("TranslateVisitor.AStatementlistStatement <"
					+ node + ">");
		Iterator sl = node.getStatement().iterator();
		while (sl.hasNext())
			((PStatement) sl.next()).apply(this);
		Iterator e = node.getStatement().iterator();

		// empty block
		if (!e.hasNext()) {
			setExp(node, new Nx(null));
			return;
		}
		PStatement s = ((PStatement) e.next());
		s.apply(this);
		Exp exp = getExp(s);

		// only one stm int block
		if (!e.hasNext()) {
			setExp(node, exp);
			return;
		}
		compiler.tree.Stm stm = exp.unNx();

		// more than one stmt int block
		while (e.hasNext()) {
			s = ((PStatement) e.next());
			s.apply(this);
			exp = getExp(s);
			stm = SEQ(stm, exp.unNx());
		}
		setExp(node, new Nx(stm));
	}

	// Id Exp
	public void caseAAssignStatement(AAssignStatement node) {
		if (debug)
			System.out.println("TranslateVisitor.AAssignStatement <" + node
					+ ">");
		node.getId().apply(this);
		node.getExp().apply(this);

		Exp e1 = getExp(node.getId());
		Exp e2 = getExp(node.getExp());

		if (e1.unEx() instanceof compiler.tree.TEMP)
			setExp(node, new Nx(MOVE(e1.unEx(), e2.unEx())));
		else {
			Temp z = new Temp(0);
			setExp(node,
					new Nx(MOVE(
							MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(z),
									e1.unEx())), e2.unEx())));
		}
	}

	// Id Index R
	public void caseAArrayAssignStatement(AArrayAssignStatement node) {
		if (debug)
			System.out.println("TranslateVisitor.AArrayAssignStatement <"
					+ node + ">");

		node.getId().apply(this);

		compiler.tree.Exp e1 = getExp(node.getId()).unEx();

		if (!(e1 instanceof compiler.tree.TEMP)) {
			Temp taux1 = new Temp();
			Temp taux2 = new Temp();
			e1 = ESEQ(
					SEQ(MOVE(TEMP(taux1),
							BINOP(compiler.tree.BINOP.MUL, e1, CONST(4))),
							MOVE(TEMP(taux2),
									MEM(BINOP(compiler.tree.BINOP.PLUS,
											TEMP(new Temp(0)), TEMP(taux1))))),
											TEMP(taux2));
		}

		node.getIndex().apply(this);
		compiler.tree.Exp e2 = getExp(node.getIndex()).unEx();

		Temp t = new Temp();
		Temp t_index = new Temp();
		Temp t_size = new Temp();

		LinkedList argList = new LinkedList();

		compiler.tree.ExpList args1 = new compiler.tree.ExpList(argList);

		Label T = new Label();
		Label F = new Label();

		e2 = ESEQ(
				SEQ(SEQ(SEQ(
						SEQ(SEQ(MOVE(TEMP(t_index),
								BINOP(compiler.tree.BINOP.MUL, e2, CONST(4))),
								MOVE(TEMP(t_size), MEM(e1))),
								CJUMP(compiler.tree.CJUMP.GE, TEMP(t_index),
										TEMP(t_size), T, F)), LABEL(T)),
										MOVE(TEMP(new Temp()),
												CALL(NAME(new Label("_error")), args1))),
												LABEL(F)), TEMP(t_index));

		node.getR().apply(this);
		compiler.tree.Exp e3 = getExp(node.getR()).unEx();

		setExp(node,
				new Nx(
						MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, e1,
								BINOP(compiler.tree.BINOP.PLUS, e2, CONST(4)))),
								e3)));
	}

	// Exp trueStatement falseStatement
	public void caseAIfStatement(AIfStatement node) {
		if (debug)
			System.out.println("TranslateVisitor.AIfStatement <" + node + ">");
		node.getExp().apply(this);
		node.getTrue().apply(this);
		node.getFalse().apply(this);

		Label T = new Label();
		Label F = new Label();
		Label D = new Label();
		Exp exp = getExp(node.getExp());
		Exp stmT = getExp(node.getTrue());
		Exp stmF = getExp(node.getFalse());
		setExp(node,
				new Nx(SEQ(
						SEQ(SEQ(SEQ(
								CJUMP(compiler.tree.CJUMP.EQ, exp.unEx(),
										CONST(1), T, F),
										SEQ(LABEL(T), stmT.unNx())), JUMP(D)),
										SEQ(LABEL(F), stmF.unNx())), LABEL(D))));
	}

	// Exp Statement
	public void caseAWhileStatement(AWhileStatement node) {
		if (debug)
			System.out.println("TranslateVisitor.AWhileStatement <" + node
					+ ">");
		node.getExp().apply(this);
		node.getStatement().apply(this);

		Label test = new Label();
		Label T = new Label();
		Label F = new Label();
		Exp exp = getExp(node.getExp());
		Exp body = getExp(node.getStatement());

		setExp(node,
				new Nx(SEQ(
						SEQ(SEQ(LABEL(test),
								(CJUMP(compiler.tree.CJUMP.EQ, exp.unEx(),
										CONST(1), T, F))),
										(SEQ(LABEL(T), body.unNx()))), LABEL(F))));
	}

	// Exp
	public void caseAPrintlnStatement(APrintlnStatement node) {
		if (debug)
			System.out.println("TranslateVisitor.APrintlnStatement <" + node
					+ ">");
		node.getExp().apply(this);
		Exp e = getExp(node.getExp());

		LinkedList argList = new LinkedList();
		argList.add(e.unEx());

		compiler.tree.ExpList args = new compiler.tree.ExpList(argList);

		setExp(node,
				new Nx(MOVE(TEMP(new Temp()),
						CALL(NAME(new Label("_printint")), args))));
	}

	// Expressions

	// Exp L, R;
	public void caseAAndExp(AAndExp node) {
		if (debug)
			System.out.println("TranslateVisitor.AAndExp <" + node + ">");
		node.getL().apply(this);
		node.getR().apply(this);

		Temp t1 = new Temp();
		Label done = new Label();
		Label ok1 = new Label();
		Label ok2 = new Label();
		compiler.tree.Exp left = getExp(node.getL()).unEx();
		compiler.tree.Exp riglt = getExp(node.getR()).unEx();
		/*
		 * (left && right) eval left first then right
		 * 
		 * MOVE(t1,0) CJUMP(EQ, left,1,ok, done) ok1 CJUMP(EQ, right, 1, ok2,
		 * done) ok2 MOVE(t1,1) JUMP done done return t1
		 */
		setExp(node,
				new Ex(ESEQ(
						SEQ(SEQ(SEQ(
								SEQ(SEQ(MOVE(TEMP(t1), CONST(0)),
										CJUMP(compiler.tree.CJUMP.EQ, left,
												CONST(1), ok1, done)),
												SEQ(LABEL(ok1),
														CJUMP(compiler.tree.CJUMP.EQ,
																left, CONST(1), ok2,
																done))),
																SEQ(LABEL(ok2), MOVE(TEMP(t1), CONST(1)))),
																JUMP(done)), LABEL(done)), TEMP(t1))));
	}

	// Exp L, R;
	public void caseALtExp(ALtExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ALtExp <" + node + ">");
		node.getL().apply(this);
		node.getR().apply(this);

		Exp expl = getExp(node.getL());
		Exp expr = getExp(node.getL());
		Label T = new Label();
		Label F = new Label();
		Temp t = new Temp();
		setExp(node,
				new Ex(ESEQ(
						SEQ(SEQ(SEQ(
								MOVE(TEMP(t), CONST(0)),
								CJUMP(compiler.tree.CJUMP.LT, expl.unEx(),
										expr.unEx(), T, F)),
										SEQ(LABEL(T), MOVE(TEMP(t), CONST(1)))),
										LABEL(F)), TEMP(t))));
	}

	// Exp L, R;
	public void caseAPlusExp(APlusExp node) {
		if (debug)
			System.out.println("TranslateVisitor.APlusExp <" + node + ">");
		node.getL().apply(this);
		node.getR().apply(this);
		// BINOP (PLUS, Exp(L), Exp(R) )
		setExp(node,
				new Ex(BINOP(compiler.tree.BINOP.PLUS, getExp(node.getL())
						.unEx(), getExp(node.getR()).unEx())));
	}

	// Exp L, R;
	public void caseAMinusExp(AMinusExp node) {
		if (debug)
			System.out.println("TranslateVisitor.AMinusExp <" + node + ">");
		node.getL().apply(this);
		node.getR().apply(this);
		// BINOP (MINUS, Exp(L), Exp(R) )
		setExp(node,
				new Ex(BINOP(compiler.tree.BINOP.MINUS, getExp(node.getL())
						.unEx(), getExp(node.getR()).unEx())));
	}

	// Exp L, R;
	public void caseATimesExp(ATimesExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ATimesExp <" + node + ">");
		node.getL().apply(this);
		node.getR().apply(this);
		// BINOP (MUL, Exp(L), Exp(R) )
		setExp(node,
				new Ex(BINOP(compiler.tree.BINOP.MUL, getExp(node.getL())
						.unEx(), getExp(node.getR()).unEx())));
	}

	// Exp array, index;
	public void caseAArrayindexExp(AArrayindexExp node) {
		if (debug)
			System.out
			.println("TranslateVisitor.AArrayindexExp <" + node + ">");
		node.getArray().apply(this);
		node.getIndex().apply(this);

		Temp t_index = new Temp();
		Temp t_size = new Temp();
		compiler.tree.Exp e1 = getExp(node.getArray()).unEx();
		compiler.tree.Exp e2 = getExp(node.getIndex()).unEx();

		Label F = new Label();
		Label T = new Label();

		LinkedList argList = new LinkedList();
		compiler.tree.ExpList args = new compiler.tree.ExpList(argList);
		compiler.tree.Stm s1 = SEQ(
				SEQ(SEQ(SEQ(
						SEQ(MOVE(TEMP(t_index),
								BINOP(compiler.tree.BINOP.MUL, e2, CONST(4))),
								MOVE(TEMP(t_size), MEM(e1))),
								CJUMP(compiler.tree.CJUMP.GE, TEMP(t_index),
										TEMP(t_size), T, F)), LABEL(T)),
										MOVE(TEMP(new Temp()),
												CALL(NAME(new Label("_error")), args))),
												LABEL(F));

		Temp t = new Temp();
		compiler.tree.Stm s2 = SEQ(
				s1,
				MOVE(TEMP(t),
						MEM(BINOP(
								compiler.tree.BINOP.PLUS,
								e1,
								BINOP(compiler.tree.BINOP.PLUS,
										BINOP(compiler.tree.BINOP.MUL, e2,
												CONST(4)), CONST(4))))));
		setExp(node, new Ex(ESEQ(s2, TEMP(t))));
	}

	// Exp
	public void caseALengthExp(ALengthExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ALengthExp <" + node + ">");
		node.getExp().apply(this);
	}

	// Exp Id ExpList
	public void caseAMethodcallExp(AMethodcallExp node) {
		if (debug)
			System.out
			.println("TranslateVisitor.AMethodcallExp <" + node + ">");
		node.getExp().apply(this);
		node.getExpList().apply(this);

		String cname = getClass(node.getExp());
		String mname = node.getId().toString();

		Method calledMethod = symbolTable.getMethod(mname, cname);
		Node retType = calledMethod.Type();

		Node formal = null;
		Node actual = null;

		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Temp t3 = new Temp();
		int index = -1;
		Exp exp1 = getExp(node.getExp());

		LinkedList argList = new LinkedList();

		argList.add(TEMP(t3));

		if (((AExpList) node.getExpList()).getExp().size() != 0) {
			argList = ((compiler.tree.ExpList) (getExp((AExpList) node
					.getExpList())).unEx()).getList();
			argList.addFirst(TEMP(t3));
		}

		compiler.tree.ExpList args = new compiler.tree.ExpList(argList);

		compiler.tree.Stm s1 = MOVE(TEMP(t3), exp1.unEx());
		compiler.tree.Stm s2 = MOVE(TEMP(t1), MEM(TEMP(t3)));
		compiler.tree.Stm s3 = MOVE(TEMP(t2),
				MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1), CONST(index))));

		setExp(node, new Ex(CALL(ESEQ(SEQ(SEQ(s1, s2), s3), TEMP(t2)), args)));
	}

	// Exp
	public void caseAExpList(AExpList node) {
		if (debug)
			System.out.println("TranslateVisitor.AExpList <" + node + ">");
		LinkedList args = new LinkedList();
		Iterator i = node.getExp().iterator();

		while (i.hasNext()) {
			Node n = ((Node) i.next());
			n.apply(this);
			args.add(getExp(n).unEx());
		}
		setExp(node, new Ex(new compiler.tree.ExpList(args)));
	}

	// INTEGER_LITERAL
	public void caseAIntegerliteralExp(AIntegerliteralExp node) {
		if (debug)
			System.out.println("TranslateVisitor.AIntegerliteralExp <" + node
					+ ">");
		int value = Integer.parseInt(node.toString());
		setExp(node, new Ex(CONST(value)));
	}

	// true
	public void caseATrueliteralExp(ATrueliteralExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ATrueliteralExp <" + node
					+ ">");
		setExp(node, new Ex(CONST(1)));
	}

	// false
	public void caseAFalseliteralExp(AFalseliteralExp node) {
		if (debug)
			System.out.println("TranslateVisitor.AFalseliteralExp <" + node
					+ ">");
		setExp(node, new Ex(CONST(0)));
	}

	// id
	public void caseAIdExp(AIdExp node) {
		if (debug)
			System.out.println("TranslateVisitor.AIdExp <" + node + ">");

		SimpleExp se = currMethod.getVar(node.getId().toString()).getExp();

		/*
		 * if (se instanceof Temp) { setExp(node, new Ex(TEMP((Temp)(se))));
		 * return; } else
		 */
		if (se instanceof Offset) {
			setExp(node, new Ex(CONST(((Offset) se).value())));
			return;
		}
		setExp(node, null);
	}

	// this
	public void caseAThisExp(AThisExp node) {
		if (debug)
			System.out.println("TranslateVisitor.AThisExp <" + node + ">");
		// trim() is necessary because TId toString() adds a ' ' when string is
		// returned. getId() returns
		// the currClass as a blank terminated string, without the trim
		// toString() would return 2 ' '
		Temp z = new Temp(0);
		setExp(node, new Ex(TEMP(z)));
	}

	// Exp e;
	public void caseANewarrayExp(ANewarrayExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ANewarrayExp <" + node + ">");
		node.getExp().apply(this);

		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Label cj = new Label();
		Label F = new Label();
		Label T = new Label();

		Exp exp1 = getExp(node.getExp());

		compiler.tree.Exp size = BINOP(compiler.tree.BINOP.MUL,
				BINOP(compiler.tree.BINOP.PLUS, exp1.unEx(), CONST(1)),
				CONST(4));
		// 1. call _halloc get pointer to space allocated in t1
		LinkedList argList = new LinkedList();
		argList.add(size);

		compiler.tree.ExpList args = new compiler.tree.ExpList(argList);

		compiler.tree.Stm s1 = MOVE(TEMP(t1),
				CALL(NAME(new Label("_halloc")), args));

		// 2.Initialization
		compiler.tree.Stm s2 = SEQ(
				SEQ(SEQ(SEQ(
						SEQ(SEQ(MOVE(TEMP(t2), CONST(4)),
								SEQ(LABEL(cj),
										CJUMP(compiler.tree.CJUMP.LT, TEMP(t2),
												size, F, T))), LABEL(T)),
												MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1),
														TEMP(t2))), CONST(0))),
														MOVE(TEMP(t2),
																BINOP(compiler.tree.BINOP.PLUS, TEMP(t2),
																		CONST(4)))), JUMP(cj)),
																		SEQ(LABEL(F),
																				MOVE(MEM(TEMP(t1)),
																						BINOP(compiler.tree.BINOP.MUL, exp1.unEx(),
																								CONST(4)))));

		setExp(node, new Ex(ESEQ(SEQ(s1, s2), TEMP(t1))));
	}

	public void caseANewExp(ANewExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ANewExp <" + node + ">");
		node.getId().apply(this);
		// getId() returns a node, getText() returns the string Id. new A()
		// would have Id string of A
		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Temp t3 = new Temp();
		Label l1 = new Label();
		Label l2 = new Label();
		Label T = new Label();
		Label F = new Label();
		/*
		 * String cname= node.getId().getText(); Class c =
		 * symbolTable.getClass(cname) ;
		 */
		String[] met_labels = currClass.getMethodsIds();
		int sz_f = 10; // pick a number (c.GetAllocSizeF() + 1)*4;
		int sz_m = 10; // and another c.GetAllocSizeM() * 4;

		compiler.tree.Stm s4;
		if (sz_f > 4) {
			s4 = (SEQ(
					SEQ(SEQ(SEQ(
							SEQ(SEQ(SEQ(
									SEQ(MOVE(TEMP(t3), CONST(4)), LABEL(l1)),
									CJUMP(compiler.tree.CJUMP.LT, TEMP(t3),
											CONST(sz_f), T, F)), LABEL(T)),
											MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS,
													TEMP(t2), TEMP(t3))), CONST(0))),
													MOVE(TEMP(t3),
															BINOP(compiler.tree.BINOP.PLUS, TEMP(t3),
																	CONST(4)))), JUMP(l1)), LABEL(F)),
																	MOVE(MEM(TEMP(t2)), TEMP(t1))));
		} else {
			s4 = (MOVE(MEM(TEMP(t2)), TEMP(t1)));
		}

		compiler.tree.Stm s3;
		if (sz_m == 0) {

			s3 = MOVE(TEMP(new Temp()), CONST(0));
		} else if (sz_m == 4) {
			Label aux_label = new Label(met_labels[0]);
			s3 = MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1), CONST(0))),
					NAME(aux_label));
		} else if (sz_m == 8) {
			Label aux_label1 = new Label(met_labels[0]);
			Label aux_label2 = new Label(met_labels[0]);
			s3 = SEQ(
					MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1), CONST(0))),
							NAME(aux_label1)),
							MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1), CONST(4))),
									NAME(aux_label1)));
		} else {
			int j = 0;
			Label aux_label = new Label(met_labels[j]);
			s3 = MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1), CONST(0))),
					NAME(aux_label));

			for (int i = 4; i < sz_m; i = i + 4) {
				aux_label = new Label(met_labels[j]);
				s3 = SEQ(
						s3,
						MOVE(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1),
								CONST(i))), NAME(aux_label)));
				j++;
			}
		}

		LinkedList argList1 = new LinkedList();
		argList1.add(CONST(sz_m));
		LinkedList argList2 = new LinkedList();
		argList2.add(CONST(sz_f));

		compiler.tree.ExpList args1 = new compiler.tree.ExpList(argList1);
		compiler.tree.ExpList args2 = new compiler.tree.ExpList(argList2);

		compiler.tree.Stm s1 = MOVE(TEMP(t1),
				CALL(NAME(new Label("_halloc")), args1));
		compiler.tree.Stm s2 = MOVE(TEMP(t2),
				CALL(NAME(new Label("_halloc")), args2));

		setExp(node, new Ex(ESEQ(SEQ(SEQ(SEQ(s1, s2), s3), s4), TEMP(t2))));
	}

	// Exp e;
	public void caseANotExp(ANotExp node) {
		if (debug)
			System.out.println("TranslateVisitor.ANotExp <" + node + ">");
		node.getExp().apply(this);

		setExp(node,
				new Ex(BINOP(compiler.tree.BINOP.MINUS, CONST(1),
						getExp(node.getExp()).unEx())));
	}

	// Get type of node
	public Exp getExp(Node node) {
		return (Exp) getIn(node);
	}

	// Add Exp to node (this does not change the AST)
	public void setExp(Node node, Exp exp) {
		setIn(node, exp);
	}

	public String getClass(Node node) {
		if (node instanceof AThisExp)
			return currClass.getId();
		if (node instanceof AIdExp)
			return (symbolTable.getVarType(TranslateVisitor.currMethod,
					currClass, ((AIdExp) node).getId().toString())).toString();
		if (node instanceof AMethodcallExp)
			return symbolTable.getVarType(
					currMethod,
					currClass,
					((AIdExp) (((AMethodcallExp) node).getExp())).getId()
					.toString()).toString();
		return node.toString();
	}

	private static compiler.tree.Exp CONST(int value) {
		return new compiler.tree.CONST(value);
	}

	private static compiler.tree.Exp NAME(Label label) {
		return new compiler.tree.NAME(label);
	}

	private static compiler.tree.Exp TEMP(Temp temp) {
		return new compiler.tree.TEMP(temp);
	}

	private static compiler.tree.Exp BINOP(int binop, compiler.tree.Exp left,
			compiler.tree.Exp right) {
		return new compiler.tree.BINOP(binop, left, right);
	}

	private static compiler.tree.Exp MEM(compiler.tree.Exp exp) {
		return new compiler.tree.MEM(exp);
	}

	private static compiler.tree.Exp CALL(compiler.tree.Exp func,
			compiler.tree.ExpList args) {
		return new compiler.tree.CALL(func, args);
	}

	private static compiler.tree.Exp ESEQ(compiler.tree.Stm stm,
			compiler.tree.Exp exp) {
		if (stm == null)
			return exp;
		return new compiler.tree.ESEQ(stm, exp);
	}

	private static compiler.tree.Stm MOVE(compiler.tree.Exp dst,
			compiler.tree.Exp src) {
		return new compiler.tree.MOVE(dst, src);
	}

	private static compiler.tree.Stm EXP(compiler.tree.Exp exp) {
		return new compiler.tree.EXP1(exp);
	}

	private static compiler.tree.Stm JUMP(Label target) {
		return new compiler.tree.JUMP(target);
	}

	private static compiler.tree.Stm CJUMP(int relop, compiler.tree.Exp l,
			compiler.tree.Exp r, Label t, Label f) {
		return new compiler.tree.CJUMP(relop, l, r, t, f);
	}

	private static compiler.tree.Stm SEQ(compiler.tree.Stm left,
			compiler.tree.Stm right) {
		if (left == null)
			return right;
		if (right == null)
			return left;
		return new compiler.tree.SEQ(left, right);
	}

	private static compiler.tree.Stm LABEL(Label label) {
		return new compiler.tree.LABEL(label);
	}
}
