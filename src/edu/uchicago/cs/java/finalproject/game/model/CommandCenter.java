package edu.uchicago.cs.java.finalproject.game.model;

import java.util.concurrent.CopyOnWriteArrayList;

import edu.uchicago.cs.java.finalproject.sounds.Sound;

// I only want one Command Center and therefore this is a perfect candidate for static
// Able to get access to methods and my movMovables ArrayList from the static context.
public class CommandCenter {

	private static int nNumFalcon;
    private static int nFalconHitCount = 0;
    private static int nNuke;
	private static int nLevel;
	private static long lScore;
    private static long lHighScore = 0;
	private static Falcon falShip;
	private static boolean bPlaying;
	private static boolean bPaused;
	
	// These ArrayLists are thread-safe
	public static CopyOnWriteArrayList<Movable> movDebris = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFriends = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFoes = new CopyOnWriteArrayList<Movable>();
	public static CopyOnWriteArrayList<Movable> movFloaters = new CopyOnWriteArrayList<Movable>();
	

	// Constructor made private - static Utility class only
	private CommandCenter() {}
	
	public static void initGame(){
		setLevel(1);
		setScore(0);
		setNumFalcons(3);
        setNumNuke(1);
		spawnFalcon(true);
	}
	
	// The parameter is true if this is for the beginning of the game, otherwise false
	// When you spawn a new falcon, you need to decrement its number
	public static void spawnFalcon(boolean bFirst) {

		if (getNumFalcons() != 0) {
			falShip = new Falcon();
			movFriends.add(falShip);
			if (!bFirst)
			    setNumFalcons(getNumFalcons() - 1);
		}
        Sound.playSound("shipspawn.wav");
	}
	
	public static void clearAll(){
		movDebris.clear();
		movFriends.clear();
		movFoes.clear();
		movFloaters.clear();
	}

	public static boolean isPlaying() {
		return bPlaying;
	}

	public static void setPlaying(boolean bPlaying) {
		CommandCenter.bPlaying = bPlaying;
	}

	public static boolean isPaused() {
		return bPaused;
	}

	public static void setPaused(boolean bPaused) {
		CommandCenter.bPaused = bPaused;
	}
	
	public static boolean isGameOver() {		//if the number of falcons is zero, then game over
        /*set high score to score if score is greater than high score*/
        if(lScore > lHighScore){
            lHighScore = lScore;
        }
        if (getNumFalcons() == 0) {
            Sound.playSound("smb_gameover.wav");
			return true;
		}
		return false;
	}

    public static long getHighScore(){
        return lHighScore;
    }

	public static int getLevel() {
		return nLevel;
	}

	public  static long getScore() {
		return lScore;
	}

	public static void setScore(long lParam) {
		lScore = lParam;
	}

	public static void setLevel(int n) {
		nLevel = n;
	}

	public static int getNumFalcons() {
		return nNumFalcon;
	}

	public static void setNumFalcons(int nParam) {
		nNumFalcon = nParam;
	}

    public static int getNumNuke(){
        return nNuke;
    }

    public static void setNumNuke(int nParam){
        nNuke = nParam;
    }

    public static int getFalconHitCount(){
        return nFalconHitCount;
    }

    public static void setFalconHitCount(int n){
        nFalconHitCount = n;
    }

	
	public static Falcon getFalcon(){
		return falShip;
	}
	
	public static void setFalcon(Falcon falParam){
		falShip = falParam;
	}

	public static CopyOnWriteArrayList<Movable> getMovDebris() {
		return movDebris;
	}



	public static CopyOnWriteArrayList<Movable> getMovFriends() {
		return movFriends;
	}



	public static CopyOnWriteArrayList<Movable> getMovFoes() {
		return movFoes;
	}


	public static CopyOnWriteArrayList<Movable> getMovFloaters() {
		return movFloaters;
	}


	
	
}
