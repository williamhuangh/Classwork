/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */
import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels.  On some platforms 
  * these may NOT actually be the dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board.  On some platforms these may NOT actually
  * be the dimensions of the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/** The ball and paddle use in the game */
	private GRect paddle;
	private GOval ball;
	
/** The number of pixels in the x and y directions that the ball moves at a time*/
	private double vx;
	private double vy=3.0;
	
/** Number of lives the player has*/
	private int lives=NTURNS;
	
/** Number of bricks still in the game*/
	private int bricksLeftCounter=NBRICKS_PER_ROW*NBRICK_ROWS;
	
/** Random generator to generate random values*/
	private RandomGenerator rgen= RandomGenerator.getInstance(); 
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		GLabel clickToPlay=centerMessage("Click to Play");
		waitForClick();
		remove(clickToPlay); // After the user clicks, the game begins.
		setUpBricks();
		setUpPaddle();
		addMouseListeners(); //For the paddle.
		while (lives>0){ // Keep playing till you run out of lives.
			setUpAndMoveBall();
			if(bricksLeftCounter==0){ // If there are no more bricks, you win.
				centerMessage("You Win!");
				break;
			}
		}
		if (lives==0){ // If you have no more lives, you lose.
			centerMessage("You Lose!");
		}
	}
/*
 * This method sets up all the bricks at the top of the window.
 */
	private void setUpBricks(){
		int rowYCoordinate=BRICK_Y_OFFSET;
		int midpointXCoordinate=getWidth()/2;
		for(int i=1;i<NBRICK_ROWS+1;i++){ // Sets up one row until i increments to the final integer number of brick rows.
			setUpOneRow(i,rowYCoordinate,midpointXCoordinate);
			rowYCoordinate+=(BRICK_HEIGHT+BRICK_SEP);
		}
	}
/*
 * This method takes in the row number, starting Y coordinate, and midpoint X coordinate to set up
 * one row of bricks. The top 2 rows are colored red, next 2 are orange, next 2 are yellow, next 2
 * are green, and next 2 are cyan.
 */
	private void setUpOneRow(int rowNumber,int rowYCoordinate,int midpointXCoordinate){
		double blockXCoordinate=midpointXCoordinate-((NBRICKS_PER_ROW/2.0)*BRICK_WIDTH)-((NBRICKS_PER_ROW/2.0-0.5)*BRICK_SEP);
		// Sets up the first brick's X coordinate.
		for(int i=0;i<NBRICKS_PER_ROW;i++){
			GRect brick=new GRect(blockXCoordinate,rowYCoordinate,BRICK_WIDTH,BRICK_HEIGHT);
			switch(rowNumber){
				case 1:
				case 2:
					brick.setColor(Color.RED);
					brick.setFilled(true);
					break;
				case 3:
				case 4:
					brick.setColor(Color.ORANGE);
					brick.setFilled(true);
					break;
				case 5:
				case 6:
					brick.setColor(Color.YELLOW);
					brick.setFilled(true);
					break;
				case 7:
				case 8:
					brick.setColor(Color.GREEN);
					brick.setFilled(true);
					break;
				case 9:
				case 10:
					brick.setColor(Color.CYAN);
					brick.setFilled(true);
					break;
				default:
					break;
			}
			add(brick);
			blockXCoordinate+=(BRICK_WIDTH+BRICK_SEP); // Sets up next brick's X coordinate.
		}
	}
