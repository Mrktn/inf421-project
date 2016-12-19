import java.util.*;
import java.math.*;
import java.lang.instrument.Instrumentation;

class FixedPolyominoGenerator
{
    /* Cette fonction énumère tous les polyominos qu'on peut construire sachant :
         - Qu'on travaille dans un carré nxn (on construit des polyominos de taille n)
         - Qu'il nous reste k blocs à placer
         - Que l'ordonnée minimale d'un bloc placé jusqu'alors est miny
         - Que les voisins des blocs placés jusque là sont dans l'ensemble vois (c'est l'ensemble des blocs directement
           adjacents à des blocs déjà placés, et comme on construit des polyominos qui sont par définition connexes,
           on ajoute toujours des blocs voisins à ceux-là)
         - Qu'on peut savoir en O(1) si le bloc (i,j) est déjà placé juste en regardant checked[i][j]
         - Que les polyominos découverts jusqu'alors sont stockés dans discovered
         - Que le polyomino qu'on a déjà construit (accumulé) a pour valeur val dans mon codage (noraj du codage)
    */
    private static void genpolfixed(int n, int k, int miny, TreeSet<Pair> vois, boolean[][] checked, TreeSet<BigInteger> discovered, BigInteger val, AdjacencyList[][] adjacency)
    {
        /* S'il ne reste aucun bloc à placer, on va regarder si par exemple minx > 0. Si c'est le cas, c'est qu'on
           peut translater le polyomino vers la gauche, ce qu'on fait puisque on cherche les polyominos à translation près.
           Et dans mon codage, translater vers la gauche c'est divisier par une certaine puissance de 2, ce que je fais
           (shiftright un nombre de k c'est le diviser par 2**k).
           Pareil pour y.
           Et on ajoute la représentation canonique de ce polyomino à notre liste (comme c'est un Set, s'il est déjà dedans, il n'est pas ajouté)
           NB: les valeurs de shift sont compréhensibles avec un dessin représentant le codage dans un carré nxn.
        */
        if(k == 0)
        {
            if(miny > 0)
                val = val.shiftRight(miny);

            discovered.add(val);
        }

        // Sinon...
        else
        {
            // copyvois est notre copie de vois sur laquelle on travaille
            TreeSet<Pair> copyvois = new TreeSet<Pair>(vois);

            // Pour chaque voisin du polyomino déjà construit, on va essayer de l'ajouter à notre construction
            for(Pair p : vois)
            {
                // Si on l'ajoute, ce n'est plus un voisin, c'est un bloc fully-fledged !
                copyvois.remove(p);

                // On le biff comme tel du coup.
                checked[p.x][p.y] = true;

                // Pour chaque voisin de ce nouveau bloc qui n'est pas déjà dans la structure, c'est un voisin de la nouvelle structure
                for(Pair v : adjacency[p.x][p.y].adj)
                    if(!checked[v.x][v.y]) // s'il n'est pas déjà biffé, sinon par définition c'est pas un voisin
                        copyvois.add(v);

                /* Appel à la fonction avec les paramètres qui vont bien
                   Exercice pour le lecteur: vérifier que je maintiens bien les invariants que j'ai explicités en prélude de la fonction
                */
                genpolfixed(n, k-1, Math.min(miny, p.y), copyvois, checked, discovered, val.setBit(n*p.x + p.y), adjacency);

                // On défait ce qu'on a fait pour rendre un copyvois propre à la prochaine itération
                copyvois.add(p);
                checked[p.x][p.y] = false;
                for(Pair v : adjacency[p.x][p.y].adj)
                    if(!checked[v.x][v.y])
                        copyvois.remove(v);
            }
        }
    }

