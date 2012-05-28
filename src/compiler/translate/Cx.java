package compiler.translate;

import compiler.temp.Temp;
import compiler.temp.Label;

public abstract class Cx extends Exp {

    public compiler.tree.Exp unEx() {
		Temp r = new Temp();
		Label t = new Label();
		Label f = new Label();

		return new compiler.tree.ESEQ
		    (new compiler.tree.SEQ
			(new compiler.tree.MOVE(new compiler.tree.TEMP(r), new compiler.tree.CONST(1)),
			 new compiler.tree.SEQ(unCx(t, f),
				      new compiler.tree.SEQ
					  (new compiler.tree.LABEL(f),
					   new compiler.tree.SEQ
					       (new compiler.tree.MOVE(new compiler.tree.TEMP(r),
							      new compiler.tree.CONST(0)),
						new compiler.tree.LABEL(t))))),
		     new compiler.tree.TEMP(r));
    }

    public abstract compiler.tree.Stm unCx(Label t, Label f);

    public compiler.tree.Stm unNx() {
		Label join = new Label();
	
		return new compiler.tree.SEQ(unCx(join, join), 
				    new compiler.tree.LABEL(join));
    }
}



