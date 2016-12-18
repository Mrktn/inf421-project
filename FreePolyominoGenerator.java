import java.util.*;
import java.util.stream.*;
import java.math.*;

class FreePolyominoGenerator
{
    public static BitList codeReflecty(int n, BitList fix)
    {
        BitList res = new BitList();
        int m = -1;

        // calcul max x
        for(int k : fix.bits)
        {
            int i = k%n, j = k/n;
            m = Math.max(i, m);
        }
        for(int k : fix.bits)
            res.setBit(m-(k%n) + n*(k/n));
        return res;
    }

    public static BitList codeReflectx(int n, BitList fix)
    {
        BitList res = new BitList();
        int m = -1;

        // calcul max y
        for(int k : fix.bits)
        {
            int i = k%n, j = k/n;
            m = Math.max(j, m);
        }
        for(int k : fix.bits)
            res.setBit((k%n) + n*(m-(k/n)));
        return res;
    }

    public static BitList codeRotate45(int n, BitList fix)
    {
        BitList res = new BitList();
        int m = -1;

        // calcul max x
        for(int k : fix.bits)
        {
            int i = k%n, j = k/n;
            m = Math.max(i, m);
        }

        for(int k : fix.bits)
            res.setBit((k/n) + n*(m-(k%n)));
        return res;
    }

    /* Pour générer les free polyminoes :
        - Commencer par récupérer les fixed
        - Puis tant que c'est possible
            - en récupérer un dans la liste des fixed, on le nomme pol plus loin
            - calculer toutes ses similitudes (rot45, rot45², rot45³, symx, symy, rot45osymx, rot45osymy)
            - on décide que le représentant est pol, et il ne reste qu'à virer les similitudes
    */
    /*public static ArrayList<BigInteger> generateFreePolyominoes(int n)
    {
        ArrayList<BigInteger> fixed = FixedPolyominoGenerator.generateFixedPolyominoes(n);

        for(int i = 0; i < fixed.size(); ++i)
        {
            BigInteger pol = fixed.get(i);

            ArrayList<BitList> similitudes = new ArrayList<BigInteger>();
            similitudes.add(codeReflecty(n, pol));
            similitudes.add(codeReflectx(n, pol));
            similitudes.add(codeRotate45(n, pol));
            similitudes.add(codeRotate45(n, codeRotate45(n, pol)));
            similitudes.add(codeRotate45(n, codeRotate45(n, codeRotate45(n, pol))));
            similitudes.add(codeRotate45(n, codeReflecty(n, pol)));
            similitudes.add(codeRotate45(n, codeReflectx(n, pol)));

            for(BigInteger b : similitudes)
                if(b.compareTo(pol) != 0)
                    fixed.remove(b);
        }

        System.out.println(fixed.size());
        return fixed;
    }*/



    /*
     * Normalize le polyomino en input en le translatant de sorte à ce qu'aucune abscisse / ordonnée ne soit négative
     */
    public static BitList normalize(int n, BitList pol)
    {
        int minx = n, miny = n;

        for(int k : pol.bits)
        {
        	int sgn = k < 0 ? -1 : 1;
        	k*=sgn;
        	int i = sgn*(k%n), j = k/n;
        	minx = Math.min(minx, i);
        	miny = Math.min(miny, j);
        }

        BitList bl = new BitList();

        for(int k : pol.bits)
        {
        	int sgn = k < 0 ? -1 : 1;
        	k*=sgn;
        	int i = sgn*(k%n), j = k/n;

            bl.setBit((i - minx) + n * (j - miny));
        }

        return bl;

    }
    /*
     * Même fonction sur le principe, mais génère à partir de l'algorithme de Redelmeier.
     */
    public static TreeSet<BitList> generateFreePolyominoesRedelmeier(int n)
    {
        ArrayList<BitList> fixed = FixedPolyominoGenerator.generateFixedPolyominoesRedelmeier(n);
        TreeSet<BitList> normalized = new TreeSet<BitList>();
        TreeSet<BitList> toberemoved = new TreeSet<BitList>();

        long startTime = System.currentTimeMillis();

        for(BitList pol : fixed)
        {
        	pol = normalize(n, pol);
        	if(toberemoved.contains(pol))
        		continue;

        	normalized.add(pol);

            TreeSet<BitList> similitudes = new TreeSet<BitList>();
            BitList refy = codeReflecty(n, pol);
            BitList refx = codeReflectx(n, pol);
            BitList rot45 = codeRotate45(n, pol), rot452 = codeRotate45(n, rot45);
            similitudes.add(refy);
            similitudes.add(refx);
            similitudes.add(rot45);
            similitudes.add(rot452);
            similitudes.add(codeRotate45(n, rot452));
            similitudes.add(codeRotate45(n, refy));
            similitudes.add(codeRotate45(n, refx));

            for(BitList b : similitudes)
            {

                if(b.compareTo(pol) != 0)
                {
                    toberemoved.add(b);
                }
            }
        }
        
        System.out.println(normalized.size());
        long endTime = System.currentTimeMillis();
        System.out.println("Free execution time: " + (endTime-startTime) + "ms");
        return normalized;
    }
}