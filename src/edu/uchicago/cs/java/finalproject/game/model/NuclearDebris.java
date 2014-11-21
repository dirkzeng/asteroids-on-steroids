package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/21/14.
 */
public class NuclearDebris extends Sprite{

    private final int RAD = 5000;

    public NuclearDebris(){

        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();

        // full screen
        pntCs.add(new Point(0, 5000));
        pntCs.add(new Point(5000, 0));
        pntCs.add(new Point(0, -5000));
        pntCs.add(new Point(-5000, 0));

        assignPolarPoints(pntCs);

        Point point = new Point(0,0);
        setCenter(point);
        setColor(Color.RED);
        setRadius(RAD);

        setExpire(10);
    }

    @Override
    public void expire() {
        if (getExpire() == 0)
            CommandCenter.movDebris.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if(getExpire() % 4 == 0){
            setColor(Color.YELLOW);
        }else if(getExpire() % 4 == 1){
            setColor(Color.RED);
        }else if(getExpire() % 4 == 2){
            setColor(Color.CYAN);
        }else if(getExpire() % 4 == 3){
            setColor(Color.MAGENTA);
        }
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
