package edu.uchicago.cs.java.finalproject.controller;

import sun.audio.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sound.sampled.Clip;

import edu.uchicago.cs.java.finalproject.game.model.*;
import edu.uchicago.cs.java.finalproject.game.view.*;
import edu.uchicago.cs.java.finalproject.sounds.Sound;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable, KeyListener {

	// ===============================================
	// FIELDS
	// ===============================================

	public static final Dimension DIM = new Dimension(1100, 800); //the dimension of the game.
	private GamePanel gmpPanel;
	public static Random R = new Random();
	public final static int ANI_DELAY = 45; // milliseconds between screen
											// updates (animation)
	private Thread thrAnim;
	private int nLevel = 1;
	private int nTick = 0;
	private ArrayList<Tuple> tupMarkForRemovals;
	private ArrayList<Tuple> tupMarkForAdds;
	private boolean bMuted = true;
	

	private final int PAUSE = 80, // p key
			QUIT = 81, // q key
			LEFT = 37, // rotate left; left arrow
			RIGHT = 39, // rotate right; right arrow
			UP = 38, // thrust; up arrow
            DOWN = 40, //break: down arrow
			START = 83, // s key
			FIRE = 32, // space key
			MUTE = 77, // m-key mute
            NUKE = 78, //n-key does nuke

	// for possible future use
	// HYPER = 68, 					// d key
	// SHIELD = 65, 				// a key arrow
	// NUM_ENTER = 10, 				// hyp
	 SPECIAL = 70; 					// fire special weapon;  F key

	private Clip clpThrust;
	private Clip clpMusicBackground;

	private static final int SPAWN_NEW_SHIP_FLOATER = 800;
    private static final int SPAWN_NEW_JEZZBALL = 450;
    private static final int SPAWN_NEW_STAR = 650;

    private int nCruiseDelay;
    private Cruise cruise;



	// ===============================================
	// ==CONSTRUCTOR
	// ===============================================

	public Game() {

		gmpPanel = new GamePanel(DIM);
		gmpPanel.addKeyListener(this);

		clpThrust = Sound.clipForLoopFactory("whitenoise.wav");
		clpMusicBackground = Sound.clipForLoopFactory("music-background.wav");
	

	}

	// ===============================================
	// ==METHODS
	// ===============================================

	public static void main(String args[]) {


		EventQueue.invokeLater(new Runnable() { // uses the Event dispatch thread from Java 5 (refactored)
					public void run() {
						try {
							Game game = new Game(); // construct itself
							game.fireUpAnimThread();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void fireUpAnimThread() { // called initially
		if (thrAnim == null) {
			thrAnim = new Thread(this); // pass the thread a runnable object (this)
			thrAnim.start();
		}
	}

	// implements runnable - must have run method
	public void run() {

        // lower this thread's priority; let the "main" aka 'Event Dispatch'
		// thread do what it needs to do first
		thrAnim.setPriority(Thread.MIN_PRIORITY);

		// and get the current time
		long lStartTime = System.currentTimeMillis();

		// this thread animates the scene
		while (Thread.currentThread() == thrAnim) {
			tick();
			spawnNewShipFloater();
            spawnJezzball();
            spawnStar();
            reloadCruise();
			gmpPanel.update(gmpPanel.getGraphics()); // update takes the graphics context we must 
														// surround the sleep() in a try/catch block
														// this simply controls delay time between 
														// the frames of the animation

			//this might be a good place to check for collisions
			checkCollisions();
			//this might be a god place to check if the level is clear (no more foes)
			//if the level is clear then spawn some big asteroids -- the number of asteroids 
			//should increase with the level. 
			checkNewLevel();

			try {
				// The total amount of time is guaranteed to be at least ANI_DELAY long.  If processing (update) 
				// between frames takes longer than ANI_DELAY, then the difference between lStartTime - 
				// System.currentTimeMillis() will be negative, then zero will be the sleep time
				lStartTime += ANI_DELAY;
				Thread.sleep(Math.max(0,
						lStartTime - System.currentTimeMillis()));
			} catch (InterruptedException e) {
				// just skip this frame -- no big deal
				continue;
			}
		} // end while
	} // end run


	private void checkCollisions() {

		
		//@formatter:off
		//for each friend in movFriends
			//for each foe in movFoes
				//if the distance between the two centers is less than the sum of their radii
					//mark it for removal
		
		//for each mark-for-removal
			//remove it
		//for each mark-for-add
			//add it
		//@formatter:on
		
		//we use this ArrayList to keep pairs of movMovables/movTarget for either
		//removal or insertion into our arrayLists later on
		tupMarkForRemovals = new ArrayList<Tuple>();
		tupMarkForAdds = new ArrayList<Tuple>();

		Point pntFriendCenter, pntFoeCenter;
		int nFriendRadiux, nFoeRadiux;
        Point pntDebrisCenter;
        int nDebrisRadiux;

		for (Movable movFriend : CommandCenter.movFriends) {
			for (Movable movFoe : CommandCenter.movFoes) {

				pntFriendCenter = movFriend.getCenter();
				pntFoeCenter = movFoe.getCenter();
				nFriendRadiux = movFriend.getRadius();
				nFoeRadiux = movFoe.getRadius();

				//detect collision
				if (pntFriendCenter.distance(pntFoeCenter) < (nFriendRadiux + nFoeRadiux)) {

					//falcon
					if ((movFriend instanceof Falcon) ){
						if (!CommandCenter.getFalcon().getProtected()){
                            if(movFriend.getHitCount() < 2){
                                Sound.playSound("smb_bump.wav");
                                movFriend.setHitCount(movFriend.getHitCount() + 1);
                                CommandCenter.setFalconHitCount(movFriend.getHitCount());
                                Point point = movFriend.getCenter();
                                tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new FalconHitDebris(movFriend,point)));
                            }else {
                                tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                                Point point = movFriend.getCenter();
                                tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new FalconDebris(point)));
                                CommandCenter.spawnFalcon(false);
                                CommandCenter.setFalconHitCount(0);
                            }
							killFoe(movFoe);
						}
					}
                    else if(movFriend instanceof Cruise){
                        //let cruise expire... cruise does not remove after collision
                        killFoe(movFoe);
                    }
					//not the falcon
					else {
						tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
						killFoe(movFoe);
					}//end else 

					//explode/remove foe
					
					
				
				}//end if 
			}//end inner for
		}//end outer for


        for (Movable movFriend : CommandCenter.movFriends) {
            for (Movable movDebris : CommandCenter.movDebris) {

                if(movDebris instanceof BlackHole) {
                    //if (movFriend instanceof Falcon) {

                        pntFriendCenter = movFriend.getCenter();
                        pntDebrisCenter = movDebris.getCenter();
                        nFriendRadiux = movFriend.getRadius();
                        nDebrisRadiux = movDebris.getRadius();

                        if (pntDebrisCenter.distance(pntFriendCenter) < (nDebrisRadiux + nFriendRadiux + 110)) {
                            int nBlackX = (movFriend.getCenter().x + movDebris.getCenter().x) / 2;
                            int nBlackY = (movFriend.getCenter().y + movDebris.getCenter().y) / 2;
                            movFriend.setCenter(new Point(nBlackX, nBlackY));
                        }

                        //detect collision
                        if (pntFriendCenter.distance(pntDebrisCenter) < (nFriendRadiux + nDebrisRadiux)) {

                            //falcon
                            if ((movFriend instanceof Falcon)) {
                                if (!CommandCenter.getFalcon().getProtected()) {
                                    tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                                    Point point = movFriend.getCenter();
                                    tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new FalconBlackHoleDebris(point)));
                                    CommandCenter.spawnFalcon(false);
                                    CommandCenter.setFalconHitCount(0);
                                }
                            }else{ //its a cruise missile
                                tupMarkForRemovals.add(new Tuple(CommandCenter.movFriends, movFriend));
                                nCruiseDelay = 0;
                                Point point = movFriend.getCenter();
                                tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new FalconBlackHoleDebris(point)));
                            }
                        }//end if
                    //}
                }
            }//end inner for
        }



        /*check collisions between debris and foes*/

        for (Movable movDebris : CommandCenter.movDebris) {
            for (Movable movFoe : CommandCenter.movFoes) {

                if((movDebris instanceof JezzballDebris)) {
                    if((movFoe instanceof Asteroid)) {
                        pntDebrisCenter = movDebris.getCenter();
                        pntFoeCenter = movFoe.getCenter();
                        nDebrisRadiux = movDebris.getRadius();
                        nFoeRadiux = movFoe.getRadius();

                        //detect collision
                        if (pntDebrisCenter.distance(pntFoeCenter) < (nDebrisRadiux + nFoeRadiux)) {
                            killFoe(movFoe);
                        }//end else
                    }
                }


                if((movDebris instanceof BlackHole)) {
                    if ((movFoe instanceof Asteroid)) {
                        pntDebrisCenter = movDebris.getCenter();
                        pntFoeCenter = movFoe.getCenter();
                        nDebrisRadiux = movDebris.getRadius();
                        nFoeRadiux = movFoe.getRadius();

                        if (pntDebrisCenter.distance(pntFoeCenter) < (nDebrisRadiux + nFoeRadiux + 200)) {
                            int nBlackX = (movFoe.getCenter().x + movDebris.getCenter().x) / 2;
                            int nBlackY = (movFoe.getCenter().y + movDebris.getCenter().y) / 2;
                            movFoe.setCenter(new Point(nBlackX, nBlackY));
                        }

                        //detect collision
                        if (pntDebrisCenter.distance(pntFoeCenter) < (nDebrisRadiux + nFoeRadiux)) {
                            //we know foe must be asteroid at this point
                            if(((Asteroid) movFoe).getSize() == 0){
                                CommandCenter.setScore(CommandCenter.getScore() + 100 * CommandCenter.getLevel());
                            }else if(((Asteroid) movFoe).getSize() == 1){
                                CommandCenter.setScore(CommandCenter.getScore() + 300 * CommandCenter.getLevel());
                            }else{
                                CommandCenter.setScore(CommandCenter.getScore() + 700 * CommandCenter.getLevel());
                            }
                            int nSpin = ((Asteroid) movFoe).getSpin();
                            Point point = movFoe.getCenter();
                            tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new AsteroidBlackHoleDebris(point)));
                            tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
                        }//end else
                        //explode/remove foe
                    }

                }
            }
        }

		//check for collisions between falcon and floaters
		if (CommandCenter.getFalcon() != null){
			Point pntFalCenter = CommandCenter.getFalcon().getCenter();
			int nFalRadiux = CommandCenter.getFalcon().getRadius();
			Point pntFloaterCenter;
			int nFloaterRadiux;
			
			for (Movable movFloater : CommandCenter.movFloaters) {
				pntFloaterCenter = movFloater.getCenter();
				nFloaterRadiux = movFloater.getRadius();
	
				//detect collision
				if (pntFalCenter.distance(pntFloaterCenter) < (nFalRadiux + nFloaterRadiux)) {
	
					
					tupMarkForRemovals.add(new Tuple(CommandCenter.movFloaters, movFloater));
					Sound.playSound("pacman_eatghost.wav");
                    if(movFloater.getFloaterType() < 5){
                        CommandCenter.setNumFalcons(CommandCenter.getNumFalcons() + 1);
                    }else if(movFloater.getFloaterType() < 8){
                        CommandCenter.setScore(CommandCenter.getScore() + 1500);
                    }else {
                        CommandCenter.setNumNuke(CommandCenter.getNumNuke() + 1);
                    }
	
				}//end if 
			}//end inner for
		}//end if not null
		
		//remove these objects from their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForRemovals) 
			tup.removeMovable();
		
		//add these objects to their appropriate ArrayLists
		//this happens after the above iterations are done
		for (Tuple tup : tupMarkForAdds) 
			tup.addMovable();

		//call garbage collection
		System.gc();
		
	}//end meth

	private void killFoe(Movable movFoe) {
		
		if (movFoe instanceof Asteroid){

			//we know this is an Asteroid, so we can cast without threat of ClassCastException
			Asteroid astExploded = (Asteroid)movFoe;
			//big asteroid 
			if(astExploded.getSize() == 0){
				//spawn two medium Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
                CommandCenter.setScore(CommandCenter.getScore() + 100 * CommandCenter.getLevel());
				
			} 
			//medium size aseroid exploded
			else if(astExploded.getSize() == 1){
				//spawn three small Asteroids
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
				tupMarkForAdds.add(new Tuple(CommandCenter.movFoes,new Asteroid(astExploded)));
                CommandCenter.setScore(CommandCenter.getScore() + 300 * CommandCenter.getLevel());
			}
            else if(astExploded.getSize() == 2){
                int nSpin = ((Asteroid) movFoe).getSpin();
                Point point = movFoe.getCenter();
                tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new AsteroidDebris(nSpin,point)));
                CommandCenter.setScore(CommandCenter.getScore() + 700 * CommandCenter.getLevel());
            }
			//remove the original Foe	
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		}
        else if(movFoe instanceof JezzBall){
            Sound.playSound("gunshot.wav");
            CommandCenter.setScore(CommandCenter.getScore() + 3000);
            int nSpin = ((JezzBall) movFoe).getSpin();
            Point point = movFoe.getCenter();
            tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new JezzballDebris(nSpin,point)));
            //tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new BlackHole(point)));
            tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
        }
        else if(movFoe instanceof Star){
            if(movFoe.getHitCount() < 3){
                movFoe.setHitCount(movFoe.getHitCount() + 1);
                movFoe.setRadius((int)(movFoe.getRadius() * 1.2));
                CommandCenter.setScore(CommandCenter.getScore() + 500);
            }else if(movFoe.getHitCount() < 6){
                movFoe.setColor(Color.YELLOW);
                movFoe.setRadius((int)(movFoe.getRadius() * 1.2));
                movFoe.setHitCount(movFoe.getHitCount() + 1);
                CommandCenter.setScore(CommandCenter.getScore() + 500);
            }else if(movFoe.getHitCount() < 9){
                movFoe.setColor(Color.ORANGE);
                movFoe.setRadius((int)(movFoe.getRadius() * 1.2));
                movFoe.setHitCount(movFoe.getHitCount() + 1);
                CommandCenter.setScore(CommandCenter.getScore() + 500);
            }else if(movFoe.getHitCount() < 12){
                movFoe.setColor(Color.RED);
                movFoe.setRadius((int)(movFoe.getRadius() * 1.2));
                movFoe.setHitCount(movFoe.getHitCount() + 1);
                CommandCenter.setScore(CommandCenter.getScore() + 500);
            }else{
                Sound.playSound("imploding.wav");
                CommandCenter.setScore(CommandCenter.getScore() + 500);
                Point point = movFoe.getCenter();
                tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new StarDebris(point)));
                tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new BlackHole(point)));
                tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
            }
        }
		//not an asteroid
		else {
			//remove the original Foe
			tupMarkForRemovals.add(new Tuple(CommandCenter.movFoes, movFoe));
		}
		
	}

    private void releaseNuke(){
        if(CommandCenter.getNumNuke() > 0) {
            Sound.playSound("explosion-02.wav");
            tupMarkForAdds.add(new Tuple(CommandCenter.movDebris, new NuclearDebris()));
            //protect falcon from getting hit right after nuke and while nuke
            CommandCenter.getFalcon().setProtected(true);
            //kill all the foes from nuke
            for (Movable foe : CommandCenter.movFoes) {
                killFoe(foe);
            }

            //remove these objects from their appropriate ArrayLists
            //this happens after the above iterations are done
            for (Tuple tup : tupMarkForRemovals)
                tup.removeMovable();

            //add these objects to their appropriate ArrayLists
            //this happens after the above iterations are done
            for (Tuple tup : tupMarkForAdds)
                tup.addMovable();

            //call garbage collection
            System.gc();

            CommandCenter.setNumNuke(CommandCenter.getNumNuke() - 1);
        }
    }

    private void releaseCruise(){
        if(nCruiseDelay == 0){
            cruise = new Cruise(CommandCenter.getFalcon());
            CommandCenter.movFriends.add(cruise);
            nCruiseDelay = 100;
            Sound.playSound("tos-photon-torpedo-1.wav");
        }
    }

    private void reloadCruise(){
        if(nCruiseDelay > 83){
            nCruiseDelay = nCruiseDelay - 1;
        }else if(nCruiseDelay > 0){
            nCruiseDelay = nCruiseDelay - 1;
            cruise.setColor(Color.CYAN);
            if(nCruiseDelay % 3 == 0) {
                CommandCenter.movFriends.add(new BulletCruise(cruise));
                Sound.playSound("laser.wav");
            }
        }

    }

	//some methods for timing events in the game,
	//such as the appearance of UFOs, floaters (power-ups), etc. 
	public void tick() {
		if (nTick == Integer.MAX_VALUE)
			nTick = 0;
		else
			nTick++;
	}

	public int getTick() {
		return nTick;
	}

	private void spawnNewShipFloater() {
		//make the appearance of power-up dependent upon ticks and levels
		//the higher the level the more frequent the appearance
		if (nTick % (SPAWN_NEW_SHIP_FLOATER - nLevel * 7) == 0) {
			CommandCenter.movFloaters.add(new NewShipFloater());
		}
	}

	// Called when user presses 's'
	private void startGame() {
		CommandCenter.clearAll();
		CommandCenter.initGame();
		CommandCenter.setLevel(0);
		CommandCenter.setPlaying(true);
		CommandCenter.setPaused(false);
	}

    private void spawnJezzball(){
        if (nTick % (SPAWN_NEW_JEZZBALL - nLevel * 7) == 0){
            CommandCenter.movFoes.add(new JezzBall());
        }
    }

    private void spawnStar(){
        if (nTick % (SPAWN_NEW_STAR - nLevel * 7) == 1){
            CommandCenter.movFoes.add(new Star());
        }
    }

	//this method spawns new asteroids
	private void spawnAsteroids(int nNum) {
		for (int nC = 0; nC < nNum; nC++) {
			//Asteroids with size of zero are big
			CommandCenter.movFoes.add(new Asteroid(0));
		}
	}
	
	
	private boolean isLevelClear(){
		//if there are no more Asteroids on the screen
		
		boolean bAsteroidFree = true;
		for (Movable movFoe : CommandCenter.movFoes) {
			if (movFoe instanceof Asteroid){
				bAsteroidFree = false;
				break;
			}
		}
		
		return bAsteroidFree;

		
	}
	
	private void checkNewLevel(){
		
		if (isLevelClear() ){
			if (CommandCenter.getFalcon() !=null)
				CommandCenter.getFalcon().setProtected(true);
			
			spawnAsteroids(CommandCenter.getLevel() + 2);
			CommandCenter.setLevel(CommandCenter.getLevel() + 1);

		}
	}
	
	
	

	// Varargs for stopping looping-music-clips
	private static void stopLoopingSounds(Clip... clpClips) {
		for (Clip clp : clpClips) {
			clp.stop();
		}
	}

	// ===============================================
	// KEYLISTENER METHODS
	// ===============================================

	@Override
	public void keyPressed(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		// System.out.println(nKey);

		if (nKey == START && !CommandCenter.isPlaying())
			startGame();

		if (fal != null) {

			switch (nKey) {
			case PAUSE:
				CommandCenter.setPaused(!CommandCenter.isPaused());
				if (CommandCenter.isPaused())
					stopLoopingSounds(clpMusicBackground, clpThrust);
				else
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
				break;
			case QUIT:
				System.exit(0);
				break;
			case UP:
				fal.thrustOn();
				if (!CommandCenter.isPaused())
					clpThrust.loop(Clip.LOOP_CONTINUOUSLY);
				break;
            case DOWN:
                fal.breakOn();
                break;
			case LEFT:
				fal.rotateLeft();
				break;
			case RIGHT:
				fal.rotateRight();
				break;

            case NUKE:
                releaseNuke();
                break;


			// possible future use
			// case KILL:
			// case SHIELD:
			// case NUM_ENTER:

			default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Falcon fal = CommandCenter.getFalcon();
		int nKey = e.getKeyCode();
		 System.out.println(nKey);

		if (fal != null) {
			switch (nKey) {
			case FIRE:
				CommandCenter.movFriends.add(new Bullet(fal));
				Sound.playSound("laser.wav");
				break;
				
			//special is a special weapon, current it just fires the cruise missile. 
			case SPECIAL:
                releaseCruise();
				break;
				
			case LEFT:
				fal.stopRotating();
				break;
			case RIGHT:
				fal.stopRotating();
				break;
			case UP:
				fal.thrustOff();
				clpThrust.stop();
				break;
            case DOWN:
                fal.breakOff();
                break;
			case MUTE:
				if (!bMuted){
					stopLoopingSounds(clpMusicBackground);
					bMuted = !bMuted;
				} 
				else {
					clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY);
					bMuted = !bMuted;
				}
				break;
				
				
			default:
				break;
			}
		}
	}

	@Override
	// Just need it b/c of KeyListener implementation
	public void keyTyped(KeyEvent e) {
	}
	

	
}

// ===============================================
// ==A tuple takes a reference to an ArrayList and a reference to a Movable
//This class is used in the collision detection method, to avoid mutating the array list while we are iterating
// it has two public methods that either remove or add the movable from the appropriate ArrayList 
// ===============================================

class Tuple{
	//this can be any one of several CopyOnWriteArrayList<Movable>
	private CopyOnWriteArrayList<Movable> movMovs;
	//this is the target movable object to remove
	private Movable movTarget;
	
	public Tuple(CopyOnWriteArrayList<Movable> movMovs, Movable movTarget) {
		this.movMovs = movMovs;
		this.movTarget = movTarget;
	}
	
	public void removeMovable(){
		movMovs.remove(movTarget);
	}
	
	public void addMovable(){
		movMovs.add(movTarget);
	}

}
