import java.util.*;
import java.util.stream.*;
import java.math.*;

class FreePolyominoGenerator
{
    public static void codeReflecty(int n, short[] workarray, short[] fix)
    {
        int m = -1;

        // calcul max x
        for(int p = 0; p < n; ++p)
        {
            short k = fix[p];
            int i = k%n, j = k/n;
            m = Math.max(i, m);
        }
        for(int i = 0; i < n; ++i)
            workarray[i] = (short)((m-(fix[i]%n) + n*(fix[i]/n)));
    }

    public static void codeReflectx(int n, short[] workarray, short[] fix)
    {
        int m = -1;

        // calcul max y
        for(int p = 0; p < n; ++p)
        {
            short k = fix[p];
            int i = k%n, j = k/n;
            m = Math.max(j, m);
        }
        for(int i = 0; i < n; ++i)
            workarray[i] = (short)((fix[i]%n) + n*(m-(fix[i]/n)));
    }

    public static void codeRotate90(int n, short[] workarray, short[] fix)
    {
        int m = -1;

        // calcul max x
        for(int p = 0; p < n; ++p)
        {
            short k = fix[p];
            int i = k%n, j = k/n;
            m = Math.max(i, m);
        }

        for(int i = 0; i < n; ++i)
            workarray[i] = (short)((fix[i]/n) + n*(m-(fix[i]%n)));
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

            ArrayList<short[]> similitudes = new ArrayList<BigInteger>();
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
    public static TreeSet<short[]> generateFreePolyominoes(int n)
    {
        ArrayList<short[]> fixed = FixedPolyominoGenerator.generateFixedPolyominoes(n);

        Comparator<short[]> arrayComparator = new Comparator<short[]>()
        {
            @Override
            public int compare(short[] o1, short[] o2)
            {
                for(int i = 0; i < o1.length; ++i)
                {
                    if(o1[i] < o2[i])
                        return -1;
                    if(o1[i] > o2[i])
                        return 1;
                }
                return 0;
            }};

        TreeSet<short[]> ret = new TreeSet<short[]>(arrayComparator);
        TreeSet<short[]> bucket = new TreeSet<short[]>(arrayComparator);

            long startTime = System.currentTimeMillis();

            short[][] similitudes = new short[7][];

            for(int i = 0; i < fixed.size(); ++i)
            {
                short[] pol = fixed.get(i);

                normalize(pol);
                Arrays.sort(pol);

                if(bucket.contains(pol))
                    continue;

                ret.add(pol);

                short[] refy = new short[n];
                codeReflecty(n, refy, pol);
                short[] refx = new short[n];
                codeReflectx(n, refx, pol);
                short[] rot90 = new short[n];
                codeRotate90(n, rot90, pol);
                short[] rot902 = new short[n];
                codeRotate90(n, rot902, rot90);
                short[] rot903 = new short[n];
                codeRotate90(n, rot903, rot902);
                short[] rot90y = new short[n];
                codeRotate90(n, rot90y, refy);
                short[] rot90x = new short[n];
                codeRotate90(n, rot90x, refx);

                Arrays.sort(refy);
                Arrays.sort(refx);
                Arrays.sort(rot90);
                Arrays.sort(rot902);
                Arrays.sort(rot903);
                Arrays.sort(rot90x);
                Arrays.sort(rot90y);

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