package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/21/14.
 */
public class JezzBall extends Sprite {

    private int nSpin;

    //radius of a jezzball
    private final int RAD = 15;

    public JezzBall(){

        //call Sprite constructor
        super();

        setSpin(7);
        setDeltaX(7);
        setDeltaY(7);

        ArrayList<Point> pntCs = new ArrayList<Point>();

        // circle jezzball
        pntCs.add(new Point(0, 10));
        pntCs.add(new Point(5, 5));
        pntCs.add(new Point(10, 0));
        pntCs.add(new Point(5, -5));
        pntCs.add(new Point(0, -10));
        pntCs.add(new Point(-5, -5));
        pntCs.add(new Point(-10, 0));
        pntCs.add(new Point(-5, 5));



        assignPolarPoints(pntCs);

        setColor(Color.RED);

        setRadius(RAD);

    }


    //overridden
    public void move(){
        //super.move();
        Point pnt = getCenter();
        double dX = pnt.x + getDeltaX();
        double dY = pnt.y + getDeltaY();

        //this just keeps the sprite inside the bounds of the frame
        /*THIS ACTUALLY MAKES THE SPRITE BOUNCE*/
        if (pnt.x > getDim().width) {
            //setCenter(new Point(1, pnt.y));
            setCenter(new Point(getDim().width - 1, pnt.y));
            setDeltaX(getDeltaX() * -1);

        } else if (pnt.x < 0) {
            //setCenter(new Point(getDim().width - 1, pnt.y));
            setCenter(new Point(1, pnt.y));
            setDeltaX(getDeltaX() * -1);
        } else if (pnt.y > getDim().height) {
            //setCenter(new Point(pnt.x, 1));
            setCenter(new Point(pnt.x, getDim().height - 1));
            setDeltaY(getDeltaY() * -1);
        } else if (pnt.y < 0) {
            //setCenter(new Point(pnt.x, getDim().height - 1));
            setCenter(new Point(pnt.x, 1));
            setDeltaY(getDeltaY() * -1);
        } else {

            setCenter(new Point((int) dX, (int) dY));
        }

        //a jezzball spins, so you need to adjust the orientation at each move()
        setOrientation(getOrientation() + getSpin());

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
        //now draw a white border
        g.setColor(Color.ORANGE);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }

}
