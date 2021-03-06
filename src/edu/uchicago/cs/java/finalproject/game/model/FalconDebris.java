package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/21/14.
 */
public class FalconDebris extends Sprite{

    private int nSpin;

    private final int RAD = 35;

    public FalconDebris(Point point){

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
            if(getExpire() <= 3) {
                assignRandomShape();
            }
            setExpire(getExpire() - 1);
        }
    }

    //this is for an asteroid only
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

        Arrays.sort(nSides);

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

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if(getExpire() % 3 == 0){
            setColor(Color.ORANGE);
        }else if(getExpire() % 3 == 1){
            setColor(Color.YELLOW);
        }else{
            setColor(Color.RED);
        }
        if(getExpire() <= 3) {
            setRadius((int) (getRadius() * 2));
        }
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a red border
        g.setColor(Color.RED);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
