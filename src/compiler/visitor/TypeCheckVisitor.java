package compiler.visitor;

import compiler.syntaxtree.And;
import compiler.syntaxtree.ArrayAssign;
import compiler.syntaxtree.ArrayLength;
import compiler.syntaxtree.ArrayLookup;
import compiler.syntaxtree.Assign;
import compiler.syntaxtree.Block;
import compiler.syntaxtree.BooleanType;
import compiler.syntaxtree.Call;
import compiler.syntaxtree.ClassDeclExtends;
import compiler.syntaxtree.ClassDeclSimple;
import compiler.syntaxtree.False;
import compiler.syntaxtree.Formal;
import compiler.syntaxtree.Identifier;
import compiler.syntaxtree.IdentifierExp;
import compiler.syntaxtree.IdentifierType;
import compiler.syntaxtree.If;
import compiler.syntaxtree.IntArrayType;
import compiler.syntaxtree.IntegerLiteral;
import compiler.syntaxtree.IntegerType;
import compiler.syntaxtree.LessThan;
import compiler.syntaxtree.MainClass;
import compiler.syntaxtree.MethodDecl;
import compiler.syntaxtree.Minus;
import compiler.syntaxtree.NewArray;
import compiler.syntaxtree.NewObject;
import compiler.syntaxtree.Not;
import compiler.syntaxtree.Plus;
import compiler.syntaxtree.Print;
import compiler.syntaxtree.Program;
import compiler.syntaxtree.This;
import compiler.syntaxtree.Times;
import compiler.syntaxtree.True;
import compiler.syntaxtree.Type;
import compiler.syntaxtree.VarDecl;
import compiler.syntaxtree.While;

public class TypeCheckVisitor implements TypeVisitor {

	@Override
	public Type visit(Program n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(MainClass n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ClassDeclSimple n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ClassDeclExtends n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(VarDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(MethodDecl n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Formal n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntArrayType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(BooleanType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntegerType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IdentifierType n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(If n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Print n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Assign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayAssign n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(LessThan n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Plus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Minus n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Times n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayLookup n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(ArrayLength n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Call n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IntegerLiteral n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(True n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(False n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(IdentifierExp n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(This n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(NewArray n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(NewObject n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Not n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}

}
