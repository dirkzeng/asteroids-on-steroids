package edu.uchicago.cs.java.finalproject.game.model;

import java.awt.*;

public interface Movable {
	//for the game to draw
	public void move();
	public void draw(Graphics g);

	//for collisions
	//if two objects are both friends or both foes, then nothing happens, otherwise exlode both
	//the friend type may be DEBRIS, in which case it is inert
	//public byte getFriend();
	//when a foe explodes, your points increase
	public int points();
	
	public int getFloaterType(); //only for NewShipFloater

	public Point getCenter();
	public int getRadius();
	//for short-lasting objects only like powerUps, bullets, special weapons and UFOs
	 //controls nExpiry that clicks down to expire, then the object should remove itself
	//see Bullet class for how this works. 
	public void expire();
	//for fading objects
	public void fadeInOut();




    public void setDeltaX(double dSet);

    public void setDeltaY(double dSet);

    public double getDeltaY();

    public double getDeltaX();

    public void setCenter(Point pntParam);

    public void setRadius(int n);

    public void setColor(Color col);

    public int getHitCount();

    public void setHitCount(int n);

    public int getOrientation();
} //end Movable
