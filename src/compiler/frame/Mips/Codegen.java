package compiler.frame.Mips;

import compiler.assem.Instr;
import compiler.assem.InstrList;
import compiler.temp.Label;
import compiler.temp.LabelList;
import compiler.temp.TempList;
import compiler.tree.BINOP;
import compiler.tree.CALL;
import compiler.tree.CJUMP;
import compiler.tree.CONST;
import compiler.tree.ExpStm;
import compiler.tree.Exp;
import compiler.tree.ExpList;
import compiler.tree.JUMP;
import compiler.tree.LABEL;
import compiler.tree.MEM;
import compiler.tree.MOVE;
import compiler.tree.NAME;
import compiler.tree.TEMP;

public class Codegen{
	
	private InstrList ilist = null, last = null;
	MipsFrame frame;

	public Codegen(MipsFrame f)
	{
		frame = f;
	}

	private void emit(Instr inst)
	{
		if (last != null)
			last = last.tail = new InstrList(inst, null);
		else
			last = ilist = new InstrList(inst, null);
	}

	public InstrList codegen(compiler.tree.Stm s)
	{
		InstrList l;
		munchStm(s);
		l = ilist;
		ilist = last = null;
		return l;
	}

	/*
	 * Trata casos de operacoes aritmeticas do tipo (binop, left, right)
	 */
	private temp.Temp munchBinop(Exp left, Exp right, int binop)
	{
		compiler.temp.Temp result = new compiler.temp.Temp();
		compiler.temp.Temp templeft;
		compiler.temp.Temp tempright;

		switch (binop)
		{
			case BINOP.PLUS :
				if (right instanceof CONST) // binop(+, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"addiu `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else if (left instanceof CONST) // binop(+, const, exp)
				{
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"addiu `d0, `s0, " + ((CONST)left).value + "\n",
							new TempList(result, null),
							new TempList(tempright, null)));
				}
				else
				{ // binop(+, e1, e2)
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"addu `d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;

			case BINOP.MUL : // binop(*, e1, e2)
				templeft = munchExp(left);
				tempright = munchExp(right);
				emit(
					new compiler.assem.OPER(
						"mul `d0, `s0,  `s1\n",
						new TempList(result, null),
						new TempList(templeft, new TempList(tempright, null))));
				break;

			case BINOP.DIV : // binop(/, e1, e2)		    
				templeft = munchExp(left);
				tempright = munchExp(right);
				emit(
					new compiler.assem.OPER(
						"divu `d0, `s0,  `s1\n",
						new TempList(result, null),
						new TempList(templeft, new TempList(tempright, null))));
				break;

			case BINOP.MINUS : //			binop(-, e1, e2)
				templeft = munchExp(left);
				tempright = munchExp(right);
				emit(
					new compiler.assem.OPER(
						"subu `d0, `s0, `s1\n",
						new TempList(result, null),
						new TempList(templeft, new TempList(tempright, null))));
				break;

			case BINOP.AND :
				if (right instanceof CONST) // binop(and, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"andi `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else
				{ // binop(and, e1, e2)
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"and`d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;
			case BINOP.OR :
				if (right instanceof CONST) //binop(or, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"ori `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else
				{ // binop(or, e1, e2)
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"or `d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;
			case BINOP.XOR :
				if (right instanceof CONST) //	binop(xor, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"xori `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else
				{ //binop(xor, e1, e2) 
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"xor `d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;
			case BINOP.LSHIFT :
				if (right instanceof CONST) //binop(lshift, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"sll `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else
				{ //binop(lshift, e1, e2) 
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"sllv `d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;
			case BINOP.RSHIFT :
				if (right instanceof CONST) //binop(rshift, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"slr `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else
				{
					//binop(rshift, e1, e2) 
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"slrv `d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;
			case BINOP.ARSHIFT :
				if (right instanceof CONST) //binop(arshift, e1, const)
				{
					templeft = munchExp(left);
					emit(
						new compiler.assem.OPER(
							"sra `d0, `s0, " + ((CONST)right).value + "\n",
							new TempList(result, null),
							new TempList(templeft, null)));
				}
				else
				{ //binop(arshift, e1, e2) 
					templeft = munchExp(left);
					tempright = munchExp(right);
					emit(
						new compiler.assem.OPER(
							"srav `d0, `s0, `s1\n",
							new TempList(result, null),
							new TempList(templeft, new TempList(tempright, null))));
				}
				break;
		}

		return result;
	}

	/* 
	 * Carrega valores da memoria para registradores 
	 */
	private compiler.temp.Temp munchMem(Exp address)
	{
		compiler.temp.Temp result = new compiler.temp.Temp();

		if (address instanceof BINOP
			&& ((BINOP)address).left instanceof CONST
			&& ((BINOP)address).right instanceof Exp
			&& ((BINOP)address).binop == BINOP.PLUS)
		{
			// mem(binop(+, const, exp))
			compiler.temp.Temp temp = munchExp(((BINOP)address).right);
			emit(
				new assem.OPER(
					"lw `d0, " + ((CONST) ((BINOP)address).left).value + "(`s0)\n",
					new TempList(result, null),
					new TempList(temp, null)));
		}
		else if (
			address instanceof BINOP
				&& ((BINOP)address).right instanceof CONST
				&& ((BINOP)address).left instanceof Exp
				&& ((BINOP)address).binop == BINOP.PLUS)
		{
			//		mem(binop(+, exp, const))
			compiler.temp.Temp temp = munchExp(((BINOP)address).left);
			emit(
				new compiler.assem.OPER(
					"lw `d0, " + ((CONST) ((BINOP)address).right).value + "(`s0)\n",
					new TempList(result, null),
					new TempList(temp, null)));
		}
		else if (address instanceof CONST)
		{
			//		mem(const)
			emit(
				new compiler.assem.OPER(
					"lw `d0, " + ((CONST)address).value + "\n",
					new TempList(result, null),
					null));
		}
		else if (address instanceof NAME)
		{
			//		mem(NAME)
			emit(
				new compiler.assem.OPER(
					"lw `d0, " + ((NAME)address).toString() + "\n",
					new TempList(result, null),
					null));
		}
		else
		{
			// mem(Exp)
			temp.Temp temp = munchExp(address);
			emit(new compiler.assem.OPER("lw `d0, (`s0)\n", new TempList(result, null), new TempList(temp, null)));
		}
		return result;
	}

	/* 
	 * Armazena valores do registrador src no endereco dst 
	 */
	private void munchMove(Exp dst, Exp src)
	{
		compiler.temp.Temp srcreg = munchExp(src);

		//move(temp, srcreg)
		if (dst instanceof TEMP)
		{
			compiler.temp.Temp dstreg = ((TEMP)dst).temp;
			emit(new compiler.assem.MOVE("move `d0, `s0\n", dstreg, srcreg));
		}
		else
		{ //move(mem, src)
			if (dst instanceof MEM)
			{
				if (((MEM)dst).exp instanceof BINOP
					&& ((BINOP) ((MEM)dst).exp).binop == BINOP.PLUS
					&& ((BINOP) ((MEM)dst).exp).left instanceof CONST)
				{
					//move(mem(binop(+, const, exp)), srcreg)
					BINOP binop = ((BINOP) ((MEM)dst).exp);
					compiler.temp.Temp dstreg = munchExp(binop.right);
					emit(
						new compiler.assem.OPER(
							"sw `s0," + ((CONST)binop.left).value + "(`s1)\n",
							null,
							new TempList(srcreg, new TempList(dstreg, null))));
				}
				else if (
					((MEM)dst).exp instanceof BINOP
						&& ((BINOP) ((MEM)dst).exp).binop == BINOP.PLUS
						&& ((BINOP) ((MEM)dst).exp).right instanceof CONST)
				{
					//move(mem(binop(+, exp, const)), srcreg)
					BINOP binop = ((BINOP) ((MEM)dst).exp);
					compiler.temp.Temp dstreg = munchExp(binop.left);
					emit(
						new compiler.assem.OPER(
							"sw `s0," + ((CONST)binop.right).value + "(`s1)\n",
							null,
							new TempList(srcreg, new TempList(dstreg, null))));
				}
				else if (((MEM)dst).exp instanceof CONST)
				{
					//move( mem(const), srcreg)
					emit(
						new compiler.assem.OPER(
							"sw `s0, " + ((CONST)dst).value + "\n",
							null,
							new TempList(srcreg, null)));
				}
				else //move(mem, srcreg)
					{
					compiler.temp.Temp dstreg = munchExp(((MEM)dst).exp);
					emit(
						new compiler.assem.OPER(
							"sw `s0, (`s1)\n",
							null,
							new TempList(srcreg, new TempList(dstreg, null))));
				}
			}
			else if (dst instanceof NAME)
			{
				//					move( mem(const), srcreg)
				emit(
					new compiler.assem.OPER(
						"sw `s0, " + ((NAME)dst).toString() + "\n",
						null,
						new TempList(srcreg, null)));
			}
			else
			{
				//move(dstreg, srcreg)
				compiler.temp.Temp dstreg = munchExp(dst);
				emit(
					new compiler.assem.OPER(
						"sw `s0, (`s1)\n",
						null,
						new TempList(srcreg, new TempList(dstreg, null))));
			}
		}
	}

	/*
	 * Gera um label no codigo 
	 */
	private void munchLabel(Label label)
	{
		emit(new compiler.assem.LABEL(label.toString() + ":\n", label));
	}

	/*
	 * Gera um jump incondicional no codigo
	 */
	private void munchJump(Exp e, LabelList targets)
	{
		if (e instanceof NAME)
			emit(new compiler.assem.OPER("j " + ((NAME)e).label.toString() + "\n", null, null, targets));
		else
			emit(new compiler.assem.OPER("j " + targets.head.toString() + "\n", null, null, targets));
	}

	/*
	 * Gera um jump condicional do tipo 
	 * if (left op right = true) goto t else goto f
	 */
	private void munchCJump(Exp left, Exp right, Label t, Label f, int op)
	{
		compiler.temp.Temp l = munchExp(left);
		compiler.temp.Temp r = munchExp(right);

		compiler.temp.TempList srcreg = new temp.TempList(l, new TempList(r, null));
		compiler.temp.LabelList labels = new temp.LabelList(t, new temp.LabelList(f, null));

		switch (op)
		{
			case CJUMP.EQ :
				emit(new compiler.assem.OPER("beq `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.GE :
				emit(new compiler.assem.OPER("bge `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.GT :
				emit(new compiler.assem.OPER("bgt `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.LE :
				emit(new compiler.assem.OPER("ble `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.LT :
				emit(new compiler.assem.OPER("blt `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.NE :
				emit(new compiler.assem.OPER("bne `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.UGE :
				emit(new compiler.assem.OPER("bgeu `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.UGT :
				emit(new compiler.assem.OPER("bgtu `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.ULE :
				emit(new compiler.assem.OPER("bleu `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
			case CJUMP.ULT :
				emit(new compiler.assem.OPER("bltu `s0, `s1," + t.toString() + "\n", null, srcreg, labels));
				break;
		}

	}

	/*
	 * Processa uma constante e retorna seu valor em um registrador
	 */
	private compiler.temp.Temp munchConst(CONST c)
	{
		compiler.temp.Temp result = new compiler.temp.Temp();
		emit(new compiler.assem.OPER("addiu `d0, $zero, " + c.value + "\n", new TempList(result, null), null));

		return result;
	}

	/*
	 * Processa todos os argumentos recursivamente, coloca na pilha
	 * e atualiza o stack pointer atual
	 */
	private compiler.temp.TempList munchArgs(int index, ExpList args)
	{
		// Processa o argumento corrente
		compiler.temp.Temp temp = munchExp(args.head);
		compiler.temp.TempList l = null;

		// Se ainda existirem argumentos, processa
		if (args.tail != null)
			l = munchArgs(index + 1, args.tail);

		emit(new compiler.assem.OPER("sw, `d0, " + 4 * index + "($sp)\n", new TempList(temp, null), null));

		return new compiler.temp.TempList(temp, l);
	}

	private String callerSaveRegs()
	{
		int offset = 0;
		StringBuffer temp = new StringBuffer("");

		// Aloca espaco para salvar regs.
		temp.append("subu $sp, $sp, " + 10 * frame.wordSize() + "\n");

		// Callee salvando registradores s.
		for (int i = 0; i <= 9; i++)
		{
			temp.append("sw $t" + i + ", " + offset + "($sp)\n");
			offset += frame.wordSize();
		}

		return temp.toString();
	}

	private String loadRegs()
	{
		int offset = 9 * frame.wordSize(); // # regs * word a partir do ultimo.
		StringBuffer temp = new StringBuffer("");

		//		Callee carregando registradores s.
		for (int i = 9; i >= 0; i--)
		{
			temp.append("lw $t" + i + ", " + offset + "($sp)\n");
			offset -= frame.wordSize();
		}

		//		Desaloca espaco de regs salvos.
		temp.append("addiu $sp, $sp, " + 10 * frame.wordSize() + "\n");

		return temp.toString();
	}
	/*
	 * Cria codigo para chamada de funcao
	 */
	private compiler.temp.Temp munchCall(Exp func, ExpList args)
	{
		int argc = 0;

		if (args != null)
		{

			argc++;
			//Conta o numero de argumentos
			for (ExpList l = args; l.tail != null; l = l.tail)
				argc++;

			emit(new compiler.assem.OPER(callerSaveRegs(), null, null));

			//Atualiza sp para "alocar" memoria
			emit(new compiler.assem.OPER("subu $sp, $sp, " + 4 * argc + "\n", null, null));

			// Processa argumentos
			compiler.temp.TempList tl = munchArgs(0, args);

			//Chama a funcao
			emit(new compiler.assem.OPER("jal __" + ((NAME)func).label.toString() + "\n", null, tl));

			//"Libera" memoria dos formais da chamada
			emit(new compiler.assem.OPER("addiu $sp, $sp, " + 4 * argc + "\n", null, null));
			
			emit(new compiler.assem.OPER(loadRegs(), null, null));
		}
		else
		{
			emit(new compiler.assem.OPER(callerSaveRegs(), null, null));
			emit(new compiler.assem.OPER("jal " + ((NAME)func).label.toString() + "\n", null, null));
			emit(new compiler.assem.OPER(loadRegs(), null, null));
		}
		return frame.RV();
	}

	private void munchStm(compiler.tree.Stm s)
	{
		if (s instanceof MOVE)
			munchMove(((MOVE)s).dst, (((MOVE)s).src));
		if (s instanceof CJUMP)
			munchCJump(
				((CJUMP)s).left,
				((CJUMP)s).right,
				((CJUMP)s).iftrue,
				((CJUMP)s).iffalse,
				((CJUMP)s).relop);
		if (s instanceof JUMP)
			munchJump(((JUMP)s).exp, ((JUMP)s).targets);
		if (s instanceof LABEL)
			munchLabel(((LABEL)s).label);
		if (s instanceof ExpStm)
			munchExp(((ExpStm)s).exp);
	}

	private compiler.temp.Temp munchExp(tree.Exp s)
	{
		if (s instanceof MEM)
			return munchMem(((MEM)s).exp);
		if (s instanceof BINOP)
			return munchBinop(((BINOP)s).left, ((BINOP)s).right, ((BINOP)s).binop);
		if (s instanceof CALL)
			return munchCall(((CALL)s).func, ((CALL)s).args);
		if (s instanceof TEMP)
			return ((TEMP)s).temp;
		if (s instanceof CONST)
			return munchConst((CONST)s);
		if (s instanceof NAME)
		{
			compiler.temp.Temp nameTemp = new compiler.temp.Temp();
			emit(
				new compiler.assem.OPER(
					"la `d0, " + ((NAME)s).label.toString() + "\n",
					new TempList(nameTemp, null),
					null));
			return nameTemp;
		}
		return null;
	}
}
