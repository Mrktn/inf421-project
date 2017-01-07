import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.*;
import java.math.BigInteger;
import java.io.*;

public class Main
{

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        
        /*ArrayList<ArrayList<Pair>> fixed = RedelmeierFixedPolyominoGenerator.generateFixedPolyominoesAsCoord(3);

        /*for(ArrayList<Pair> l : fixed)
        {
            for(Pair p : l)
                System.out.print("(" + p.x + "," + p.y + ") ;");
            System.out.println("");
        }
        



        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime-startTime) + "ms");


        ArrayList <ColoredPolygon> cpl = ColoredPolygon.loadFromFile("polyominoesINF421.txt");

        Image2d im = new Image2d(800, 600);

        for(ColoredPolygon cp : cpl)
        {
        	cp.rotate90();
        	cp.rotate90();
        	cp.reflecty();
        	im.addColoredPolygon(cp);
        }



        Image2dViewer frame = new Image2dViewer(im);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);*/
        
        int n = 1;
        int m = 1;
        
        if(args.length == 2){
        	n = Integer.valueOf(args[0]);
        	m = Integer.valueOf(args[1]);
        }
        else if(args.length == 1){
        	n = Integer.valueOf(args[0]);
        	m = n;
        }
        System.out.println("***** Comprehensive enumeration of free polyominoes *****");
        RedelmeierFreePolyominoGenerator.generateFreePolyominoes(n);
        
        System.out.println("\n***** Counting all fixed polyominoes *****");
        RedelmeierFixedPolyominoCounter.countFixedPolyominoes(m);
    }
}