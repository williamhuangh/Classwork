/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	
/*
 * Instance variables for the different parts of the hangman body.
 */
	private GOval head;
	private GLine body;
	private GLine leftArmHorizontal;
	private GLine leftArmVertical;
	private GLine rightArmHorizontal;
	private GLine rightArmVertical;
	private GLine leftLegHorizontal;
	private GLine leftLegVertical;
	private GLine rightLegHorizontal;
	private GLine rightLegVertical;
	private GLine leftFoot;
	private GLine rightFoot;
	
/*
 * Sets up the original word display of dashes as a GLabel on the canvas.
 */
	private GLabel wordDisplay= new GLabel("",0,0);
	
/*
 * Initially sets the wrong answer String to an instance variable and empty.
 * Sets guess number to 0 as an instance variable.
 */
	private String wrongAnswers="";
	private int guessNumber=0;
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		wordDisplay= new GLabel("",0,0);
		guessNumber=0;
		createScaffold();
		wrongAnswers="";
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		GLabel oldDisplay=wordDisplay;
		remove(oldDisplay); // Removes the old display and resets the new one.
		wordDisplay=new GLabel(word,72,430);
		wordDisplay.setFont("Sans Serif-15");
		add(wordDisplay);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		drawBodyPart();
		wrongAnswers+=letter;
		GLabel wrongAnswersDisplay=new GLabel(wrongAnswers, 72, 450); // Draws over the old GLabel because it's the same except 1 more letter added.
		wrongAnswersDisplay.setFont("Sans Serif-15");
		add(wrongAnswersDisplay);
	}
/*
 * Sets up all body parts and adds to the canvas whatever body party corresponds with how many guesses the
 * player has gone through.
 */
	private void drawBodyPart(){
		head=createHead();
		body=createBody();
		leftArmHorizontal=createLeftArmHorizontal();
		leftArmVertical=createLeftArmVertical();
		rightArmHorizontal=createRightArmHorizontal();
		rightArmVertical=createRightArmVertical();
		leftLegHorizontal=createLeftLegHorizontal();
		leftLegVertical=createLeftLegVertical();
		rightLegHorizontal=createRightLegHorizontal();
		rightLegVertical=createRightLegVertical();
		leftFoot= createLeftFoot();
		rightFoot= createRightFoot();
		switch(guessNumber){
			case 0: 
				add(head);
				guessNumber++;
				break;
				
			case 1:
				add(body);
				guessNumber++;
				break;
				
			case 2:
				add(leftArmHorizontal);
				add(leftArmVertical);
				guessNumber++;
				break;
				
			case 3:
				add(rightArmHorizontal);
				add(rightArmVertical);
				guessNumber++;
				break;
				
			case 4:
				add(leftLegHorizontal);
				add(leftLegVertical);
				guessNumber++;
				break;
				
			case 5:
				add(rightLegHorizontal);
				add(rightLegVertical);
				guessNumber++;
				break;
				
			case 6:
				add(leftFoot);
				guessNumber++;
				break;
				
			case 7:
				add(rightFoot);
				guessNumber++;
				break;
				
			default:
				break;
		}
	}
/*
 * Draws in the scaffold on the canvas.
 */
	private void createScaffold(){
		GLine groundConnect=new GLine(72,45,72,45+SCAFFOLD_HEIGHT);
		add(groundConnect);
		GLine horizontalComponent=new GLine(72,45,72+BEAM_LENGTH,45);
		add(horizontalComponent);
		GLine rope=new GLine(72+BEAM_LENGTH,45,72+BEAM_LENGTH,45+ROPE_LENGTH);
		add(rope);
	}
/*
 * Sets up and returns the head of the hangman.
 */
	private GOval createHead(){
		head=new GOval(72+BEAM_LENGTH-HEAD_RADIUS,45+ROPE_LENGTH,2*HEAD_RADIUS,2*HEAD_RADIUS);
		return head;
	}
