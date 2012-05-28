package compiler.translate;

import compiler.symbol.Symbol;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class Level {
    public Level parent;
    compiler.frame.Frame frame;		// not public!
    public LinkedList formals = new LinkedList();
    
    public Level(Level p, Symbol name, List escapes) {
		parent = p;
		frame = parent.frame.newFrame(name, escapes);
		if(frame != null)
		  for (Iterator f = frame.formals.iterator(); f.hasNext();)
		    formals.add(new Access(this, (compiler.frame.Access)f.next()));
    }

    Level(compiler.frame.Frame f) { 
    	frame = f; 
    }

    public compiler.temp.Label name() { 
    	return frame.name; 
    }

    public Access allocLocal(boolean escape) {
	return new Access(this, frame.allocLocal(escape));
    }
}


