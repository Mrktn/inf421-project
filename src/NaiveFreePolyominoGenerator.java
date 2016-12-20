import java.util.*;
import java.math.*;

public class NaiveFreePolyominoGenerator
{
    // On opère sur les bits du codage de "pol" pour le transformer en le codage canonique de sa réflection selon l'axe x
    public static BigInteger codeReflectx(int n, BigInteger pol)
    {
        BigInteger ret = BigInteger.ZERO;
        int[] bits = new int[n];
        int m = -1, c = 0;

        // calcul max y
        while(!pol.equals(BigInteger.ZERO))
        {
            int k = pol.getLowestSetBit();
            bits[c++] = k;
            int i = k%n, j = k/n;
            m = Math.max(j, m);
            pol = pol.clearBit(k);
        }

        for(c = 0; c < n; ++c)
            ret = ret.setBit(bits[c]%n + n*(m - bits[c]/n));

        return ret;
    }

    // Idem pour la réflection selon l'axe y
    public static BigInteger codeReflecty(int n, BigInteger pol)
    {
        BigInteger ret = BigInteger.ZERO;
        int[] bits = new int[n];
        int m = -1, c = 0;

        // calcul max x
        while(!pol.equals(BigInteger.ZERO))
        {
            int k = pol.getLowestSetBit();
            bits[c++] = k;
            int i = k%n, j = k/n;
            m = Math.max(i, m);
            pol = pol.clearBit(k);
        }

        for(c = 0; c < n; ++c)
            ret = ret.setBit(m-(bits[c]%n) + n*(bits[c]/n));

        return ret;
    }

    // Et la même chose pour la rotation d'angle pi/2
    public static BigInteger codeRotate90(int n, BigInteger pol)
    {
        BigInteger ret = BigInteger.ZERO;
        int[] bits = new int[n];
        int m = -1, c = 0;

        // calcul max x
        while(!pol.equals(BigInteger.ZERO))
        {
            int k = pol.getLowestSetBit();
            bits[c++] = k;
            int i = k%n, j = k/n;
            m = Math.max(i, m);
            pol = pol.clearBit(k);
        }

        for(c = 0; c < n; ++c)
            ret = ret.setBit((bits[c]/n) + n*(m - bits[c]%n));

        return ret;
    }

    public static ArrayList<BigInteger> generateFreePolyominoes(int n)
    {
        ArrayList<BigInteger> fixed = NaiveFixedPolyominoGenerator.generateFixedPolyominoes(n);

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < fixed.size(); ++i)
        {
            BigInteger pol = fixed.get(i);

            ArrayList<BigInteger> similitudes = new ArrayList<BigInteger>();
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
        long endTime = System.currentTimeMillis();
        System.out.println("NaiveFree execution time: " + (endTime-startTime) + "ms");
        return fixed;
    }
}