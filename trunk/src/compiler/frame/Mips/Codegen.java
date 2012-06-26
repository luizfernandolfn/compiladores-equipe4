package compiler.frame.Mips;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import compiler.assem.Instr;
import compiler.assem.InstrList;
import compiler.temp.Label;
import compiler.temp.LabelList;
import compiler.temp.Temp;
import compiler.temp.TempList;
import compiler.tree.BINOP;
import compiler.tree.CALL;
import compiler.tree.CJUMP;
import compiler.tree.CONST;
import compiler.tree.CodeVisitor;
import compiler.tree.Exp;
import compiler.tree.ExpList;
import compiler.tree.JUMP;
import compiler.tree.LABEL;
import compiler.tree.MEM;
import compiler.tree.MOVE;
import compiler.tree.NAME;
import compiler.tree.TEMP;
import compiler.assem.OPER;

public class Codegen implements CodeVisitor {

	private MipsFrame frame;
	private ListIterator<Instr> insns;

	public Codegen(MipsFrame f, ListIterator<Instr> i) {
		frame = f;
		insns = i;
	}

	private void emit(Instr inst) {
		insns.add(inst);
	}

	static compiler.assem.Instr OPER(String a, Temp[] d, Temp[] s,
			List<Label> j) {
		return new compiler.assem.OPER("\t" + a, d, s, j);
	}

	static compiler.assem.Instr OPER(String a, TempList d, TempList s) {
		return OPER(a, d, s, null);
	}

	static Instr MOVE(String a, Temp d, Temp s) {
		return new compiler.assem.MOVE("\t" + a, d, s);
	}

	private static compiler.tree.CONST CONST16(compiler.tree.Exp e) {

		if (e instanceof compiler.tree.CONST) {
			compiler.tree.CONST c = (compiler.tree.CONST) e;
			int value = c.value;

			if (value == (short) value)
				return c;
		}
		return null;
	}

	private static boolean immediate(compiler.tree.BINOP e) {
		compiler.tree.CONST left = CONST16(e.left);
		compiler.tree.CONST right = CONST16(e.right);

		if (left == null)
			return right != null;
		switch (e.binop) {
		case compiler.tree.BINOP.PLUS:
		case compiler.tree.BINOP.MUL:
		case compiler.tree.BINOP.AND:
		case compiler.tree.BINOP.OR:
		case compiler.tree.BINOP.XOR:
			if (right == null) {
				e.left = e.right;
				e.right = left;
			}
			return true;
		}
		return false;
	}

