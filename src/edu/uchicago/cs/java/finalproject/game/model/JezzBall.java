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
        setDeltaX(10);
        setDeltaY(10);

        setExpire(200);
        /*ArrayList<Point> pntCs = new ArrayList<Point>();

        // circle jezzball
        pntCs.add(new Point(0, 10));
        pntCs.add(new Point(5, 5));
        pntCs.add(new Point(10, 0));
        pntCs.add(new Point(5, -5));
        pntCs.add(new Point(0, -10));
        pntCs.add(new Point(-5, -5));
        pntCs.add(new Point(-10, 0));
        pntCs.add(new Point(-5, 5));



        assignPolarPoints(pntCs);*/

        assignRandomShape();

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
        //now draw a white border
        g.setColor(Color.ORANGE);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }

    public void assignRandomShape ()
    {
        int nSide = Game.R.nextInt( 7 ) + 7;
        int nSidesTemp = nSide;

        int[] nSides = new int[nSide];
        for ( int nC = 0; nC < nSides.length; nC++ )
        {
            int n = nC * 48 / nSides.length - 4 + Game.R.nextInt( 8 );
            if ( n >= 48 || n < 0 )
            {
                n = 0;
                nSidesTemp--;
            }
            nSides[nC] = n;
        }

        Arrays.sort( nSides );

        double[]  dDegrees = new double[nSidesTemp];
        for ( int nC = 0; nC <dDegrees.length; nC++ )
        {
            dDegrees[nC] = nSides[nC] * Math.PI / 24 + Math.PI / 2;
        }
        setDegrees( dDegrees);

        double[] dLengths = new double[dDegrees.length];
        for (int nC = 0; nC < dDegrees.length; nC++) {
            if(nC %3 == 0)
                dLengths[nC] = 1 - Game.R.nextInt(40)/100.0;
            else
                dLengths[nC] = 1;
        }
        setLengths(dLengths);

    }

}
