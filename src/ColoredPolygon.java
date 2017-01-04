import java.util.List;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.math.BigInteger;

public class ColoredPolygon implements List<Pair> 
{
    //Ajout par rapport à ta version : les methodes permettant d'en faire une extensioo de List<Pair> 
	//(permet de faire l'algo directement avec des ColoredPolygon)
	
	
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
    
    public ColoredPolygon(ArrayList<Pair> points, Color c)
    {
        xcoords = new int[points.size()];
        ycoords = new int[points.size()];
    	
    	int i = 0;
        for(Pair p : points){
        	xcoords[i] = p.x;
        	ycoords[i] = p.y;
        	i++;
        }
        
        color = c;
        polygon = new Polygon(xcoords, ycoords, xcoords.length);
    }
    
    @Override
    public Iterator<Pair> iterator(){
    	
    	ArrayList<Pair> rtn = new ArrayList<Pair>();
    	
    	for(int i = 0; i< xcoords.length; i++){
    		rtn.add(new Pair(xcoords[i], ycoords[i]));
    	}
    	
    	Collections.sort(rtn);
    	
    	return rtn.iterator();
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

    public void rotate90()
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



	@Override
	public int size() {
		return this.xcoords.length;
	}



	@Override
	public boolean isEmpty() {
		if(this.size() == 0)
			return true;
		return false;
	}



	@Override
	public boolean contains(Object o) {
		Pair p = (Pair) o;
		for(int i = 0; i < this.size(); i++){
			if(xcoords[i] == p.x && ycoords[i] == p.y)
				return true;
		}
		return false;
	}



	@Override
	public Object[] toArray() {
		
		Pair[] rtn = new Pair[this.size()];
		
		for(int i = 0; i < this.size(); i++){
			rtn[i] = new Pair(this.xcoords[i], this.ycoords[i]);
		}
		
		return rtn;
	}



	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public boolean add(Pair e) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean addAll(Collection<? extends Pair> c) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean addAll(int index, Collection<? extends Pair> c) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Pair get(int index) {
		
		return new Pair(this.xcoords[index],this.ycoords[index]);
	}



	@Override
	public Pair set(int index, Pair element) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void add(int index, Pair element) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Pair remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public ListIterator<Pair> listIterator() {
		
		ArrayList<Pair> rtn = new ArrayList<Pair>();
		
		for(int i = 0; i < this.size(); i++){
			rtn.add(this.get(i));
		}
		
		return rtn.listIterator();
	}



	@Override
	public ListIterator<Pair> listIterator(int index) {
ArrayList<Pair> rtn = new ArrayList<Pair>();
		
		for(int i = 0; i < this.size(); i++){
			rtn.add(this.get(i));
		}
		
		return rtn.listIterator(index);
	}



	@Override
	public List<Pair> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean equals(Object objThat){
		
		ColoredPolygon that = (ColoredPolygon) objThat;
		if(this.xcoords.equals(that.xcoords) && this.ycoords.equals(that.ycoords))
			return true;
		return false;
	}
}
