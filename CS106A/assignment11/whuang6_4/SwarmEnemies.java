/*
 * File: SwarmEnemies.java
 * ---------------------
 * Defines the enemies for the game SWARM.
 */

import java.awt.*;
import acm.graphics.*;
import acm.util.*;

public class SwarmEnemies extends GRect implements SwarmConstants{
	
	RandomGenerator rgen= RandomGenerator.getInstance();
	double vx;
	double vy;
/*
 * Constructor for the class SwarmEnemies. Sets up a rectangle and the rate of change of its x
 * and y coordinates randomly.
 */
	public SwarmEnemies(){
		super(MOUSERECT_LENGTH,MOUSERECT_LENGTH);
		setColor(Color.RED);
		setFilled(true);
		vx=rgen.nextDouble(2, 4);
		if(rgen.nextBoolean()==false){
			vx=-vx;
		}
		vy=rgen.nextDouble(2, 4);
		if(rgen.nextBoolean()==false){
			vy=-vy;
		}
	}
/*
 * This method moves the enemy a vx and a vy.
 */
	public void moveEnemy(){
		this.move(vx, vy);
	}
/*
 * This method reverses the sign on the rate of change of x.
 */
	public void reverseX(){
		vx=-vx;
	}
/*
 * This method reverses the sign on the rate of change of y.
 */
	public void reverseY(){
		vy=-vy;
	}
/*
 * This method amplifies the rate of change of x.
 */
	public void cutX(){
		vx*=1.2;
	}
/*
 * This method amplifies the rate of change of y.
 */
	public void cutY(){
		vy*=1.2;
	}
}