	public void visit(compiler.tree.MOVE s) {

		if (s.dst instanceof compiler.tree.MEM) {
			compiler.tree.MEM mem = (compiler.tree.MEM) s.dst;

			if (mem.exp instanceof compiler.tree.BINOP) {
				compiler.tree.BINOP b = (compiler.tree.BINOP) mem.exp;

				if (b.binop == compiler.tree.BINOP.PLUS && immediate(b)) {
					int right = ((compiler.tree.CONST) b.right).value;
					Temp left = (b.left instanceof compiler.tree.TEMP) ? ((compiler.tree.TEMP) b.left).temp
							: b.left.accept(this);
					String off = Integer.toString(right);
					if (left == frame.FP) {
						left = frame.SP;
						off += "+" + frame.name + "_framesize";
					}

					emit(OPER("sw `s0 " + off + "(`s1)", null, new TempList(
							s.src.accept(this), new TempList(left, null))));
					return;
				}
			}

			compiler.tree.CONST exp = CONST16(mem.exp);
			if (exp != null) {
				emit(OPER("sw `s0 " + exp.value + "(`s1)", null, new TempList(
						s.src.accept(this), new TempList(frame.ZERO, null))));
				return;
			}

			if (mem.exp instanceof compiler.tree.TEMP) {
				Temp temp = ((compiler.tree.TEMP) mem.exp).temp;
				if (temp == frame.FP) {
					emit(OPER("sw `s0 " + frame.name + "_framesize" + "(`s1)",
							null, new TempList(s.src.accept(this),
									new TempList(frame.SP, null))));
					return;
				}
			}

			emit(OPER("sw `s0 (`s1)", null, new TempList(s.src.accept(this),
					new TempList(mem.exp.accept(this), null))));
			return;
		}

		// From here on dst must be a TEMP
		Temp dst = ((compiler.tree.TEMP) s.dst).temp;

		// MOVE(TEMP, MEM)
		if (s.src instanceof compiler.tree.MEM) {
			compiler.tree.MEM mem = (compiler.tree.MEM) s.src;

			// MOVE(TEMP, MEM(+ Exp CONST))
			if (mem.exp instanceof compiler.tree.BINOP) {
				compiler.tree.BINOP b = (compiler.tree.BINOP) mem.exp;

				if (b.binop == compiler.tree.BINOP.PLUS && immediate(b)) {
					int right = ((compiler.tree.CONST) b.right).value;
					Temp left = (b.left instanceof compiler.tree.TEMP) ? ((compiler.tree.TEMP) b.left).temp
							: b.left.accept(this);
					String off = Integer.toString(right);

					if (left == frame.FP) {
						left = frame.SP;
						off += "+" + frame.name + "_framesize";
					}
					emit(OPER("lw `d0 " + off + "(`s0)",
							new TempList(dst, null), new TempList(left, null)));
					return;
				}
			}

			// MOVE(TEMP, MEM(CONST))
			compiler.tree.CONST exp = CONST16(mem.exp);
			if (exp != null) {
				emit(OPER("lw `d0 " + exp.value + "(`s0)", new TempList(dst,
						null), new TempList(frame.ZERO, null)));
				return;
			}

			// MOVE(TEMP, MEM(TEMP))
			if (mem.exp instanceof compiler.tree.TEMP) {
				Temp temp = ((compiler.tree.TEMP) mem.exp).temp;
				if (temp == frame.FP) {
					emit(OPER("lw `d0 " + frame.name + "_framesize" + "(`s0)",
							new TempList(dst, null), new TempList(frame.SP,
									null)));
					return;
				}
			}

			// MOVE(TEMP, MEM(Exp))
			emit(OPER("lw `d0 (`s0)", new TempList(dst, null), new TempList(
					mem.exp.accept(this), null)));
			return;
		}

		// MOVE(TEMP, Exp)
		emit(MOVE("move `d0 `s0", dst, s.src.accept(this)));
	}

	public void visit(compiler.tree.EXPE s) {
		s.exp.accept(this);
	}

	public void visit(compiler.tree.JUMP s) {

		if (s.exp instanceof compiler.tree.NAME) {
			compiler.tree.NAME name = (compiler.tree.NAME) s.exp;
			emit(OPER("b " + name.label.toString(), null, null, s.targets));
			return;
		}
		emit(OPER("jr `s0", null, new TempList(s.exp.accept(this), null),
				s.targets));
		return;
	}

	private static boolean immediate(compiler.tree.CJUMP s) {
		compiler.tree.CONST left = CONST16(s.left);
		compiler.tree.CONST right = CONST16(s.right);

		if (left == null)
			return right != null;

		if (right == null) {
			s.left = s.right;
			s.right = left;
			switch (s.relop) {
			case compiler.tree.CJUMP.EQ:
			case compiler.tree.CJUMP.NE:
				break;
			case compiler.tree.CJUMP.LT:
				s.relop = compiler.tree.CJUMP.GT;
				break;
			case compiler.tree.CJUMP.GE:
				s.relop = compiler.tree.CJUMP.LE;
				break;
			case compiler.tree.CJUMP.GT:
				s.relop = compiler.tree.CJUMP.LT;
				break;
			case compiler.tree.CJUMP.LE:
				s.relop = compiler.tree.CJUMP.GE;
				break;
			case compiler.tree.CJUMP.ULT:
				s.relop = compiler.tree.CJUMP.UGT;
				break;
			case compiler.tree.CJUMP.UGE:
				s.relop = compiler.tree.CJUMP.ULE;
				break;
			case compiler.tree.CJUMP.UGT:
				s.relop = compiler.tree.CJUMP.ULT;
				break;
			case compiler.tree.CJUMP.ULE:
				s.relop = compiler.tree.CJUMP.UGE;
				break;
			default:
				throw new Error("bad relop in Codegen.immediate");
			}
		}
		return true;
	}

	private static String[] CJUMP = new String[10];

