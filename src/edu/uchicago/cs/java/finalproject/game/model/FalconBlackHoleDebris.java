package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/23/14.
 */
public class FalconBlackHoleDebris extends Sprite {

    private int nSpin;

    //radius of a falcon
    private final int RAD = 35;


    public FalconBlackHoleDebris(Point point){

        //call Sprite constructor
        super();

        ArrayList<Point> pntCs = new ArrayList<Point>();

        // top of ship
        pntCs.add(new Point(0, 18));

        //right points
        pntCs.add(new Point(3, 3));
        pntCs.add(new Point(12, 0));
        pntCs.add(new Point(13, -2));
        pntCs.add(new Point(13, -4));
        pntCs.add(new Point(11, -2));
        pntCs.add(new Point(4, -3));
        pntCs.add(new Point(2, -10));
        pntCs.add(new Point(4, -12));
        pntCs.add(new Point(2, -13));

        //left points
        pntCs.add(new Point(-2, -13));
        pntCs.add(new Point(-4, -12));
        pntCs.add(new Point(-2, -10));
        pntCs.add(new Point(-4, -3));
        pntCs.add(new Point(-11, -2));
        pntCs.add(new Point(-13, -4));
        pntCs.add(new Point(-13, -2));
        pntCs.add(new Point(-12, 0));
        pntCs.add(new Point(-3, 3));


        assignPolarPoints(pntCs);

        setCenter(point);
        setColor(Color.WHITE);

        setRadius(RAD);

        setExpire(8);
    }

    //overridden
    public void move(){
        super.move();
        
        setOrientation(getOrientation() + getSpin());
    }

    public int getSpin() {
        return this.nSpin;
    }


    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }

    @Override
    public void expire() {
        if (getExpire() == 0) {
            CommandCenter.movDebris.remove(this);
        }
        else {
            setExpire(getExpire() - 1);
        }
    }


    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if(getExpire() % 3 == 0){
            setColor(Color.WHITE);
        }else if(getExpire() % 3 == 1){
            setColor(Color.GRAY);
        }else{
            setColor(Color.DARK_GRAY);
        }
        setRadius((int) (getRadius() / 2));

        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a white border
        g.setColor(Color.WHITE);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
