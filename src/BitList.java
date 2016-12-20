import java.util.*;

/*
 * Une liste d'entiers représentant des bits à 1 dans le codage d'un polyomino
 */
class BitList implements Comparable
{
    public short[] lst;

    BitList(int n)
    {
        lst = new short[n];
    }

    BitList(short[] nlst)
    {
        lst = new short[nlst.length];
        for(int i = 0; i < nlst.length; ++i)
        {
            lst[i] = nlst[i];
        }
    }

    public void set(int k, short v)
    {
        this.lst[k] = v;
    }

    @Override
    public int compareTo(Object o)
    {
        BitList other = (BitList)o;

        for(int i = 0; i < lst.length; ++i)
        {
            if(this.lst[i] < other.lst[i])
                return -1;
            if(this.lst[i] > other.lst[i])
                return 1;
        }

        return 0;
    }
}