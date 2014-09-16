/*
 * File: PythagoreanTheorem.java
 * Name: 
 * Section Leader: 
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;
/*
 * This program reads in numbers for a and b and prints out the corresponding
 * variable c according to the Pythagorean theorem.
 */
public class PythagoreanTheorem extends ConsoleProgram {
	public void run(){
		println("Enter values to compute the Pythagorean theorem.");
		double a=readDouble("a: ");
		double b=readDouble("b: ");
		double c=calculateC(a,b);
		println("c = "+c);
	}
/*
 * This method calculates the corresponding c for the values of a and b.
 */
	private double calculateC(double a,double b){
		double c=Math.sqrt(a*a+b*b);
		return c;
	}
}

