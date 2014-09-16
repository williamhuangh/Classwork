/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends Karel {
/*
 * Karel goes outside, picks up the newspaper, and returns
 * to his starting position.
 */
	public void run(){
		moveOut();
		pickBeeper();
		turnAround();
		goBack();
	}
/*
 * This method makes it more efficient for Karel to turn right.
 */
	private void turnRight(){
		turnLeft();
		turnLeft();
		turnLeft();
	}
/*
 * This method makes it easier for Karel to turn around.
 */
	private void turnAround(){
		turnLeft();
		turnLeft();
	}
/*
 * This method makes Karel move out of the house.
 */
	private void moveOut(){
		move();
		move();
		turnRight();
		move();
		turnLeft();
		move();
	}
/*
 * This method makes Karel go back to his starting position.
 */
	private void goBack(){
		move();
		turnRight();
		move();
		turnLeft();
		move();
		move();
		turnAround();
	}
}