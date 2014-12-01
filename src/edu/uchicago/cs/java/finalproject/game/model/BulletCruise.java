package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jakehergott on 12/1/14.
 */
public class BulletCruise extends Sprite{

    private final double FIRE_POWER = 35.0;



    public BulletCruise(Cruise cruise){

        super();


        //defined the points on a cartesean grid
        ArrayList<Point> pntCs = new ArrayList<Point>();

        pntCs.add(new Point(0,3)); //top point

        pntCs.add(new Point(1,-1));
        pntCs.add(new Point(0,-2));
        pntCs.add(new Point(-1,-1));

        assignPolarPoints(pntCs);

        //a bullet expires after 20 frames
        setExpire( 20 );
        setRadius(6);
        setColor(Color.YELLOW.darker().darker());


        //everything is relative to the falcon ship that fired the bullet
        setDeltaX( cruise.getDeltaX() +
                Math.cos( Math.toRadians( cruise.getOrientation() ) ) * FIRE_POWER );
        setDeltaY( cruise.getDeltaY() +
                Math.sin( Math.toRadians( cruise.getOrientation() ) ) * FIRE_POWER );
        setCenter( cruise.getCenter() );

        //set the bullet orientation to the falcon (ship) orientation
        setOrientation(cruise.getOrientation());


    }

    @Override
    public void move() {

        /*BULLETS FLY OFF SCREEN AND DO NOT WRAP AROUND*/
        Point pnt = getCenter();
        double dX = pnt.x + getDeltaX();
        double dY = pnt.y + getDeltaY();
        setCenter(new Point((int) dX, (int) dY));
    }


    //override the expire method - once an object expires, then remove it from the arrayList.
    public void expire(){
        if (getExpire() == 0)
            CommandCenter.movFriends.remove(this);
        else
            setExpire(getExpire() - 1);
    }
}
