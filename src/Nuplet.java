
public class Nuplet implements Comparable<Nuplet>{
	
	public final byte[] x;
    
    public int get(int CoordinateIndex){
    	try{
    		return x[CoordinateIndex];
    	}
    	catch(IndexOutOfBoundsException e){
    		System.err.println("Index out of range");
    	}
		return x[0];
    	
    }
    
    public int getDimention(){
    	return x.length;
    }
    
    public Nuplet add(Nuplet other){
    	
    	if(this.getDimention() != other.getDimention()){
    		return null;
    	}
    	
    	byte[] x = new byte[this.getDimention()];
    	
    	for(int i = 0; i < this.getDimention(); i++){
    		x[i] = (byte) (this.x[i] + other.x[i]);
    	}
    	
    	return new Nuplet(x);
    }
    
    public Nuplet sub(Nuplet other){
    	
    	if(this.getDimention() != other.getDimention()){
    		return null;
    	}
    	
    	byte[] x = new byte[this.getDimention()];
    	
    	for(int i = 0; i < this.getDimention(); i++){
    		x[i] = (byte) (this.x[i] - other.x[i]);
    	}
    	
    	return new Nuplet(x);
    }
    
    public Nuplet opp(Nuplet that){
    	
    	byte[] x = new byte[this.getDimention()];
    	
    	for(int i = 0; i < this.getDimention(); i++){
    		x[i] = (byte) -that.x[i];
    	}
    	
    	return new Nuplet(x);
    }

    public Nuplet(int[] that)
    {
        x = new byte[that.length];
    	for(int i = 0; i < that.length; i++){
        	this.x[i] = (byte)that[i];
        }
    }
    
    public Nuplet(byte[] that)
    {
        x = new byte[that.length];
    	for(int i = 0; i < that.length; i++){
        	this.x[i] = that[i];
        }
    }

    @Override
    public int compareTo(Nuplet other)
    {
        boolean condition = true;
        boolean rtnCondition = false;
        
        for(int i = 0; i < this.getDimention(); i++){
        	for(int j = 0; j < i; j++){
        		condition &= (this.x[j] == other.x[j]);
        	}
        	condition &= (this.x[i] < other.x[i]);
        	rtnCondition |= condition;
        	condition = true;
        }
        
        if(rtnCondition){
        	return -1;
        }
        else if(this.equals(other)){
        	return 0;
        }
        else{
        	return 1;
        }
    }
    
    @Override
    public boolean equals(Object that){
    	
    	boolean condition = true;
    	
    	Nuplet other = (Nuplet) that;
    	
    	for(int i = 0; i < this.getDimention(); i++){
    		condition &= (this.x[i] == other.x[i]);
    	}
    	
    	return condition;
    }
    
    public String toString(){
    	
    	String str ="(";
    	
    	for(int i = 0; i < this.getDimention()-1; i++){
    		str += this.x[i] + ";";
    	}
    	str += String.valueOf(this.x[this.getDimention()-1]);
    	return str + ")";
    }
}
