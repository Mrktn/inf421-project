import java.util.*;
import java.math.*;

// L'algorithme de Redelmeier pour compter (et seulement compter) efficacement les polyominos fixes
class RedelmeierFixedPolyominoCounter
{
    /*
     * Algorithme de Redelmeier (Counting Polyominoes: Yet Another Attack, D. H. Redelmeier, 1979)
     */
    private static long redelpol(int n, int k, int[][] untried, int startIndex, int endIndex, boolean[][] field)
    {
        if(k == n)
            return 1;

        else
        {
            long l = 0;

            for(int i = startIndex; i <= endIndex; ++i)
            {
                int x = untried[i][0], y = untried[i][1];

                field[x+n-1][y+1] = false;

                int cursor = 0;
                boolean a = false, b = false, c = false, d = false;

                if(x+1 < n && (field[x+1+n-1][y+1]))
                {
                    field[x+1+n-1][y+1] = false;
                    cursor++;
                    untried[cursor+endIndex][0] = x+1;
                    untried[cursor+endIndex][1] = y;
                    a = true;
                }

                if(y + 1 < n && (field[x+n-1][y+1+1]))
                {
                    field[x+n-1][y+1+1] = false;
                    cursor++;
                    untried[cursor+endIndex][0] = x;
                    untried[cursor+endIndex][1] = y+1;
                    b = true;
                }

                if(x-1 > -n && (field[x-1+n-1][y+1]))
                {
                    field[x-1+n-1][y+1] = false;
                    cursor++;
                    untried[cursor+endIndex][0] = x-1;
                    untried[cursor+endIndex][1] = y;
                    c = true;
                }

                if(y-1 >= -1 && (field[x+n-1][y-1+1]))
                {
                    field[x+n-1][y-1+1] = false;
                    cursor++;
                    untried[cursor+endIndex][0] = x;
                    untried[cursor+endIndex][1] = y-1;
                    d = true;
                }

                l += redelpol(n, k+1, untried, i+1, endIndex+cursor, field);

                if(a)
                    field[x+1+n-1][y+1] = true;
                if(b)
                    field[x+n-1][y+1+1] = true;
                if(c)
                    field[x-1+n-1][y+1] = true;
                if(d)
                    field[x+n-1][y-1+1] = true;
            }

            return l;
        }
    }

    public static long countFixedPolyominoes(int n)
    {
        long startTime = System.currentTimeMillis();

        int[][] untried = new int[(2*n-1)*(n+1)][2];

        // field[i][j] = état de la case [i-n+1][j-1]
        boolean[][] field = new boolean[2*n-1][n+1];

        for(int i = 0; i < 2*n-1; ++i)
            for(int j = 0; j < n+1; ++j)
                field[i][j] = true;

        // On bloque les cases initialement interdites
        for(int k = 0; k < 2*n-1; ++k)
            field[k][0] = false;
        for(int k = 0; k < n-1; ++k)
            field[k][1] = false;

        untried[0][0] = 0;
        untried[0][1] = 0;
        field[n-1][1] = false;

        long ret = redelpol(n, 0, untried, 0, 0, field);
        System.out.println("There are " + ret + " fixed polyominoes of area " + n);
        long endTime = System.currentTimeMillis();
        System.out.println("Fixed counter execution time: " + (endTime-startTime) + "ms");

        return ret;
    }
}