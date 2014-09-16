/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.program.*;
import acm.graphics.*;
import java.awt.*;
/*
 * This program draws a Target logo centered in the middle of the screen.
 * The radius of the 3 circles are adjustable.
 */
public class Target extends GraphicsProgram {
	private double outerRadiusPixels;
	private double whiteRadiusPixels;
	private double innerRadiusPixels;
	public void run(){
		outerRadiusPixels=convertToPixels(OUTER_RADIUS_INCHES);
		whiteRadiusPixels=convertToPixels(WHITE_RADIUS_INCHES);
		innerRadiusPixels=convertToPixels(INNER_RADIUS_INCHES);
		drawCircle((double)getWidth()/2-outerRadiusPixels,(double)getHeight()/2-outerRadiusPixels,outerRadiusPixels*2,true);
		drawCircle((double)getWidth()/2-whiteRadiusPixels,(double)getHeight()/2-whiteRadiusPixels,whiteRadiusPixels*2,false);
		drawCircle((double)getWidth()/2-innerRadiusPixels,(double)getHeight()/2-innerRadiusPixels,innerRadiusPixels*2,true);
	}
/*
 * This method converts the radii in inches to the radii in pixels.
 */
	private double convertToPixels(double inches){
		double pixels=inches*72;
		return pixels;
	}
/*
 * This method takes in the coordinates of a circle, the diameter, a boolean, and draws the circle..
 * If the boolean is true, the circle is colored red and if the boolean is false, the circle is colored
 * white.
 */
	private void drawCircle(double radiusX,double radiusY,double radiusDiameter,boolean isColored){
		GOval circle=new GOval(radiusX,radiusY,radiusDiameter,radiusDiameter);
		if(isColored==true){
			circle.setColor(Color.RED);
			circle.setFilled(true);
			add(circle);
		} else{
			circle.setColor(Color.WHITE);
			circle.setFilled(true);
			add(circle);
		}
	}
/*
 * These values are set and can be changed.
 */
	private static final double OUTER_RADIUS_INCHES=1.0;
	private static final double WHITE_RADIUS_INCHES=0.65;
	private static final double INNER_RADIUS_INCHES=0.3;
}
