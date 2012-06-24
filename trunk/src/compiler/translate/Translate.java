package compiler.translate;

import compiler.temp.Temp;
import compiler.temp.Label;
import compiler.temp.Offset;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;

import compiler.frame.Frame;
import compiler.symbol.Symbol;

public class Translate extends DepthFirstAdapter {

	public Label exit = null;
	private Level topLevel;
	public Level level;

	public Translate(compiler.frame.Frame frame) {
		level = topLevel = new Level(frame);
	}

	public Level newLevel(compiler.symbol.Symbol name, boolean isLeaf,
			List formals) {
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

	/**
	 * Provides default methods which visit each node in the tree in depth-first
	 * order. Your visitors may extend this class.
	 */

	//
	// Auto class visitors--probably don't need to be overridden.
	//
	/*
	 * MJObj last_type = null; String last_name = null;
	 * 
	 * 
	 * public Exp visit(NodeList n, MJType argu) { Exp _ret=null; int _count=0;
	 * for ( Enumeration<Node> e = n.elements(); e.hasMoreElements(); ) {
	 * e.nextElement().accept(this,argu); _count++; } return _ret; }
	 * 
	 * public Exp visit(NodeListOptional n, MJType argu) { if ( n.present() ) {
	 * Exp _ret=null; int _count=0; for ( Enumeration<Node> e = n.elements();
	 * e.hasMoreElements(); ) { e.nextElement().accept(this,argu); _count++; }
	 * return _ret; } else return null; }
	 * 
	 * public Exp visit(NodeOptional n, MJType argu) { if ( n.present() ) return
	 * n.node.accept(this,argu); else return null; }
	 * 
	 * public Exp visit(NodeSequence n, MJType argu) { Exp _ret=null; int
	 * _count=0; for ( Enumeration<Node> e = n.elements(); e.hasMoreElements();
	 * ) { e.nextElement().accept(this,argu); _count++; } return _ret; }
	 * 
	 * public Exp visit(NodeToken n, MJType argu) { return null; }
	 */
	//
	// User-generated visitor methods below
	//

	/**
	 * f0 -> MainClass() f1 -> ( TypeDeclaration() )* f2 -> <EOF>
	 */
	public Exp visit(Goal n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		DataFrag frag = new DataFrag(topLevel.frame.programTail());
		frags.add(frag);
		return _ret;
	}

	/**
	 * f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> "public" f4 -> "static"
	 * f5 -> "void" f6 -> "main" f7 -> "(" f8 -> "String" f9 -> "[" f10 -> "]"
	 * f11 -> Identifier() f12 -> ")" f13 -> "{" f14 -> PrintStatement() f15 ->
	 * "}" f16 -> "}"
	 */
	public Exp visit(MainClass n, MJType argu) {
		Exp _ret = null;
		String class_name = (n.f1).f0.toString();
		MJObj m_obj = (MJObj) (((MJClasses) argu).SearchType(class_name));
		MJMethod main_met = (MJMethod) m_obj.SearchMethod("main");
		Symbol sym = compiler.symbol.symbol("main");
		Level oldlevel = level;
		LinkedList<Boolean> params = new LinkedList<Boolean>();
		level = newLevel(sym, false, params);
		procEntryExit(n.f14.accept(this, main_met));
		level = oldlevel;
		return _ret;
	}

	/**
	 * f0 -> ClassDeclaration() | ClassExtendsDeclaration()
	 */
	public Exp visit(TypeDeclaration n, MJType argu) {
		n.f0.accept(this, argu);
		return null;
	}

	/**
	 * f0 -> "class" f1 -> Identifier() f2 -> "{" f3 -> ( VarDeclaration() )* f4
	 * -> ( MethodDeclaration() )* f5 -> "}"
	 */
	public Exp visit(ClassDeclaration n, MJType argu) {
		Exp _ret = null;
		String class_name = (n.f1).f0.toString();
		MJObj m_obj = (MJObj) (((MJClasses) argu).SearchType(class_name));

		n.f3.accept(this, argu);
		n.f4.accept(this, m_obj);

		return _ret;
	}

	/**
	 * f0 -> "class" f1 -> Identifier() f2 -> "extends" f3 -> Identifier() f4 ->
	 * "{" f5 -> ( VarDeclaration() )* f6 -> ( MethodDeclaration() )* f7 -> "}"
	 */
	public Exp visit(ClassExtendsDeclaration n, MJType argu) {
		Exp _ret = null;
		String class_name = (n.f1).f0.toString();
		MJObj m_obj = (MJObj) (((MJClasses) argu).SearchType(class_name));

		n.f6.accept(this, m_obj);

		return _ret;
	}

	/**
	 * f0 -> Type() f1 -> Identifier() f2 -> ";"
	 */
	public Exp visit(VarDeclaration n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "public" f1 -> Type() f2 -> Identifier() f3 -> "(" f4 -> (
	 * FormalParameterList() )? f5 -> ")" f6 -> "{" f7 -> ( VarDeclaration() )*
	 * f8 -> ( Statement() )* f9 -> "return" f10 -> Expression() f11 -> ";" f12
	 * -> "}"
	 */
	public Exp visit(MethodDeclaration n, MJType argu) {

		Exp _ret = null;
		String class_name = ((MJObj) argu).GetClassName();
		String met_name = (n.f2).f0.toString();
		String met_label = class_name + "_" + met_name;
		int arg_number = 0;

		MJMethod m_met = (MJMethod) (((MJObj) argu).SearchMethod(met_name));
		arg_number = m_met.GetArgsNumber();

		Level oldlevel = level;
		LinkedList<Boolean> params = new LinkedList<Boolean>();
		Symbol sym = compiler.symbol.symbol(met_label);

		for (int i = 0; i <= arg_number; i++)
			params.add(new Boolean(false));

		level = newLevel(sym, false, params);

		compiler.tree.Stm stms;
		Enumeration e = n.f8.elements();

		// empty block
		if (!e.hasMoreElements()) {

			stms = null;
			Exp retexp = n.f10.accept(this, m_met);
			procEntryExit(new Ex(ESEQ(stms, retexp.unEx())));
			level = oldlevel;
			return _ret;
		}
		Exp exp = ((Statement) e.nextElement()).accept(this, m_met);

		// only one stm int block
		if (!e.hasMoreElements())
			stms = exp.unNx();
		stms = exp.unNx();

		// more than one stmt int block
		while (e.hasMoreElements()) {
			exp = ((Statement) e.nextElement()).accept(this, m_met);
			stms = SEQ(stms, exp.unNx());
		}

		Exp retexp = n.f10.accept(this, m_met);

		procEntryExit(new Ex(ESEQ(stms, retexp.unEx())));

		level = oldlevel;

		return _ret;
	}

	/**
	 * f0 -> FormalParameter() f1 -> ( FormalParameterRest() )*
	 */
	public Exp visit(FormalParameterList n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> Type() f1 -> Identifier()
	 */
	public Exp visit(FormalParameter n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "," f1 -> FormalParameter()
	 */
	public Exp visit(FormalParameterRest n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> ArrayType() | BooleanType() | IntegerType() | Identifier()
	 */
	public Exp visit(Type n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "int" f1 -> "[" f2 -> "]"
	 */
	public Exp visit(ArrayType n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "boolean"
	 */
	public Exp visit(BooleanType n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> "int"
	 */
	public Exp visit(IntegerType n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> Block() | AssignmentStatement() | ArrayAssignmentStatement() |
	 * IfStatement() | WhileStatement() | PrintStatement()
	 */
	public Exp visit(Statement n, MJType argu) {
		return n.f0.accept(this, argu);
	}

	/**
	 * f0 -> "{" f1 -> ( Statement() )* f2 -> "}"
	 */
	public Exp visit(Block n, MJType argu) {

		Enumeration e = n.f1.elements();

		// empty block
		if (!e.hasMoreElements())
			return new Nx(null);
		Exp exp = ((Statement) e.nextElement()).accept(this, argu);

		// only one stm int block
		if (!e.hasMoreElements())
			return exp;
		compiler.tree.Stm stm = exp.unNx();

		// more than one stmt int block
		while (e.hasMoreElements()) {
			exp = ((Statement) e.nextElement()).accept(this, argu);
			stm = SEQ(stm, exp.unNx());
		}
		return new Nx(stm);
	}

	/**
	 * f0 -> Identifier() f1 -> "=" f2 -> Expression() f3 -> ";"
	 */
	public Exp visit(AssignmentStatement n, MJType argu) {

		Exp e1 = n.f0.accept(this, argu);
		Exp e2 = n.f2.accept(this, argu);

		if (e1.unEx() instanceof compiler.tree.TEMP) {

			// System.out.println
			// ( ((compiler.tree.TEMP)e1.unEx()).temp.toString());

			return new Nx(MOVE(e1.unEx(), e2.unEx()));
		} else {
			Temp z = new Temp(0);
			return new Nx(MOVE(
					MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(z), e1.unEx())),
					e2.unEx()));
		}

	}

	/**
	 * f0 -> Identifier() f1 -> "[" f2 -> Expression() f3 -> "]" f4 -> "=" f5 ->
	 * Expression() f6 -> ";"
	 */
	public Exp visit(ArrayAssignmentStatement n, MJType argu) {
		Exp _ret = null;

		compiler.tree.Exp e1 = n.f0.accept(this, argu).unEx();

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

		compiler.tree.Exp e2 = n.f2.accept(this, argu).unEx();
		Temp t = new Temp();
		Temp t_index = new Temp();
		Temp t_size = new Temp();
		LinkedList<compiler.tree.Exp> args1 = new LinkedList<compiler.tree.Exp>();
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

		compiler.tree.Exp e3 = n.f5.accept(this, argu).unEx();

		return new Nx(MOVE(
				MEM(BINOP(compiler.tree.BINOP.PLUS, e1,
						BINOP(compiler.tree.BINOP.PLUS, e2, CONST(4)))), e3));
	}

	/**
	 * f0 -> "if" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement() f5 ->
	 * "else" f6 -> Statement()
	 */
	public Exp visit(IfStatement n, MJType argu) {
		Label T = new Label();
		Label F = new Label();
		Label D = new Label();
		Exp exp = n.f2.accept(this, argu);
		Exp stmT = n.f4.accept(this, argu);
		Exp stmF = n.f6.accept(this, argu);
		return new Nx(SEQ(
				SEQ(SEQ(SEQ(
						CJUMP(compiler.tree.CJUMP.EQ, exp.unEx(), CONST(1), T,
								F), SEQ(LABEL(T), stmT.unNx())), JUMP(D)),
								SEQ(LABEL(F), stmF.unNx())), LABEL(D)));
	}

	/**
	 * f0 -> "while" f1 -> "(" f2 -> Expression() f3 -> ")" f4 -> Statement()
	 */
	public Exp visit(WhileStatement n, MJType argu) {
		Label test = new Label();
		Label T = new Label();
		Label F = new Label();
		Exp exp = n.f2.accept(this, argu);
		Exp body = n.f4.accept(this, argu);

		return new Nx(SEQ(
				SEQ(SEQ(LABEL(test),
						(CJUMP(compiler.tree.CJUMP.EQ, exp.unEx(), CONST(1), T,
								F))), (SEQ(LABEL(T), body.unNx()))), LABEL(F)));
	}

	/**
	 * f0 -> "System.out.println" f1 -> "(" f2 -> Expression() f3 -> ")" f4 ->
	 * ";"
	 */
	public Exp visit(PrintStatement n, MJType argu) {
		Exp _ret = null;

		Exp e = n.f2.accept(this, argu);

		LinkedList<compiler.tree.Exp> args1 = new LinkedList<compiler.tree.Exp>();
		args1.add(e.unEx());

		return new Nx(MOVE(TEMP(new Temp()),
				CALL(NAME(new Label("_printint")), args1)));
	}

	/**
	 * f0 -> AndExpression() | CompareExpression() | PlusExpression() |
	 * MinusExpression() | TimesExpression() | ArrayLookup() | ArrayLength() |
	 * MessageSend() | PrimaryExpression()
	 */
	public Exp visit(Expression n, MJType argu) {
		Exp _ret = null;

		return n.f0.accept(this, argu);

	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "&&" f2 -> PrimaryExpression()
	 */
	public Exp visit(AndExpression n, MJType argu) {

		Temp t1 = new Temp();
		Label done = new Label();
		Label ok1 = new Label();
		Label ok2 = new Label();
		compiler.tree.Exp left = n.f0.accept(this, argu).unEx();
		compiler.tree.Exp riglt = n.f2.accept(this, argu).unEx();
		/*
		 * (left && right) eval left first then right
		 * 
		 * MOVE(t1,0) CJUMP(EQ, left,1,ok, done) ok1 CJUMP(EQ, right, 1, ok2,
		 * done) ok2 MOVE(t1,1) JUMP done done return t1
		 */
		return new Ex(
				ESEQ(SEQ(
						SEQ(SEQ(SEQ(
								SEQ(MOVE(TEMP(t1), CONST(0)),
										CJUMP(compiler.tree.CJUMP.EQ, left,
												CONST(1), ok1, done)),
												SEQ(LABEL(ok1),
														CJUMP(compiler.tree.CJUMP.EQ, left,
																CONST(1), ok2, done))),
																SEQ(LABEL(ok2), MOVE(TEMP(t1), CONST(1)))),
																JUMP(done)), LABEL(done)), TEMP(t1)));

	}

	/**
	 * f0 -> PrimaryExpression()
	 * f1 -> "<"
	 * f2 -> PrimaryExpression()
	 */
	public Exp visit(CompareExpression n, MJType argu) {
		Exp expl = n.f0.accept(this, argu);
		Exp expr = n.f2.accept(this, argu);
		Label T = new Label();
		Label F = new Label();
		Temp t = new Temp();
		return new Ex(
				ESEQ(SEQ(
						SEQ(SEQ(MOVE(TEMP(t), CONST(0)),
								CJUMP(compiler.tree.CJUMP.LT, expl.unEx(),
										expr.unEx(), T, F)),
										SEQ(LABEL(T), MOVE(TEMP(t), CONST(1)))),
										LABEL(F)), TEMP(t)));
	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "+" f2 -> PrimaryExpression()
	 */
	public Exp visit(PlusExpression n, MJType argu) {
		// BINOP (PLUS, Exp(f0), Exp(f2) )
		return new Ex(BINOP(compiler.tree.BINOP.PLUS,
				(n.f0.accept(this, argu)).unEx(),
				(n.f2.accept(this, argu)).unEx()));
	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "-" f2 -> PrimaryExpression()
	 */
	public Exp visit(MinusExpression n, MJType argu) {
		// BINOP (MINUS, Exp(f0), Exp(f2) )
		return new Ex(BINOP(compiler.tree.BINOP.MINUS,
				(n.f0.accept(this, argu)).unEx(),
				(n.f2.accept(this, argu)).unEx()));
	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "*" f2 -> PrimaryExpression()
	 */
	public Exp visit(TimesExpression n, MJType argu) {
		// BINOP (MUL, Exp(f0), Exp(f2) )
		return new Ex(BINOP(compiler.tree.BINOP.MUL,
				(n.f0.accept(this, argu)).unEx(),
				(n.f2.accept(this, argu)).unEx()));
	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "[" f2 -> PrimaryExpression() f3 -> "]"
	 */
	public Exp visit(ArrayLookup n, MJType argu) {

		Temp t_index = new Temp();
		Temp t_size = new Temp();
		compiler.tree.Exp e1 = n.f0.accept(this, argu).unEx();
		compiler.tree.Exp e2 = n.f2.accept(this, argu).unEx();

		Label F = new Label();
		Label T = new Label();

		LinkedList<compiler.tree.Exp> args1 = new LinkedList<compiler.tree.Exp>();

		compiler.tree.Stm s1 = SEQ(
				SEQ(SEQ(SEQ(
						SEQ(MOVE(TEMP(t_index),
								BINOP(compiler.tree.BINOP.MUL, e2, CONST(4))),
								MOVE(TEMP(t_size), MEM(e1))),
								CJUMP(compiler.tree.CJUMP.GE, TEMP(t_index),
										TEMP(t_size), T, F)), LABEL(T)),
										MOVE(TEMP(new Temp()),
												CALL(NAME(new Label("_error")), args1))),
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
		return new Ex(ESEQ(s2, TEMP(t)));
	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "." f2 -> "length"
	 */
	public Exp visit(ArrayLength n, MJType argu) {
		Exp _ret = null;
		n.f0.accept(this, argu);
		n.f1.accept(this, argu);
		n.f2.accept(this, argu);
		return _ret;
	}

	/**
	 * f0 -> PrimaryExpression() f1 -> "." f2 -> Identifier() f3 -> "(" f4 -> (
	 * ExpressionList() )? f5 -> ")"
	 */
	public Exp visit(MessageSend n, MJType argu) {
		Exp _ret = null;
		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Temp t3 = new Temp();
		int index = -1;
		Exp exp1 = n.f0.accept(this, argu);
		String m_name = (n.f2).f0.toString();
		MJType aux_type;
		if (last_name == null) {
			index = last_type.SearchMetInd(m_name) * 4;
			aux_type = ((MJMethod) last_type.SearchMethod(m_name)).GetRetType();
		} else {
			MJObj new_type = (MJObj) ((MJMethod) argu).SearchType(last_name);
			if (new_type == null)
				new_type = (MJObj) ((MJMethod) argu).SearchClass(last_name);
			index = new_type.SearchMetInd(m_name) * 4;
			aux_type = ((MJMethod) new_type.SearchMethod(m_name)).GetRetType();
		}

		LinkedList<compiler.tree.Exp> argslist = new LinkedList<compiler.tree.Exp>();
		argslist.add(TEMP(t3));
		if (n.f4.present()) {
			argslist = ((compiler.tree.ExpList) (n.f4.accept(this, argu).unEx()))
					.getList();
			argslist.addFirst(TEMP(t3));
		}

		last_name = null;
		if (aux_type instanceof MJObj)
			last_type = (MJObj) aux_type;

		compiler.tree.Stm s1 = MOVE(TEMP(t3), exp1.unEx());
		compiler.tree.Stm s2 = MOVE(TEMP(t1), MEM(TEMP(t3)));
		compiler.tree.Stm s3 = MOVE(TEMP(t2),
				MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(t1), CONST(index))));

		return new Ex(CALL(ESEQ(SEQ(SEQ(s1, s2), s3), TEMP(t2)), argslist));
	}

	/**
	 * f0 -> Expression() f1 -> ( ExpressionRest() )*
	 */
	public Exp visit(ExpressionList n, MJType argu) {
		Exp _ret = null;
		LinkedList<compiler.tree.Exp> args = new LinkedList<compiler.tree.Exp>();

		args.add(n.f0.accept(this, argu).unEx());

		for (int i = 0; i < n.f1.size(); i++)
			args.add(n.f1.elementAt(i).accept(this, argu).unEx());

		return new Ex(new compiler.tree.ExpList(args));
	}

	/**
	 * f0 -> "," f1 -> Expression()
	 */
	public Exp visit(ExpressionRest n, MJType argu) {
		Exp e = n.f1.accept(this, argu);
		return e;
	}

	/**
	 * f0 -> IntegerLiteral() | TrueLiteral() | FalseLiteral() | Identifier() |
	 * ThisExpression() | ArrayAllocationExpression() | AllocationExpression() |
	 * NotExpression() | BracketExpression()
	 */

	public Exp visit(PrimaryExpression n, MJType argu) {
		Exp _ret = n.f0.accept(this, argu);

		if (n.f0.which == 3) {
			compiler.tree.Exp e = _ret.unEx();
			if (e instanceof compiler.tree.CONST) {
				Temp z = new Temp(0);
				_ret = new Ex(MEM(BINOP(compiler.tree.BINOP.PLUS, TEMP(z), e)));
			}
		} else
			last_name = null;
		return _ret;
	}

	/**
	 * f0 -> <INTEGER_LITERAL>
	 */
	public Exp visit(IntegerLiteral n, MJType argu) {

		int value = Integer.parseInt(n.f0.tokenImage);
		/*
		 * System.out.println("INT :" + value); Exp e = new Ex(CONST(value));
		 * System.out.println(((compiler.tree.CONST)e.unEx()).value);
		 */
		// return e;
		return new Ex(CONST(value));
	}

	/**
	 * f0 -> "true"
	 */
	public Exp visit(TrueLiteral n, MJType argu) {
		return new Ex(CONST(1));
	}

	/**
	 * f0 -> "false"
	 */
	public Exp visit(FalseLiteral n, MJType argu) {
		return new Ex(CONST(0));
	}

	/**
	 * f0 -> <IDENTIFIER>
	 */
	public Exp visit(Identifier n, MJType argu) {
		last_name = n.f0.toString();

		if (argu instanceof MJMethod) {
			Temp.SimpleExp se = ((MJMethod) argu).SearchIdName(last_name);
			if (se instanceof Temp)
				return new Ex(TEMP((Temp) (se)));
			else if (se instanceof Offset)
				return new Ex(CONST(((Offset) se).value()));
		}

		return null;
	}

	/**
	 * f0 -> "this"
	 */
	public Exp visit(ThisExpression n, MJType argu) {

		last_name = ((MJObj) (((MJMethod) argu).GetClass())).GetClassName();
		Temp z = new Temp(0);
		return new Ex(TEMP(z));
	}

	/**
	 * f0 -> "new" f1 -> "int" f2 -> "[" f3 -> Expression() f4 -> "]"
	 */
	public Exp visit(ArrayAllocationExpression n, MJType argu) {

		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Label cj = new Label();
		Label F = new Label();
		Label T = new Label();

		Exp exp1 = n.f3.accept(this, argu);

		compiler.tree.Exp size = BINOP(compiler.tree.BINOP.MUL,
				BINOP(compiler.tree.BINOP.PLUS, exp1.unEx(), CONST(1)),
				CONST(4));
		// 1. call _halloc get pointer to space allocated in t1
		LinkedList<compiler.tree.Exp> args1 = new LinkedList<compiler.tree.Exp>();
		args1.add(size);
		compiler.tree.Stm s1 = MOVE(TEMP(t1),
				CALL(NAME(new Label("_halloc")), args1));

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

		return new Ex(ESEQ(SEQ(s1, s2), TEMP(t1)));
	}

	/**
	 * f0 -> "new" f1 -> Identifier() f2 -> "(" f3 -> ")"
	 */
	public Exp visit(AllocationExpression n, MJType argu) {
		Temp t1 = new Temp();
		Temp t2 = new Temp();
		Temp t3 = new Temp();
		Label l1 = new Label();
		Label l2 = new Label();
		Label T = new Label();
		Label F = new Label();
		String obj_name = (n.f1).f0.toString();
		MJObj m_obj = (MJObj) ((MJMethod) argu).SearchClass(obj_name);
		String[] met_labels = m_obj.GetMetLabels();
		int sz_f = (m_obj.GetAllocSizeF() + 1) * 4;
		int sz_m = m_obj.GetAllocSizeM() * 4;

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

		LinkedList<compiler.tree.Exp> args1 = new LinkedList<compiler.tree.Exp>();
		args1.add(CONST(sz_m));
		LinkedList<compiler.tree.Exp> args2 = new LinkedList<compiler.tree.Exp>();
		args2.add(CONST(sz_f));

		compiler.tree.Stm s1 = MOVE(TEMP(t1),
				CALL(NAME(new Label("_halloc")), args1));
		compiler.tree.Stm s2 = MOVE(TEMP(t2),
				CALL(NAME(new Label("_halloc")), args2));

		last_type = m_obj;
		return new Ex(ESEQ(SEQ(SEQ(SEQ(s1, s2), s3), s4), TEMP(t2)));
	}

	/**
	 * f0 -> "!" f1 -> Expression()
	 */
	public Exp visit(NotExpression n, MJType argu) {

		// BINOP (MINUS, 1, Exp(f1))
		return new Ex(BINOP(compiler.tree.BINOP.MINUS, CONST(1),
				(n.f1.accept(this, argu)).unEx()));

	}

	/**
	 * f0 -> "(" f1 -> Expression() f2 -> ")"
	 */
	public Exp visit(BracketExpression n, MJType argu) {
		Exp e = n.f1.accept(this, argu);
		return e;
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
			List<compiler.tree.Exp> args) {
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
		return new compiler.tree.EXP(exp);
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
