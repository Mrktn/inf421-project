import java.util.*;
import java.math.*;

class RedelmeierFixedPolyominoGenerator
{


    private static class LinkedPoint
    {
        public Pair loc;
        public LinkedPoint next;

        LinkedPoint(Pair nloc, LinkedPoint nnext)
        {
            this.loc = nloc;
            this.next = nnext;
        }
    }

    // 0 est positif
    private static int sgn(int x)
    {
        return x < 0 ? (-1) : 1;
    }


    /*
     * Algorithme de Redelmeier (Counting Polyominoes: Yet Another Attack, D. H. Redelmeier, 1979)
     */
    private static ArrayList<short[]> redelpol(int n, int k, short[] parent, int[][] untried, int startIndex, int endIndex, boolean[][] field)
    {
        if(k == n)
        {
            ArrayList<short[]> ret = new ArrayList<short[]>();
            short[] neu = new short[n];

            System.arraycopy(parent, 0, neu, 0, n);
            ret.add(neu);
            return ret;
        }

        else
        {
            ArrayList<short[]> l = new ArrayList<short[]>();

            for(int i = startIndex; i <= endIndex; ++i)
            {
                int x = untried[i][0], y = untried[i][1];

                field[x+n-1][y+1] = false;
                //parent.setBit(sgn(x) * (Math.abs(x) + n*p.y));
                parent[k] = (short) (sgn(x) * (Math.abs(x) + n*y));//lst[k] = ;

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

                l.addAll(redelpol(n, k+1, parent, untried, i+1, endIndex+cursor, field));

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


    public static ArrayList<short[]> generateFixedPolyominoes(int n)
    {
        long startTime = System.currentTimeMillis();
        short[] initialParent = new short[n];

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

        ArrayList<short[]> ret = redelpol(n, 0, initialParent, untried, 0, 0, field);
        System.out.println(ret.size());
        long endTime = System.currentTimeMillis();
        System.out.println("Fixed execution time: " + (endTime-startTime) + "ms");

        return ret;
    }


}