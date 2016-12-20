import java.util.*;
import java.util.stream.*;
import java.math.*;

class FreePolyominoGenerator
{
    public static BitList codeReflecty(int n, BitList fix)
    {
        BitList res = new BitList(n);
        int m = -1;

        // calcul max x
        for(int p = 0; p < n; ++p)
        {
            short k = fix.lst[p];
            int i = k%n, j = k/n;
            m = Math.max(i, m);
        }
        for(int i = 0; i < n; ++i)
            res.lst[i] = (short)((m-(fix.lst[i]%n) + n*(fix.lst[i]/n)));
        return res;
    }

    public static BitList codeReflectx(int n, BitList fix)
    {
        BitList res = new BitList(n);
        int m = -1;

        // calcul max y
        for(int p = 0; p < n; ++p)
        {
            short k = fix.lst[p];
            int i = k%n, j = k/n;
            m = Math.max(j, m);
        }
        for(int i = 0; i < n; ++i)
            res.lst[i] = (short)((fix.lst[i]%n) + n*(m-(fix.lst[i]/n)));
        return res;
    }

    public static BitList codeRotate90(int n, BitList fix)
    {
        BitList res = new BitList(n);
        int m = -1;

        // calcul max x
        for(int p = 0; p < n; ++p)
        {
            short k = fix.lst[p];
            int i = k%n, j = k/n;
            m = Math.max(i, m);
        }

        for(int i = 0; i < n; ++i)
            res.lst[i] = (short)((fix.lst[i]/n) + n*(m-(fix.lst[i]%n)));
        return res;
    }

    /* Pour générer les free polyminoes :
        - Commencer par récupérer les fixed
        - Puis tant que c'est possible
            - en récupérer un dans la liste des fixed, on le nomme pol plus loin
            - calculer toutes ses similitudes (rot90, rot90², rot90³, symx, symy, rot90osymx, rot90osymy)
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
            similitudes.add(codeRotate90(n, pol));
            similitudes.add(codeRotate90(n, codeRotate90(n, pol)));
            similitudes.add(codeRotate90(n, codeRotate90(n, codeRotate90(n, pol))));
            similitudes.add(codeRotate90(n, codeReflecty(n, pol)));
            similitudes.add(codeRotate90(n, codeReflectx(n, pol)));

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
    public static void normalize(short[] pol)
    {
        int n = (short)pol.length;
        int minx = n, miny = n;

        for(int p = 0; p < n; ++p)
        {
            short k = pol[p];
            int sgn = k < 0 ? -1 : 1;
            k*=sgn;
            int i = sgn*(k%n), j = (k/n);
            minx = Math.min(minx, i);
            miny = Math.min(miny, j);
        }

        for(int p = 0; p < n; ++p)
        {
            short k = pol[p];
            int sgn = k < 0 ? -1 : 1;
            k*= (short)sgn;
            int i = (sgn)*(k%n), j = k/n;
            pol[p] = (short)((i - minx) + n * (j - miny));
        }

    }
    /*
     * Même fonction sur le principe, mais génère à partir de l'algorithme de Redelmeier.
     */
    public static TreeSet<BitList> generateFreePolyominoes(int n)
    {
        ArrayList<BitList> fixed = FixedPolyominoGenerator.generateFixedPolyominoes(n);
        TreeSet<BitList> ret = new TreeSet<BitList>();
        TreeSet<BitList> bucket = new TreeSet<BitList>();

        long startTime = System.currentTimeMillis();

        BitList[] similitudes = new BitList[7];

        for(int i = 0; i < fixed.size(); ++i)
        {
            BitList pol = fixed.get(i);

            normalize(pol.lst);
            Arrays.sort(pol.lst);

            if(bucket.contains(pol))
                continue;

            ret.add(pol);

            BitList refy = codeReflecty(n, pol);
            BitList refx = codeReflectx(n, pol);
            BitList rot90 = codeRotate90(n, pol), rot902 = codeRotate90(n, rot90), rot903 = codeRotate90(n, rot902);
            BitList rot90y = codeRotate90(n, refy), rot90x = codeRotate90(n, refx);

            Arrays.sort(refy.lst);
            Arrays.sort(refx.lst);
            Arrays.sort(rot90.lst);
            Arrays.sort(rot902.lst);
            Arrays.sort(rot903.lst);
            Arrays.sort(rot90x.lst);
            Arrays.sort(rot90y.lst);

            similitudes[0] = (refy);
            similitudes[1] = (refx);
            similitudes[2] = (rot90);
            similitudes[3] = (rot902);
            similitudes[4] = (rot903);
            similitudes[5] = (rot90y);
            similitudes[6] = (rot90x);

            for(int j = 0; j < 7; ++j)
            {
                if(!similitudes[j].equals(pol))
                {
                    bucket.add(similitudes[j]);
                }
            }
        }

        System.out.println(ret.size());
        long endTime = System.currentTimeMillis();
        System.out.println("Free execution time: " + (endTime-startTime) + "ms");
        return ret;
    }
}