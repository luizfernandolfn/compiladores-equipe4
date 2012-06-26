package compiler.assem;

import compiler.temp.Temp;
import compiler.temp.TempMap;
import compiler.temp.Label;
import java.util.List;
import java.util.Iterator;

public abstract class Instr {
    public String assem;
    public Temp[] use;
    public Temp[] def;
    public List jumps;
    public void replaceUse(Temp olduse, Temp newuse) {
	if (use != null)
	    for (int i = 0; i< use.length; i++)
		if (use[i] == olduse) use[i] = newuse;
    }
    public void replaceDef(Temp olddef, Temp newdef) {
	if (def != null)
	    for (int i = 0; i< def.length; i++)
		if (def[i] == olddef) def[i] = newdef;
    };

    public String format(TempMap m) {
	StringBuffer s = new StringBuffer();
	int len = assem.length();
	for(int i=0; i<len; i++)
	    if (assem.charAt(i)=='`')
		switch(assem.charAt(++i)) {
		case 's': {
		    int n = Character.digit(assem.charAt(++i),10);
		    s.append(m.tempMap(use[n]));
		    break;
		}
		case 'd': {
		    int n = Character.digit(assem.charAt(++i),10);
		    s.append(m.tempMap(def[n]));
		    break;
		}
		case 'j': {
		    int n = Character.digit(assem.charAt(++i),10);
		    s.append(jumps.get(n).toString());
		    break;
		}
		case '`':
		    s.append('`'); 
		    break;
		default:
		    throw new Error("bad Assem format:" + assem);
		}
	    else s.append(assem.charAt(i));
	return s.toString();
    }
    public abstract String toString();

    public String toString( Temp[] t) {
	String result = "";
	if( t==null) return result;
	for (int i=0; i<t.length; i++)
		result = result + t[i]+ " ";
	return result;
    }

    public String toString( Targets j) {
	String result = "";
	if( j==null) return result;
	Iterator l = j.labels.iterator();
		
	while( l.hasNext()) 
		result = result + l.next();
	return result;
    }
   abstract public Temp[] use();
   abstract public Temp[] def();
   abstract public Targets jumps();


}
