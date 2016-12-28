
public class Triplet implements Comparable<Triplet>{
	//Ajout d'un méthode equals, d'une methode translate, et d'une toString
	
		public final byte x;
	    public final byte y;
	    public final byte z;

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
