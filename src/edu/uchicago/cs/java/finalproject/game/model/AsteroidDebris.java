package edu.uchicago.cs.java.finalproject.game.model;

import edu.uchicago.cs.java.finalproject.controller.Game;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by jakehergott on 11/21/14.
 */
public class AsteroidDebris extends Sprite {

    private int nSpin;

    private final int RAD = 25;


    public AsteroidDebris(int nSpin, Point point){

        //call Sprite constructor
        super();

        setSpin(nSpin);
        setCenter(point);
        setColor(Color.RED);

        assignRandomShape();

        setRadius(RAD);

        setExpire(5);
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
            assignRandomShape();
            setExpire(getExpire() - 1);
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
        if(getExpire() % 2 == 0){
            setColor(Color.YELLOW);
        }else{
            setColor(Color.RED);
        }
        //fill this polygon (with whatever color it has)
        g.fillPolygon(getXcoords(), getYcoords(), dDegrees.length);
        //now draw a orange border
        g.setColor(Color.ORANGE);
        g.drawPolygon(getXcoords(), getYcoords(), dDegrees.length);
    }
}
