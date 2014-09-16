/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;
/*
 * This program reads in a number and prints out the Hailstone sequence for that
 * number. If the number is odd, you take 3n+1 and if the number is even, you take
 * half. The sequence continues till it reaches 1 and and program prints out the
 * number of steps it took to reach 1 as well.
 */
public class Hailstone extends ConsoleProgram {
	private int counter;
	public void run(){
		int n=readInt("Enter a number: ");
		counter=0; // To keep track of the number of steps.
		while(n!=1){
			if(n%2==0){
				n=takeHalf(n);
				counter++;
			}else{
				n=makeBigger(n);
				counter++;
			}
		}
		println("The process took "+counter+" to reach 1");
	}
/*
 * This method divides the number by 2 and reassigns the returns n to the
 * new value.
 */
	private int takeHalf(int n){
		print(n+" is even, so I take half: ");
		n/=2;
		println(n);
		return n;
	}
/*
 * This method multiplies the value by 3 and adds 1 and returns the variable
 * n to the new value.
 */
	private int makeBigger(int n){
		print(n+" is odd, so I make 3n+1: ");
		n=3*n+1;
		println(n);
		return n;
	}
}