	static {
		CJUMP[compiler.tree.CJUMP.EQ] = "beq";
		CJUMP[compiler.tree.CJUMP.NE] = "bne";
		CJUMP[compiler.tree.CJUMP.LT] = "blt";
		CJUMP[compiler.tree.CJUMP.GT] = "bgt";
		CJUMP[compiler.tree.CJUMP.LE] = "ble";
		CJUMP[compiler.tree.CJUMP.GE] = "bge";
		CJUMP[compiler.tree.CJUMP.ULT] = "bltu";
		CJUMP[compiler.tree.CJUMP.ULE] = "bleu";
		CJUMP[compiler.tree.CJUMP.UGT] = "bgtu";
		CJUMP[compiler.tree.CJUMP.UGE] = "bgeu";
	}

	public void visit(compiler.tree.CJUMP s) {

		LabelList targets = null;
		// List<Label> targets = new LinkedList<Label>();
		targets = new LabelList(s.iftrue, targets);
		// targets.add(s.iftrue);
		targets = new LabelList(s.iffalse, targets);
		// targets.add(s.iffalse);

		if (immediate(s)) {
			int right = ((compiler.tree.CONST) s.right).value;
			// CJUMP(op, Exp, CONST, Label, Label)
			emit(OPER(
					CJUMP[s.relop] + " `s0 " + right + " "
							+ s.iftrue.toString(), null,
							new TempList(s.left.accept(this), null), targets));
			return;
		}

		// CJUMP(op, Exp, Exp, Label, Label)
		emit(OPER(
				CJUMP[s.relop] + " `s0 `s1 " + s.iftrue.toString(),
				null,
				new TempList(s.left.accept(this), new TempList(s.right
						.accept(this), null)), targets));
		return;
	}

	public void visit(compiler.tree.LABEL l) {
		emit(new compiler.assem.LABEL(l.label.toString() + ":", l.label));
		return;
	}

	public Temp visit(compiler.tree.CONST e) {
		if (e.value == 0)
			return frame.ZERO;
		Temp t = new Temp();
		emit(OPER("li `d0 " + e.value, new TempList(t, null), null));
		return t;
	}

	public Temp visit(compiler.tree.NAME e) {
		Temp t = new Temp();
		emit(OPER("la `d0 " + e.label.toString(), new TempList(t, null), null));
		return t;
	}

	public Temp visit(compiler.tree.TEMP e) {
		if (e.temp == frame.FP) {
			Temp t = new Temp();
			emit(OPER("addu `d0 `s0 " + frame.name + "_framesize",
					new TempList(t, null), new TempList(frame.SP, null)));
			return t;
		}
		return e.temp;
	}

	private static String[] BINOP = new String[10];
	static {
		BINOP[compiler.tree.BINOP.PLUS] = "add";
		BINOP[compiler.tree.BINOP.MINUS] = "sub";
		BINOP[compiler.tree.BINOP.MUL] = "mulo";
		BINOP[compiler.tree.BINOP.DIV] = "div";
		BINOP[compiler.tree.BINOP.AND] = "and";
		BINOP[compiler.tree.BINOP.OR] = "or";
		BINOP[compiler.tree.BINOP.LSHIFT] = "sll";
		BINOP[compiler.tree.BINOP.RSHIFT] = "srl";
		BINOP[compiler.tree.BINOP.ARSHIFT] = "sra";
		BINOP[compiler.tree.BINOP.XOR] = "xor";
	}

	private static int shift(int i) {
		int shift = 0;
		if ((i >= 2) && ((i & (i - 1)) == 0)) {
			while (i > 1) {
				shift += 1;
				i >>= 1;
			}
		}
		return shift;
	}

	public Temp visit(compiler.tree.BINOP e) {
		Temp t = new Temp();
		if (immediate(e)) {
			int right = ((compiler.tree.CONST) e.right).value;
			switch (e.binop) {
			case compiler.tree.BINOP.PLUS: {
				Temp left = (e.left instanceof compiler.tree.TEMP) ? ((compiler.tree.TEMP) e.left).temp
						: e.left.accept(this);
				String off = Integer.toString(right);
				if (left == frame.FP) {
					left = frame.SP;
					off += "+" + frame.name + "_framesize";
				}
				emit(OPER("add `d0 `s0 " + off, new TempList(t, null),
						new TempList(left, null)));
				return t;
			}
			case compiler.tree.BINOP.MUL: {
				int shift = shift(right);
				if (shift != 0) {
					emit(OPER("sll `d0 `s0 " + shift, new TempList(t, null),
							new TempList(e.left.accept(this), null)));
					return t;
				}
				emit(OPER(BINOP[e.binop] + " `d0 `s0 " + right, new TempList(t,
						null), new TempList(e.left.accept(this), null)));
				return t;
			}
			case compiler.tree.BINOP.DIV: {
				int shift = shift(right);
				if (shift != 0) {
					emit(OPER("sra `d0 `s0 " + shift, new TempList(t, null),
							new TempList(e.left.accept(this), null)));
					return t;
				}
				emit(OPER(BINOP[e.binop] + " `d0 `s0 " + right, new TempList(t,
						null), new TempList(e.left.accept(this),null)));
				return t;
			}
			default:
				emit(OPER(BINOP[e.binop] + " `d0 `s0 " + right, new TempList(t,
						null), new TempList(e.left.accept(this),null)));
				return t;
			}
		}
		emit(OPER(BINOP[e.binop] + " `d0 `s0 `s1", new TempList(t, null),
				new TempList(e.left.accept(this),new TempList(e.right.accept(this),null))));
		return t;
	}

