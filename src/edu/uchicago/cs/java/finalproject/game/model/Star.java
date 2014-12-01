package edu.uchicago.cs.java.finalproject.game.model;


import java.awt.*;
import java.util.ArrayList;


/**
 * Created by jakehergott on 11/24/14.
 */
public class Star extends Sprite{

    private int nSpin;

    private final int RAD = 15;

    public Star(){

        //call Sprite constructor
        super();

        setSpin(1);
        setDeltaX(0);
        setDeltaY(0);

        setExpire(1000);
        ArrayList<Point> pntCs = new ArrayList<Point>();

        //draw star
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

        setHitCount(0);

        setColor(Color.WHITE);

        setRadius(RAD);

    }


    //overridden
    public void move(){
        super.move();
        setOrientation(getOrientation() + getSpin());

    }

    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movFoes.remove(this);
        else
            setExpire(getExpire() - 1);
    }

    public int getSpin() {
        return this.nSpin;
    }


    public void setSpin(int nSpin) {
        this.nSpin = nSpin;
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }

}
