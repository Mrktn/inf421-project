import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;
import java.math.BigInteger;
import java.io.*;
public class Main
{
    public static int serialize(Object obj) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        return baos.toByteArray().length;
    }

    public static void main(String[] args)
    {
        int sz = 0;

        /* First trigger classloading */
        /*BitList delegate = new BitList(1);

        Runtime.getRuntime().gc();

        long before = Runtime.getRuntime().freeMemory();
        BitList delegate2 = new BitList(1);
        long after = Runtime.getRuntime().freeMemory();

        System.out.println("Memory used:"+(before-after));*/

        for(int i = 2; i < 5; ++i)
        long startTime = System.currentTimeMillis();
        FreePolyominoGenerator.generateFreePolyominoes(2);
        long endTime = System.currentTimeMillis();
        //System.out.println("Total execution time: " + (endTime-startTime) + "ms");


        /*ArrayList <ColoredPolygon> cpl = ColoredPolygon.loadFromFile("polyominoesINF421.txt");

        Image2d im = new Image2d(800, 600);

        for(ColoredPolygon cp : cpl)
        {
        	cp.rotate45();
        	cp.rotate45();
        	cp.reflecty();
        	im.addColoredPolygon(cp);
        }



        Image2dViewer frame = new Image2dViewer(im);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);*/
    }
}