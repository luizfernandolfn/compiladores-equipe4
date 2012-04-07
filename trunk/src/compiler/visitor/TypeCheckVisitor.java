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
		
		if (! (n.e1.accept(this) instanceof BooleanType) ) {
		    System.out.println("Left side of And must be of type integer");
		    System.exit(-1);
		}
		if (! (n.e2.accept(this) instanceof BooleanType) ) {
		    System.out.println("Right side of And must be of type integer");
		    System.exit(-1);
		}
		return new BooleanType();
	}

	@Override
	public Type visit(LessThan n) {
		
		if (! (n.e1.accept(this) instanceof IntegerType) ) {
		   System.out.println("Left side of LessThan must be of type integer");
		   System.exit(-1);
		}
		if (! (n.e2.accept(this) instanceof IntegerType) ) {
		   System.out.println("Right side of LessThan must be of type integer");
		   System.exit(-1);
		}
		return new BooleanType();
	}

	@Override
	public Type visit(Plus n) {
		
		if ( !(n.e1.accept(this) instanceof IntegerType ) )
			System.out.println("Left side of Plus must be of type integer");
		
		if ( !(n.e2.accept(this) instanceof IntegerType ) )
			System.out.println("Right side of Plus must be of type integer");
		
		return new IntegerType();
	}

	@Override
	public Type visit(Minus n) {
		
		if ( !(n.e1.accept(this) instanceof IntegerType ) )
			System.out.println("Left side of Minus must be of type integer");
		
		if ( !(n.e2.accept(this) instanceof IntegerType ) )
			System.out.println("Right side of Minus must be of type integer");
		
		return new IntegerType();
	}

	@Override
	public Type visit(Times n) {
		
		if ( !(n.e1.accept(this) instanceof IntegerType ) )
			System.out.println("Left side of Times must be of type integer");
		
		if ( !(n.e2.accept(this) instanceof IntegerType ) )
			System.out.println("Right side of Times must be of type integer");
		
		return new IntegerType();
	}

	@Override
	public Type visit(ArrayLookup n) {
		if (! (n.e1.accept(this) instanceof IntArrayType) ) {
	    }
	    if (! (n.e2.accept(this) instanceof IntegerType) ) {
	    }
	    return new IntegerType();
	}

	@Override
	public Type visit(ArrayLength n) {
		if (! (n.e.accept(this) instanceof IntArrayType) ) {
	    }
	    return new IntegerType();
	}

	@Override
	public Type visit(Call n) {
		if (! (n.e.accept(this) instanceof IdentifierType)){
			System.out.println("method "+ n.i.toString() 
					   + "called  on something that is not a"+
					   " class or Object.");
			System.exit(-1);
		    } 

		    String mname = n.i.toString();    
		    String cname = ((IdentifierType) n.e.accept(this)).s;

		    Method calledMethod = TypeCheckVisitor.symbolTable.getMethod(mname,cname);
		    
		    for ( int i = 0; i < n.el.size(); i++ ) {     	
			Type t1 =null;  
			Type t2 =null;  

			if (calledMethod.getParamAt(i)!=null)
			    t1 = calledMethod.getParamAt(i).type();
			t2 = n.el.elementAt(i).accept(this);
			if (!TypeCheckVisitor.symbolTable.compareTypes(t1,t2)){
			    System.out.println("Type Error in arguments passed to " +
					       cname+"." +mname);
			    System.exit(-1);  
			}	    
		    }

		    return TypeCheckVisitor.symbolTable.getMethodType(mname,cname);
	}

	@Override
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	@Override
	public Type visit(True n) {
		return new BooleanType();
	}

	@Override
	public Type visit(False n) {
		return new BooleanType();
	}

	@Override
	public Type visit(IdentifierExp n) {
		return TypeCheckVisitor.symbolTable.getVarType(TypeCheckVisitor.currMethod,
			      TypeCheckVisitor.currClass,n.s);
	}

	@Override
	public Type visit(This n) {
		return TypeCheckVisitor.currClass.type();
	}

	@Override
	public Type visit(NewArray n) {
		if (! (n.e.accept(this) instanceof IntegerType) ) {
		       System.out.println("An appropriate message");
		       System.exit(-1);
		    }
		    return new IntArrayType();
	}

	@Override
	public Type visit(NewObject n) {
		return new IdentifierType(n.i.s);
	}

	@Override
	public Type visit(Not n) {
		if (! (n.e.accept(this) instanceof BooleanType) ) {
		       System.out.println("Argument of ! must be of type Boolean");
		       System.exit(-1);
		    }
		    return new BooleanType();
	}

	@Override
	public Type visit(Identifier n) {
		// TODO Auto-generated method stub
		return null;
	}

}
