package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/24/14.
 */
public class StarDebris extends Sprite {

    private int nSpin;

    //radius of a large star
    private final int RAD = 125;


    public StarDebris(Point point){

        //call Sprite constructor
        super();

        setSpin(5);

        setCenter(point);

        ArrayList<Point> pntCs = new ArrayList<Point>();

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

        setColor(Color.WHITE);

        setRadius(RAD);

        setExpire(15);
    }


    //overridden
    public void move(){
        super.move();
        //an asteroid spins, so you need to adjust the orientation at each move()
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
        if (getExpire() == 0)
            CommandCenter.movDebris.remove(this);
        else
            setExpire(getExpire() - 1);
    }



    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if(getExpire() % 2 == 0){
            setColor(Color.GRAY);
        }else{
            setColor(Color.DARK_GRAY);
        }
        setRadius((int) (getRadius() / 1.2));
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