	public Temp visit(compiler.tree.MEM e) {
		Temp t = new Temp();

		// MEM(+ Exp CONST)
		if (e.exp instanceof compiler.tree.BINOP) {
			compiler.tree.BINOP b = (compiler.tree.BINOP) e.exp;

			if (b.binop == compiler.tree.BINOP.PLUS && immediate(b)) {
				int right = ((compiler.tree.CONST) b.right).value;
				Temp left = (b.left instanceof compiler.tree.TEMP) ? ((compiler.tree.TEMP) b.left).temp
						: b.left.accept(this);
				String off = Integer.toString(right);
				if (left == frame.FP) {
					left = frame.SP;
					off += "+" + frame.name + "_framesize";
				}
				emit(OPER("lw `d0 " + off + "(`s0)", new TempList(t, null),
						new TempList(left,null)));
				return t;
			}
		}

		// MEM(CONST)
		compiler.tree.CONST exp = CONST16(e.exp);

		if (exp != null) {
			emit(OPER("lw `d0 " + exp.value + "(`s0)", new TempList(t, null),
					new TempList(frame.ZERO,null)));
			return t;
		}

		// MEM(TEMP)
		if (e.exp instanceof compiler.tree.TEMP) {
			Temp temp = ((compiler.tree.TEMP) e.exp).temp;
			if (temp == frame.FP) {
				emit(OPER("lw `d0 " + frame.name + "_framesize" + "(`s0)",
						new TempList(t, null), new TempList(frame.SP,null)));
				return t;
			}
		}

		// MEM(Exp)
		emit(OPER("lw `d0 (`s0)", new TempList(t, null),
				new TempList(e.exp.accept(this),null)));
		return t;
	}

	public Temp visit(compiler.tree.CALL s) {
		Iterator<compiler.tree.Exp> args = s.args.iterator();

		LinkedList<Temp> uses = new LinkedList<Temp>();

		Temp V0 = args.next().accept(this);
		if (V0 != frame.ZERO) {
			emit(MOVE("move `d0 `s0", frame.V0, V0));
			uses.add(frame.V0);
		}

		int offset = 0;

		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0 `s0", frame.A0, args.next().accept(this)));
			uses.add(frame.A0);
		}
		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0 `s0", frame.A1, args.next().accept(this)));
			uses.add(frame.A1);
		}
		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0 `s0", frame.A2, args.next().accept(this)));
			uses.add(frame.A2);
		}
		if (args.hasNext()) {
			offset += frame.wordSize();
			emit(MOVE("move `d0 `s0", frame.A3, args.next().accept(this)));
			uses.add(frame.A3);
		}
		while (args.hasNext()) {
			offset += frame.wordSize();
			emit(OPER("sw `s0 " + offset + "(`s1)", null, new Temp[] { args.next().accept(this), frame.SP }));
		}

		if (offset > frame.maxArgOffset)
			frame.maxArgOffset = offset;

		if (s.func instanceof compiler.tree.NAME) {
			emit(OPER("jal " + ((compiler.tree.NAME) s.func).label.toString(),
					frame.calldefs, uses.toArray(new Temp[] {})));
			return frame.V0;
		}
		uses.addFirst(s.func.accept(this));
		emit(OPER("jal `s0", frame.calldefs, uses.toArray(new Temp[] {})));
		return frame.V0;
	}

	public void visit(compiler.tree.SEQ n) {
		throw new Error();
	}

	public Temp visit(compiler.tree.ESEQ n) {
		throw new Error();
	}
}
