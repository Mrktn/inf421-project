/*
 * Implémente les couples d'entiers (x,y), ordonnables par ordre lexicographique
 */

public class Pair implements Comparable<Pair>
{
    public final byte x;
    public final byte y;

    public Pair(byte x, byte y)
    {
        this.x = x;
        this.y = y;
    }

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
}