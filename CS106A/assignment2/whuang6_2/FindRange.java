/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;
/*
 * This class reads in a list of integers and prints out the largest
 * and the smallest.
 */
public class FindRange extends ConsoleProgram {
	public void run(){
		println("This program finds the largest and smallest numbers.");
		int largest=readInt("?");
		int smallest=largest;
		if (largest==0){
			println("No values have been entered.");
		} else{
			findLargeAndSmall(smallest,largest);
		}
	}
/*
 * This method finds the largest and smallest values of the entered integers
 * until the sentinal is entered and calls the printResult method.
 */
	private void findLargeAndSmall(int smallest, int largest){
		while(true){
			int a=readInt("?");
			if (a==SENTINAL){
				break;
			} else if(a>largest){
				largest=a;
			} else if(a<smallest){
				smallest=a;
			}
		}
		printResult(smallest,largest);
	}
/*
 * This method prints out the final smallest and largest values.
 */
	private void printResult(int smallest,int largest){
		println("smallest: "+smallest);
		println("largest: "+largest);
	}
/*
 * This is the sentinal value.
 */
	private static final int SENTINAL=0;
}