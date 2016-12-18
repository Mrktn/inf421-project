import java.util.*;

/*
 * Une liste d'entiers représentant des bits à 1 dans le codage d'un polyomino
 * TODO: implémenter le n
 */
class BitList implements Comparable
{
    public TreeSet<Integer> bits;

    private static class GeqComp implements Comparator<Integer>
    {
    	@Override
    	public int compare(Integer e1, Integer e2)
    	{
        	return -e1.compareTo(e2);
    	}
    }

    BitList()
    {
        bits = new TreeSet<Integer>(new GeqComp());
    }

    BitList(TreeSet<Integer> nbits)
    {
        bits = new TreeSet<Integer>(new GeqComp());
        bits.addAll(nbits);
    }

    public void setBit(int k)
    {
        bits.add(k);
    }

    public void clearBit(int k)
    {
        bits.remove(k);
    }

    public boolean isSet(int k)
    {
        return bits.contains(k);
    }

    public String toString()
    {
        String ret = "{";

        for(int k : bits)
            ret += k + ",";

        return ret+"}";
    }

    @Override
    public int compareTo(Object o)
    {
        BitList other = (BitList) o;

        Iterator<Integer> itrthis  = bits.iterator();
        Iterator<Integer> itrother = other.bits.iterator();

        while(itrthis.hasNext())
        {
        	int kthis  = itrthis.next();
        	int kother = itrother.next();

        	if(kthis > kother)
        		return 1;
        	if(kthis < kother)
        		return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        return this.compareTo(o) == 0;
    }
}