/*
 * Sets up and returns the body of the hangman.
 */
	private GLine createBody(){
		body=new GLine(head.getX()+HEAD_RADIUS,head.getY()+2*HEAD_RADIUS,head.getX()+HEAD_RADIUS,head.getY()+2*HEAD_RADIUS+BODY_LENGTH);
		return body;
	}
/*
 * Sets up and returns the horizontal part of the left arm of the hangman.
 */
	private GLine createLeftArmHorizontal(){
		leftArmHorizontal=new GLine(head.getX()+HEAD_RADIUS,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD,head.getX()+HEAD_RADIUS-UPPER_ARM_LENGTH,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD);
		return leftArmHorizontal;
	}
/*
 * Sets up and returns the vertical part of the left arm of the hangman.
 */
	private GLine createLeftArmVertical(){
		leftArmVertical=new GLine(head.getX()+HEAD_RADIUS-UPPER_ARM_LENGTH,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD,head.getX()+HEAD_RADIUS-UPPER_ARM_LENGTH,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD+LOWER_ARM_LENGTH);
		return leftArmVertical;
	}
/*
 * Sets up and returns the horizontal part of the right arm of the hangman.
 */
	private GLine createRightArmHorizontal(){
		rightArmHorizontal=new GLine(head.getX()+HEAD_RADIUS,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD,head.getX()+HEAD_RADIUS+UPPER_ARM_LENGTH,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD);
		return rightArmHorizontal;
	}
/*
 * Sets up and returns the vertical part of the right arm of the hangman.
 */
	private GLine createRightArmVertical(){
		rightArmVertical=new GLine(head.getX()+HEAD_RADIUS+UPPER_ARM_LENGTH,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD,head.getX()+HEAD_RADIUS+UPPER_ARM_LENGTH,head.getY()+2*HEAD_RADIUS+ARM_OFFSET_FROM_HEAD+LOWER_ARM_LENGTH);
		return rightArmVertical;
	}
/*
 * Sets up and returns the horizontal part of the left leg of the hangman.
 */
	private GLine createLeftLegHorizontal(){
		leftLegHorizontal=new GLine(head.getX()+HEAD_RADIUS,head.getY()+2*HEAD_RADIUS+BODY_LENGTH,head.getX()+HEAD_RADIUS-HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH);
		return leftLegHorizontal;
	}
/*
 * Sets up and returns the vertical part of the left leg of the hangman.
 */
	private GLine createLeftLegVertical(){
		leftLegVertical=new GLine(head.getX()+HEAD_RADIUS-HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH,head.getX()+HEAD_RADIUS-HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH);
		return leftLegVertical;
	}
/*
 * Sets up and returns the horizontal part of the right leg of the hangman.
 */
	private GLine createRightLegHorizontal(){
		rightLegHorizontal=new GLine(head.getX()+HEAD_RADIUS,head.getY()+2*HEAD_RADIUS+BODY_LENGTH,head.getX()+HEAD_RADIUS+HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH);
		return rightLegHorizontal;
	}
/*
 * Sets up and returns the vertical part of the right leg of the hangman.
 */
	private GLine createRightLegVertical(){
		rightLegVertical=new GLine(head.getX()+HEAD_RADIUS+HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH,head.getX()+HEAD_RADIUS+HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH);
		return rightLegVertical;
	}
/*
 * Sets up and returns the left foot of the hangman.
 */
	private GLine createLeftFoot(){
		leftFoot=new GLine(head.getX()+HEAD_RADIUS-HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH,head.getX()+HEAD_RADIUS-HIP_WIDTH-FOOT_LENGTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH);
		return leftFoot;
	}
/*
 * Sets up and returns the right foot of the hangman.
 */
	private GLine createRightFoot(){
		rightFoot=new GLine(head.getX()+HEAD_RADIUS+HIP_WIDTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH,head.getX()+HEAD_RADIUS+HIP_WIDTH+FOOT_LENGTH,head.getY()+2*HEAD_RADIUS+BODY_LENGTH+LEG_LENGTH);
		return rightFoot;
	}

/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

}
