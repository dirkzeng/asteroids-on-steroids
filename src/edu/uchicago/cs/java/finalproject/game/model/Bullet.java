package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;
import java.util.ArrayList;

import edu.uchicago.cs.java.finalproject.controller.Game;


public class Bullet extends Sprite {

	  private final double FIRE_POWER = 35.0;

	 
	
public Bullet(Falcon fal){
		
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
        setColor(Color.YELLOW);
	    

	    //everything is relative to the falcon ship that fired the bullet
	    setDeltaX( fal.getDeltaX() +
	               Math.cos( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setDeltaY( fal.getDeltaY() +
	               Math.sin( Math.toRadians( fal.getOrientation() ) ) * FIRE_POWER );
	    setCenter( fal.getCenter() );

	    //set the bullet orientation to the falcon (ship) orientation
	    setOrientation(fal.getOrientation());


	}

    @Override
    public void move() {

        Point pnt = getCenter();
        double dX = pnt.x + getDeltaX();
        double dY = pnt.y + getDeltaY();
        setCenter(new Point((int) dX, (int) dY));

        /*BULLETS FLY OFF SCREEN AND DO NOT WRAP AROUND*/

        //this just keeps the sprite inside the bounds of the frame
        /*if (pnt.x > getDim().width) {
            setCenter(new Point(1, pnt.y));

        } else if (pnt.x < 0) {
            setCenter(new Point(getDim().width - 1, pnt.y));
        } else if (pnt.y > getDim().height) {
            setCenter(new Point(pnt.x, 1));
        } else if (pnt.y < 0) {
            setCenter(new Point(pnt.x, getDim().height - 1));
        } else {

            setCenter(new Point((int) dX, (int) dY));
        }*/

    }

    //override the expire method - once an object expires, then remove it from the arrayList. 
	public void expire(){
 		if (getExpire() == 0)
 			CommandCenter.movFriends.remove(this);
		 else 
			setExpire(getExpire() - 1);
	}

}
