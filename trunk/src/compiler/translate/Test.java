package Translate;

import Temporary.SimpleExp;
import Temporary.Temp;
import Temporary.Offset;

public class Test{
	
    public void test() {
        SimpleExp se = new Temp(1);
        if(se instanceof Temp) System.exit(-1);
    }
}