    // Énumère tous les polyominos fixés de taille n (leur codage en réalité)
    public static ArrayList<BigInteger> generateFixedPolyominoes(int n)
    {
        TreeSet<BigInteger> res = new TreeSet<BigInteger>();
        AdjacencyList[][] adjacency = new AdjacencyList[n][n];

        // Initialise adjacency. adjacency[i][j] est la liste des voisins de i,j dans le maillage carré
        for(int i = 0; i < n; ++i)
        {
            for(int j = 0; j < n; ++j)
            {
                adjacency[i][j] = new AdjacencyList();
                adjacency[i][j].adj = new ArrayList<Pair>();

                if(i-1 >= 0)
                    adjacency[i][j].adj.add(new Pair(i-1, j));
                if(i+1 < n)
                    adjacency[i][j].adj.add(new Pair(i+1,j));
                if(j-1 >= 0)
                    adjacency[i][j].adj.add(new Pair(i, j-1));
                if(j+1 < n)
                    adjacency[i][j].adj.add(new Pair(i,j+1));
            }
        }

        // On lance la recherche depuis le bord gauche des x = 0
        for(int i = 0; i < n; ++i)
        {
            TreeSet<Pair> vois = new TreeSet<Pair>();
            boolean[][] checked = new boolean[n][n];
            for(Pair p : adjacency[0][i].adj)
                vois.add(p);

            checked[0][i] = true;
            genpolfixed(n, n-1, i, vois, checked, res, BigInteger.ZERO.setBit(i), adjacency);
        }

        System.out.println(res.size());

        return new ArrayList<BigInteger>(res);
    }

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
    private static ArrayList<BitList> redelpol(int n, int k, BitList parent, int[][] sublimeUntried, int startIndex, int endIndex, boolean[][] field)
    {
        if(k == n)
        {
            ArrayList<BitList> ret = new ArrayList<BitList>();
            ret.add(new BitList(parent.lst));
            //System.out.println("+1 ma gueule !");
            return ret;
        }

        else
        {
            ArrayList<BitList> l = new ArrayList<BitList>();

            for(int i = startIndex; i <= endIndex; ++i)
            {
                int x = sublimeUntried[i][0], y = sublimeUntried[i][1];

                field[x+n-1][y+1] = false;
                //parent.setBit(sgn(x) * (Math.abs(x) + n*p.y));
                parent.lst[k] = (short) (sgn(x) * (Math.abs(x) + n*y));

                int cursor = 0;
                boolean a = false, b = false, c = false, d = false;


                if(x+1 < n && (field[x+1+n-1][y+1]))
                {
                    field[x+1+n-1][y+1] = false;
                    cursor++;
                    sublimeUntried[cursor+endIndex][0] = x+1;
                    sublimeUntried[cursor+endIndex][1] = y;
                    a = true;
                }

                if(y + 1 < n && (field[x+n-1][y+1+1]))
                {
                    field[x+n-1][y+1+1] = false;

                    cursor++;
                    sublimeUntried[cursor+endIndex][0] = x;
                    sublimeUntried[cursor+endIndex][1] = y+1;
                    b = true;
                }

                if(x-1 > -n && (field[x-1+n-1][y+1]))
                {
                    field[x-1+n-1][y+1] = false;

                    cursor++;
                    sublimeUntried[cursor+endIndex][0] = x-1;
                    sublimeUntried[cursor+endIndex][1] = y;

                    c = true;
                }

                if(y-1 >= -1 && (field[x+n-1][y-1+1]))
                {
                    field[x+n-1][y-1+1] = false;

                    cursor++;
                    sublimeUntried[cursor+endIndex][0] = x;
                    sublimeUntried[cursor+endIndex][1] = y-1;

                    d = true;

                }

                l.addAll(redelpol(n, k+1, parent, sublimeUntried, i+1, endIndex+cursor, field));

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


    public static ArrayList<BitList> generateFixedPolyominoesRedelmeier(int n)
    {
        long startTime = System.currentTimeMillis();
        BitList initialParent = new BitList(n);

        int[][] sublimeUntried = new int[(2*n-1)*(n+1)][2];

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

        sublimeUntried[0][0] = 0;
        sublimeUntried[0][1] = 0;
        field[n-1][1] = false;

        ArrayList<BitList> ret = redelpol(n, 0, initialParent, sublimeUntried, 0, 0, field);
        System.out.println(ret.size());
        long endTime = System.currentTimeMillis();
        System.out.println("Fixed execution time: " + (endTime-startTime) + "ms");

        return ret;
    }


}