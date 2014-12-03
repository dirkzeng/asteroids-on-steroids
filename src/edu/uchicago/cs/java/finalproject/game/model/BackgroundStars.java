package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakehergott on 12/3/14.
 */
public class BackgroundStars extends Sprite{


    public BackgroundStars(int rad){

        //call Sprite constructor
        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();

        //draw a star
        pntCs.add(new Point(0, 8));
        pntCs.add(new Point(1, 4));
        pntCs.add(new Point(2, 3));
        pntCs.add(new Point(6, 6));
        pntCs.add(new Point(3, 2));
        pntCs.add(new Point(4, 1));
        pntCs.add(new Point(8, 0));
        pntCs.add(new Point(4, -1));
        pntCs.add(new Point(3, -2));
        pntCs.add(new Point(6, -6));
        pntCs.add(new Point(2, -3));
        pntCs.add(new Point(1, -4));
        pntCs.add(new Point(0, -8));
        pntCs.add(new Point(-1, -4));
        pntCs.add(new Point(-2, -3));
        pntCs.add(new Point(-6, -6));
        pntCs.add(new Point(-3, -2));
        pntCs.add(new Point(-4, -1));
        pntCs.add(new Point(-8, 0));
        pntCs.add(new Point(-3, 2));
        pntCs.add(new Point(-6, 6));
        pntCs.add(new Point(-2, 3));
        pntCs.add(new Point(-1, 4));

        assignPolarPoints(pntCs);

        setColor(Color.WHITE.darker());

        setRadius(rad);

    }


    @Override
    public void draw(Graphics g) {
        super.draw(g);
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }

}
