/*
 * Implémente les couples d'entiers (x,y), ordonnables par ordre lexicographique
 */

public class Pair implements Comparable<Pair>
{
    //Ajout d'un méthode equals, d'une methode translate, et d'une toString
	
	public final byte x;
    public final byte y;

    public Pair(int x, int y)
    {
        this.x = (byte)x;
        this.y = (byte)y;
    }

    @Override
    public int compareTo(Pair other)
    {
        if(this.x < other.x || (this.x == other.x && this.y < other.y))
            return -1;
        if(this.x == other.x && this.y == other.y)
            return 0;
        return 1;
    }
    
    @Override
    public boolean equals(Object other){
    	Pair that = (Pair) other;
    	return that.x == this.x && that.y == this.y;
    }

    @Override
    public int hashCode()
    {
        return 27*x + y;
    }
    
    public Pair translate(int x, int y){
    	return new Pair(this.x + x, this.y + y);
    }
    
    public String toString(){
    	return "(" + this.x + ";" + this.y + ")";
    }
}