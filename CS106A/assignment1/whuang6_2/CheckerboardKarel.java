/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {
/*
 * Karel starts in the left bottom corner and puts beepers down in two
 * different patterns. He puts beepers down in one street, returns
 * to the avenue one position, goes up one avenue, and puts down beepers
 * on the new street and returns to avenue one again, alternating
 * putting beepers down in pattern one and pattern two until he hits
 * the end.
 */
	public void run(){
		putBeeper(); 
		if(frontIsClear()){
			beeperPatternOne();
			turnLeft();
			while(frontIsClear()){
				leftMoveBack();
				reorient();
				beeperPatternTwo();
				turnLeft();
				if(frontIsClear()){
					leftMoveBack();
					reorient();
					putBeeper();
					beeperPatternOne();
					turnLeft();
				}
			}
		}else{
			exception(); //If Karel's world is only one avenue long.
		}
	}
/*
 * The exception accounts for if Karel's world is only one
 * avenue long. Karel goes up the avenue and puts a beeper
 * in each alternating spot.
 */
	private void exception(){
		turnLeft();
		while(frontIsClear()){
			move();
			if(frontIsClear()){
				move();
				putBeeper();
			}
		}
	}
/*
 * The beeper pattern one puts down alternating beepers in
 * odd number avenues except avenue one.
 */
	private void beeperPatternOne(){
		while(frontIsClear()){
			move();
			if(frontIsClear()){
				move();
				putBeeper();
			}
		}
	}
/*
 * The beeper pattern one puts down alternating beepers in
 * even number avenues.
 */
	private void beeperPatternTwo(){
		if(frontIsClear()){
			move();
		}
		while(frontIsClear()){
			putBeeper();
			move();
			if(frontIsClear()){
				move();
			}
		}
		putBeeper();
	}
/*
 * This method turns Karel to the left then moves him back
 * to avenue one.
 */
	private void leftMoveBack(){
		turnLeft();
		while(frontIsClear()){
			move();
		}
	}
/*
 * This method reorients Karel and gets him ready to start
 * on the next street that needs beepers.
 */
	private void reorient(){
		turnRight();
		if(frontIsClear()){
			move();
		}
		turnRight();
	}
}
