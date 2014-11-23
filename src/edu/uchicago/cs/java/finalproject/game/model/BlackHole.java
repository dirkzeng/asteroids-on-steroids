package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/23/14.
 */
public class BlackHole extends Sprite{

    private int nSpin;

    //radius of a blackhole
    private final int RAD = 10;

    public BlackHole(Point point){

        //call Sprite constructor
        super();

        setSpin(10000);
        setCenter(point);
        setDeltaX(0);
        setDeltaY(0);

        setExpire(50);

        assignRandomShape();

        setColor(Color.GRAY.darker());

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
            CommandCenter.movDebris.remove(this);
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
        g.setColor(Color.GRAY.darker());
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
}