/*
 * This method sets up the starting coordinates of the paddle.
 */
	private void setUpPaddle(){
		int midpointX=getWidth()/2;
		paddle=new GRect(midpointX-PADDLE_WIDTH/2,getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setColor(Color.BLACK);
		paddle.setFilled(true);
		add(paddle);
	}
/*
 * This method sets up the x-velocity of the ball, using a random number generator to generate a random
 * number between 1.0 and 3.0 and randomly assigns either positive or negative velocities.
 */
	private void setUpBallVelocity(){
		vx = rgen.nextDouble(1.0, 3.0); 
		if (rgen.nextBoolean(0.5)){
			vx = -vx;
		}
	}
/*
 * This method moves the ball in the window and interacts with the things that it 
 * contacts.
 */
	private void setUpAndMoveBall(){
		GLabel clickToBegin=centerMessage("Click to Drop Ball");
		clickToBegin.move(0, -BALL_RADIUS*4);
		setUpBall();
		waitForClick(); // After the user clicks, the ball begins moving.
		remove(clickToBegin);
		boolean ballIsAlive=true;
		while(ballIsAlive==true){
			ballIsAlive=calculateBallMovements();
			if (ballIsAlive==false){
				// If boolean ballIsAlive is false, subtract 1 life, reset the lives remaining display and breaks the while loop.
				lives--;
				break;
			}
			checkCollisions(); // Method to check if ball collides with anything.
			if(bricksLeftCounter==0){ // If all bricks are gone, break the loop.
				break;
			}
		}
	}
/*
 * This method sets up the initial position and state of the ball.
 */
	private void setUpBall(){
		int ballXCoordinate=getWidth()/2-BALL_RADIUS;
		int ballYCoordinate=getHeight()/2-BALL_RADIUS;
		ball=new GOval(ballXCoordinate,ballYCoordinate,BALL_RADIUS*2,BALL_RADIUS*2);
		ball.setColor(Color.BLACK);
		ball.setFilled(true);
		add(ball);
		pause(200);
		setUpBallVelocity();
	}
/*
 * This method takes in a String and centers it as a GLabel into the middle of the screen.
 */
	private GLabel centerMessage(String message){
		GLabel msg=new GLabel(message,getWidth()/2,getHeight()/2);
		msg.move(-msg.getWidth()/2,-msg.getAscent()/2);
		add(msg);
		return msg;
	}
/*
 * This method moves the ball and returns true if the ball is still alive and false if the ball hit the bottom boundary
 * of the window.
 */
	private boolean calculateBallMovements(){
		if((ball.getX()+BALL_RADIUS*2)>=getWidth()){
			ball.move(-((ball.getX()+BALL_RADIUS*2)-getWidth()), 0);
			vx=-vx;
		} else if(ball.getX()<=0){
			ball.move(-ball.getX(),0);
			vx=-vx;
		}
		if(ball.getY()<=0){
			ball.move(0, -ball.getY());
			vy=-vy;
		} else if((ball.getY()+BALL_RADIUS*2>=getHeight())){
			ball.move(0,-(ball.getY()+BALL_RADIUS*2-getHeight()));
			remove(ball);
			return false;
		}
		ball.move(vx, vy);
		pause(10);
		return true;
	}
/*
 * This method makes the ball react to collisions.
 */
	private void checkCollisions(){
		GObject collider=getCollidingObject();
		if (collider==paddle){
			// If it collides with the paddle, reverses vy.
			ball.move(0, paddle.getY()-ball.getY()-BALL_RADIUS*2);
			// This ball movement removes the "ball sticking to the paddle" issue because it moves the ball to the top of the paddle.
			vy=-vy;
		} else if (collider!=null){
			// If it hits a brick remove it, subtract 1 from the bricks remaining, and reverse vy.
			remove(collider);
			bricksLeftCounter--;
			vy=-vy;
		}
	}
/*
 * This method checks for the object that collided with the four corners of the square that encircles the circle.
 * Returns the GObject or if nothing collided, return null.
 */
	private GObject getCollidingObject(){
		if(getElementAt(ball.getX(),ball.getY()+BALL_RADIUS*2)!=null){
			return getElementAt(ball.getX(),ball.getY()+BALL_RADIUS*2);
		} else if(getElementAt(ball.getX()+BALL_RADIUS*2,ball.getY()+BALL_RADIUS*2)!=null){
			return getElementAt(ball.getX()+BALL_RADIUS*2,ball.getY()+BALL_RADIUS*2);
		} else if(getElementAt(ball.getX(),ball.getY())!=null){
			return getElementAt(ball.getX(),ball.getY());
		} else if(getElementAt(ball.getX()+BALL_RADIUS*2,ball.getY())!=null){
			return getElementAt(ball.getX()+BALL_RADIUS*2,ball.getY());
		}
		return null;
	}
/*
 * This method detects the mouse movements and moves the paddle accordingly.
 * Does not allow paddle to go off the window.
 */
	public void mouseMoved(MouseEvent e){
		if(e.getX()>=PADDLE_WIDTH/2 && e.getX()<=(getWidth()-PADDLE_WIDTH/2)){
			paddle.setLocation(e.getX()-PADDLE_WIDTH/2, getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
		}
	}
}