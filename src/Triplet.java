
public class Triplet implements Comparable<Triplet>, Coordinates {
	//Ajout d'un méthode equals, d'une methode translate, et d'une toString
	
		public final byte x;
	    public final byte y;
	    public final byte z;
	    
	    public int get(int CoordinateIndex){
	    	if(CoordinateIndex == 0){
	    		return (int)x;
	    	}
	    	if(CoordinateIndex == 1){
	    		return (int)y;
	    	}
	    	if(CoordinateIndex == 2){
	    		return (int)z;
	    	}
	    	return (int)x;
	    }
	    
	    public int getDimention(){
	    	return 3;
	    }
	    
	    public Triplet add(Coordinates other){
	    	Triplet that = (Triplet) other;
	    	return new Triplet(this.x + that.x, this.y + that.y, this.z + that.z);
	    }

	    public Triplet(int x, int y, int z)
	    {
	        this.x = (byte)x;
	        this.y = (byte)y;
	        this.z = (byte)z;
	    }

	    @Override
	    public int compareTo(Triplet other)
	    {
	        if(this.x < other.x || (this.x == other.x && this.y < other.y) || (this.x == other.x && this.y == other.y && this.z < other.z))
	            return -1;
	        if(this.x == other.x && this.y == other.y && this.z == other.z)
	            return 0;
	        return 1;
	    }
	    
	    @Override
	    public boolean equals(Object other){
	    	Triplet that = (Triplet) other;
	    	return that.x == this.x && that.y == this.y && that.z == this.z;
	    }
	    
	    public Triplet translate(int x, int y){
	    	return new Triplet(this.x + x, this.y + y, this.z + z);
	    }
	    
	    public String toString(){
	    	return "(" + this.x + ";" + this.y + ")";
	    }
}
