import java.util.*;
import java.util.stream.*;
import java.math.*;
import java.util.stream.Collectors;

class RedelmeierFreePolyominoGenerator
{
	//ajout d'une methode qui traduit le codage en un ColoredPolygon
	
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
    public static ArrayList<short[]> generateFreePolyominoes(int n)
    {
        // On récupère les polyominos fixes
        ArrayList<short[]> fixed = RedelmeierFixedPolyominoGenerator.generateFixedPolyominoes(n);

        // Un comparateur pour les arrays : c'est simplement un ordre lexicographique
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
            }
        };

        short[] refy = new short[n];
        short[] refx = new short[n];
        short[] rot90 = new short[n];
        short[] rot902 = new short[n];
        short[] rot903 = new short[n];
        short[] rot90y = new short[n];
        short[] rot90x = new short[n];

        short[][] similitudes = new short[][] {refy, refx, rot90, rot902, rot903, rot90y, rot90x};

        ArrayList<short[]> ret = new ArrayList<short[]>();
        TreeSet<short[]> bucket = new TreeSet<short[]>(arrayComparator);

        long startTime = System.currentTimeMillis();

        

        for(int i = 0; i < fixed.size(); ++i)
        {
            short[] pol = fixed.get(i);

            normalize(pol);
            Arrays.sort(pol);

            if(bucket.contains(pol))
                continue;

            ret.add(pol);

            codeReflecty(n, refy, pol);
            codeReflectx(n, refx, pol);
            codeRotate90(n, rot90, pol);
            codeRotate90(n, rot902, rot90);
            codeRotate90(n, rot903, rot902);
            codeRotate90(n, rot90y, refy);
            codeRotate90(n, rot90x, refx);

            Arrays.sort(refy);
            Arrays.sort(refx);
            Arrays.sort(rot90);
            Arrays.sort(rot902);
            Arrays.sort(rot903);
            Arrays.sort(rot90x);
            Arrays.sort(rot90y);

            for(int j = 0; j < 7; ++j)
                if(!Arrays.equals(similitudes[j], pol))
                    bucket.add(Arrays.copyOf(similitudes[j], n));
        }

        System.out.println("There are " + ret.size() + " free polyominoes of area " + n);

        
        long endTime = System.currentTimeMillis();
        System.out.println("Free execution time: " + (endTime-startTime) + "ms");
        return ret;
    }

    public static ArrayList<ArrayList<Pair>> generateFreePolyominoesAsCoord(int n)
    {
        return generateFreePolyominoes(n).stream().map((short[] l) -> codage2ListOfPairs(n, l)).collect(Collectors.toCollection(ArrayList::new));
    }
    
    public static ArrayList<short[]> classeEquivalence(final short[] pol)
    {
	int n = pol.length;
    	ArrayList<short[]> ret = new ArryList<short[]>(7);
	short[] workarray = new short[n];
	short[] refy = new short[n];
        short[] refx = new short[n];
        short[] rot90 = new short[n];
        short[] rot902 = new short[n];
        short[] rot903 = new short[n];
        short[] rot90y = new short[n];
        short[] rot90x = new short[n];
	    short[][] similitudes = new short[][] {refy, refx, rot90, rot902, rot903, rot90y, rot90x};
	codeReflecty(n, refy, pol);
            codeReflectx(n, refx, pol);
            codeRotate90(n, rot90, pol);
            codeRotate90(n, rot902, rot90);
            codeRotate90(n, rot903, rot902);
            codeRotate90(n, rot90y, refy);
            codeRotate90(n, rot90x, refx);
	
	    for(int i = 0; i < 7; ++i)
		    ret.add(similitudes[i]);
	    return ret;
    }
    
    public static ArrayList<ColoredPolygon> generateFreePolyominoesAsColoredPolygon(int n){
    	
    	return generateFreePolyominoes(n).stream().map((short[] l) -> codage2ColoredPolygon(n, l)).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Pair> codage2ListOfPairs(int n, final short[] cod)
    {
        ArrayList<Pair> ret = new ArrayList<Pair>(n);

        for(int i = 0; i < n; ++i)
            ret.add(new Pair(cod[i]%n, cod[i]/n));

        return ret;
    }
    
    public static ColoredPolygon codage2ColoredPolygon(int n, final short[] cod)
    {
        int[] xc = new int[n];
        int[] yc = new int[n];

        for(int i = 0; i < n; ++i){
        	xc[i] = cod[i]%n;
        	yc[i] = cod[i]/n;
        }

        return new ColoredPolygon(xc,yc,ColoredPolygon.rndCol());
    }
}
