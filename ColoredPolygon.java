import java.awt.*;
import java.util.*;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.math.BigInteger;

public class ColoredPolygon
{
    // Un polygone coloré c'est des coords de carrés de taille 1x1 et une couleur (définie dans awt)
    public int[] xcoords;
    public int[] ycoords;
    public Color color;
    public Polygon polygon;

    public ColoredPolygon(int[] xc, int[] yc, Color c)
    {
        xcoords = xc;
        ycoords = yc;
        color = c;
        polygon = new Polygon(xcoords, ycoords, xcoords.length);
    }

    // Todo: on peut aussi calculer le x max et le y max, ça suffit sachant que l'origine est à 0.
    public int getWidth()
    {
        int mx = Integer.MAX_VALUE;
        int mn = -1;

        for(int x : xcoords)
        {
            if(x < mx)
                mx = x;
            if(x > mn)
                mn = x;
        }

        return mx + 1 - mn;
    }

    public int getHeight()
    {
        int mx = Integer.MAX_VALUE;
        int mn = -1;

        for(int y : ycoords)
        {
            if(y < mx)
                mx = y;
            if(y > mn)
                mn = y;
        }

        return mx + 1 - mn;
    }



    // Une couleur aléatoire !
    public static Color rndCol()
    {
        Random rand = new Random();
        return  new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
    }

    // todo: move to utils
    // Transforme une liste d'entiers en tableau d'entiers, bien pratique
    private static int[] buildIntArray(ArrayList<Integer> integers)
    {
        int[] ints = new int[integers.size()];
        int i = 0;
        for(Integer n : integers)
            ints[i++] = n;
        return ints;
    }

    // Faut pas essayer de comprendre, je prends une string de la forme
    // "[(0,0), (1,0), etc...]" qui code les coins haut-gauche des carrés 1x1 qui constituent le polyomino
    // et j'en fais un objet de type ColoredPolygon (couleur aléatoire)
    public static ColoredPolygon loadFromString(String s)
    {
        Scanner scanner = new Scanner(s);
        java.util.ArrayList<Integer> xco = new ArrayList<Integer>(), yco = new ArrayList<Integer>();

        String parts[] = s.split(",|\\]|\\[|\\(|\\)|\\s+");

        for(int i = 0; i < parts.length; ++i)
        {
            if(parts[i].length() > 0) // Si c'est pas blank
            {
                xco.add(Integer.parseInt(parts[i]));
                i++;
                yco.add(Integer.parseInt(parts[i]));
            }
        }

        return new ColoredPolygon(ColoredPolygon.buildIntArray(xco), ColoredPolygon.buildIntArray(yco), ColoredPolygon.rndCol());
    }


    // Utilise séquentiellement loadFromString sur les lignes d'un fichier
    public static ArrayList<ColoredPolygon> loadFromFile(String filename)
    {
        ArrayList<ColoredPolygon> ret = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while((line = br.readLine()) != null)
                ret.add(ColoredPolygon.loadFromString(line));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return ret;
    }

    public static ColoredPolygon int2ColoredPolygon(int n, BigInteger k)
    {
        return null;
    }

    public static String tree2str(TreeSet<Pair> t)
    {
        String ret = new String();

        ret += "[";

        for(Pair p : t)
        {
            ret += "("+p.x+","+p.y+"), ";
        }
        ret+="]";
        return ret;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////// Opérations géométriques /////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    // NB: je retranslate après chaque opération susceptible de donner des coordonnées négatives pour garder une
    //     origine à 0. Par exemple quand je symétrise selon y (reflecty), je négationne toutes les abscisses puis je translate de
    //     la valeur absolue de l'abscisse minimale pour remettre le polyomionimiono où il faut.

    public void translate(int vx, int vy)
    {
        for(int i = 0; i < xcoords.length; ++i)
        {
            xcoords[i]+=vx;
            ycoords[i]+=vy;
        }
    }

    public void rotate45()
    {
        int m = 0;
        for(int i = 0; i < xcoords.length; ++i)
        {
            int y = ycoords[i];
            ycoords[i] = -xcoords[i];
            if(ycoords[i] <= m)
                m = ycoords[i];
            xcoords[i] = y;
        }

        this.translate(0, -m);
    }

    public void reflecty()
    {
        int m = 0;
        for(int i = 0; i < xcoords.length; ++i)
        {
            xcoords[i] *= -1;
            if(xcoords[i] <= m)
                m = xcoords[i];
        }

        this.translate(-m, 0);
    }
}