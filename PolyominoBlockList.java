import java.util.*;

/*
 * Encapsule une ArrayList<Pair> pour faire des "ArrayList<ArrayList<Pair>>" (aka des listes de polyominos) facilement
 */
class PolyominoBlockList
{
    public ArrayList<Pair> pl;

    PolyominoBlockList(ArrayList<Pair> l)
    {
        this.pl = l;
    }
}