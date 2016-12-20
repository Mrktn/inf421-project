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
        RedelmeierFreePolyominoGenerator.generateFreePolyominoes(15);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime-startTime) + "ms");


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