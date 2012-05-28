package compiler.translate;

public class Access {
    Level home;
    compiler.frame.Access acc;
    
    Access(Level h, compiler.frame.Access a) {
		home = h;
		acc = a;
    }
    public String toString() {
	return "[" + home.frame.name.toString() + "," 
	    + acc.toString() + "]";
    }
}
