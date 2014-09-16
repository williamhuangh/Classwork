/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.program.*;
import acm.graphics.*;
/*
 * This program draws a pyramid made of same-shaped blocks centered at the bottom
 * of the screen. The number of blocks in each row decreases by one
 * as you go up. The block length, width, and number in row 1 is adjustable.
 */
public class Pyramid extends GraphicsProgram {
	public void run() {
		int counter=BRICKS_IN_BASE; //Sets up the number of bricks in each row (subtract 1 each row up)
		int startXCoordinate=(getWidth()-BRICK_WIDTH*BRICKS_IN_BASE)/2; //Sets up the starting x coordinate.
		int startYCoordinate=getHeight()-BRICK_HEIGHT; //Sets up the starting y coordinate.
		for(int i=BRICKS_IN_BASE;i>0;i--){
			drawInARow(startXCoordinate,startYCoordinate,counter);
			startXCoordinate=setUpNextRowX(startXCoordinate);
			startYCoordinate=setUpNextRowY(startYCoordinate);
			counter--;
		}
	}
/*
 * This method draws one row of bricks in the pyramid.
 */
	private void drawInARow(int startXCoordinate,int startYCoordinate,int counter){
		int bricksInTheRow=startXCoordinate;
		for(int k=counter;k>0;k--){
			GRect rect=new GRect(bricksInTheRow,startYCoordinate,BRICK_WIDTH,BRICK_HEIGHT);
			add(rect);
			bricksInTheRow+=BRICK_WIDTH;
		}
	}
/*
 * This method sets up the next row first brick's X coordinate.
 */
	private int setUpNextRowX(int startXCoordinate){
		startXCoordinate+=(BRICK_WIDTH/2);
		return startXCoordinate;
	}
/*
 * This method sets up the next row first brick's Y coordinate.
 */
	private int setUpNextRowY(int startYCoordinate){
		startYCoordinate-=(BRICK_HEIGHT);
		return startYCoordinate;
	}
/*
 * These values are set and can be changed.
 */
	private static final int BRICK_WIDTH=30;
	private static final int BRICK_HEIGHT=12;
	private static final int BRICKS_IN_BASE=14;
}


