/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {
/*
 * Karel finds the midpoint by first filling up all the avenues
 * on street one with beepers, then takes them off one by one 
 * from the sides till only one beeper is left.
 */
	public void run(){
		fillWithBeepers();
		turnAround();
		if(frontIsClear()){
			pickEndBeeper();
			if(frontIsClear()){
				pickEndBeeper();
				beeperRound(); //Finishes when there is only one beeper left.
				turnAround();
				move(); // To get back on the beeper.
			}else dance(); // If Karel's world is two avenues long.
		}else dance(); //If Karel's world is one avenue long.
	}
/*
 * This method fills street one with beepers.
 */
	public void fillWithBeepers(){
		putBeeper();
		while(frontIsClear()){
			move();
			putBeeper();
		}
	}
/*
 * This method is an exception. If the street is only one or
 * two blocks long, Karel will dance.
 */
	public void dance(){
		turnLeft();
		turnLeft();
		turnLeft();
		turnLeft();
	}
/*
 * This method makes Karel move all the way down the street,
 * pick the end beeper on the street, and move back one avenue.
 */
	public void pickEndBeeper(){
		while(frontIsClear()){
			move();
		}
		pickBeeper();
		turnAround();
		move();
	}
/*
 * This recursive function makes Karel move till he finds
 * a spot with no beeper and turn around to pick up the previous
 * beeper, but only if there are still two or more beepers left
 * next to each other.
 */
	public void beeperRound(){
		if(beepersPresent()){
			move();
			if(beepersPresent()){
				while(beepersPresent()){
					move();
				}
				turnAround();
				move();
				pickBeeper();
				move();
				beeperRound();
			}
		}
	}
}
