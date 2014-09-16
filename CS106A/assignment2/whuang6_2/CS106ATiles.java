/*
 * File: CS106ATiles.java
 * Name: 
 * Section Leader: 
 * ----------------------
 * This file is the starter file for the CS106ATiles problem.
 */

import acm.program.*;
import acm.graphics.*;
/*
 * This class draws 4 rectangles centered around the midpoint with the text "CS106A"
 * centered in each of them.
 */
public class CS106ATiles extends GraphicsProgram {
	public void run(){
		int midpointX=(getWidth()/2);
		int midpointY=(getHeight()/2);
		drawTile(midpointX-(TILE_WIDTH/2)-(TILE_SPACE/2),midpointY-(TILE_HEIGHT/2)-(TILE_SPACE/2));
		drawTile(midpointX+(TILE_SPACE/2)+(TILE_WIDTH/2),midpointY-(TILE_HEIGHT/2)-(TILE_SPACE/2));
		drawTile(midpointX-(TILE_WIDTH/2)-(TILE_SPACE/2),midpointY+(TILE_HEIGHT/2)+(TILE_SPACE/2));
		drawTile(midpointX+(TILE_SPACE/2)+(TILE_WIDTH/2),midpointY+(TILE_HEIGHT/2)+(TILE_SPACE/2));
	}
/*
 * This method draws the tiles on the screen. The parameters passed are the midpoints
 * of the rectangles.
 */
	private void drawTile(double rectMidX,double rectMidY){
		drawRect(rectMidX,rectMidY);
		writeText(rectMidX,rectMidY);
	}
/*
 * This method draws the rectangle depending on the midpoints passed in. It centers the
 * rectangle on this midpoint.
 */
	private void drawRect(double rectMidX,double rectMidY){
		double x=rectMidX-(TILE_WIDTH/2);
		double y=rectMidY-(TILE_HEIGHT/2);
		GRect rect=new GRect(x,y,TILE_WIDTH,TILE_HEIGHT);
		add(rect);
	}
/*
 * This method writes in the text depending on the midpoints passed in. It centers the
 * text on this midpoint.
 */
	private void writeText(double rectMidX,double rectMidY){
		GLabel text=new GLabel("CS106A",0,0);
		add(text);
		double x=rectMidX-(text.getWidth()/2);
		double y=rectMidY+(text.getAscent()/2);
		text.move(x, y);
	}
/*
 * These are the variables that have set values that can be changed.
 */
	private static final double TILE_WIDTH=100;
	private static final double TILE_HEIGHT=75;
	private static final double TILE_SPACE=25;
}
