import java.util.*;
import java.math.*;

public class NaiveFixedPolyominoGenerator
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
        /* S'il ne reste aucun bloc à placer, on va regarder si miny > 0. Si c'est le cas, c'est qu'on
           peut translater le polyomino vers le haut, ce qu'on fait puisque on cherche les polyominos à translation près.
           Et dans mon codage, translater vers le hautc'est divisier par une certaine puissance de 2, ce que je fais
           (shiftright un nombre de k c'est le diviser par 2**k).
           Et on ajoute la représentation canonique de ce polyomino à notre liste (comme c'est un Set, s'il est déjà dedans, il n'est pas ajouté)
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
        long startTime = System.currentTimeMillis();
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
        long endTime = System.currentTimeMillis();
        System.out.println("NaiveFixed execution time: " + (endTime-startTime) + "ms");

        return new ArrayList<BigInteger>(res);
    }
}