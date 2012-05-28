package compiler.temp;

public class Temp extends SimpleExp{
	
	private static int count = 30;
	private int num;
	public boolean spillTemp = false;
    public int spillCost;
	
	public Temp() { 
		num=count++;
	}
	
	public Temp(int t){    
		num = t ;
	}
	
	public String toString() {
		return "t" + num;
	}
	
    public int hashCode() { 
    	return num; 
    }
}