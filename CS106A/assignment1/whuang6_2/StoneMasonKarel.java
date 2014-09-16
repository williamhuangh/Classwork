/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {
/*
 * Karel fixes a column, moves back down the column to street
 * one, and moves over to the next column needed to fix until
 * he finishes fixing the entire world.
 */
	public void run(){
		while(frontIsClear()){
			turnLeft();
			fixOneColumn();
			turnAround();
			moveBack();
			turnLeft();
			nextAvenue();
		}
		turnLeft();
		fixOneColumn();
	}
/*
 * This method tells Karol to fix one of the columns.
 */
	private void fixOneColumn(){
		if(noBeepersPresent()){
			putBeeper();
		} // Puts beeper on Karel's starting position on a column if there is not one there.
		while(frontIsClear()){
			move();
			if(noBeepersPresent()){
				putBeeper();
			}
		} //Puts beeper on Karel's position while he moves up if there is not one there.
	}
/*
 * This method moves Karel back to street one.
 */
	private void moveBack(){
		while(frontIsClear()){
			move();
		}
	}
/*
 * This method moves Karel to the next avenue needed to be fixed.
 */
	private void nextAvenue(){
		move();
		move();
		move();
		move();
	}
}
