/*
 * Implémente les couples d'entiers (x,y), ordonnables par ordre lexicographique
 */

public class Pair implements Comparable<Pair>
{
    public final int x;
    public final int y;

    public Pair(int x, int y)
    {
        this.x = x;
        this.y = y;
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
}