/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvasExtension extends GCanvas {
	
/*
 * Instance variables for the different parts of the hangman body.
 */
	private GImage head;
	private GImage body;
	private GImage leftArmHorizontal;
	private GImage leftArmVertical;
	private GImage rightArmHorizontal;
	private GImage rightArmVertical;
	private GImage leftLegHorizontal;
	private GImage leftLegVertical;
	private GImage rightLegHorizontal;
	private GImage rightLegVertical;
	private GImage leftFoot;
	private GImage rightFoot;
	
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
		createTitle();
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
	private GImage createHead(){
		head=new GImage("robothead.jpeg",72+BEAM_LENGTH-HEAD_RADIUS,45+ROPE_LENGTH);
		return head;
	}
/*
 * Sets up and returns the body of the hangman.
 */
	private GImage createBody(){
		body=new GImage("robotbody.jpeg",head.getX()+head.getWidth()/2-body.getWidth()/2,head.getY()+head.getHeight());
		return body;
	}
/*
 * Sets up and returns the horizontal part of the left arm of the hangman.
 */
	private GImage createLeftArmHorizontal(){
		leftArmHorizontal=new GImage("robothorizontalarm.jpeg",body.getX()-leftArmHorizontal.getWidth(),body.getY()+0.4*body.getHeight());
		return leftArmHorizontal;
	}
/*
 * Sets up and returns the vertical part of the left arm of the hangman.
 */
	private GImage createLeftArmVertical(){
		leftArmVertical=new GImage("robotverticalarm.jpeg",leftArmHorizontal.getX(),leftArmHorizontal.getY()+leftArmHorizontal.getHeight());
		return leftArmVertical;
	}
/*
 * Sets up and returns the horizontal part of the right arm of the hangman.
 */
	private GImage createRightArmHorizontal(){
		rightArmHorizontal=new GImage("robothorizontalarm.jpeg",body.getX()+body.getWidth()+rightArmHorizontal.getWidth(),body.getY()+0.4*body.getHeight());
		return rightArmHorizontal;
	}
/*
 * Sets up and returns the vertical part of the right arm of the hangman.
 */
	private GImage createRightArmVertical(){
		rightArmVertical=new GImage("robotverticalarm.jpeg",rightArmHorizontal.getX(),rightArmHorizontal.getY()+rightArmHorizontal.getHeight());
		return rightArmVertical;
	}
/*
 * Sets up and returns the horizontal part of the left leg of the hangman.
 */
	private GImage createLeftLegHorizontal(){
		leftLegHorizontal=new GImage("robothorizontalleg.jpeg",body.getX()-leftLegHorizontal.getWidth(),body.getY()+body.getHeight()-leftLegHorizontal.getHeight());
		return leftLegHorizontal;
	}
/*
 * Sets up and returns the vertical part of the left leg of the hangman.
 */
	private GImage createLeftLegVertical(){
		leftLegVertical=new GImage("robotverticalleg.jpeg",leftLegHorizontal.getX(),leftLegHorizontal.getY()+leftLegHorizontal.getHeight());
		return leftLegVertical;
	}
/*
 * Sets up and returns the horizontal part of the right leg of the hangman.
 */
	private GImage createRightLegHorizontal(){
		rightLegHorizontal=new GImage("robothorizontalleg.jpeg",body.getX()+body.getWidth(),body.getY()+body.getHeight()-rightLegHorizontal.getHeight());
		return rightLegHorizontal;
	}
/*
 * Sets up and returns the vertical part of the right leg of the hangman.
 */
	private GImage createRightLegVertical(){
		rightLegVertical=new GImage("robotverticalleg.jpeg",rightLegHorizontal.getX()+rightLegHorizontal.getWidth()-rightLegVertical.getWidth(),rightLegHorizontal.getY()+rightLegHorizontal.getHeight());
		return rightLegVertical;
	}
/*
 * Sets up and returns the left foot of the hangman.
 */
	private GImage createLeftFoot(){
		leftFoot=new GImage("robotfoot.jpeg",leftLegVertical.getX()-0.5*leftFoot.getWidth(),leftLegVertical.getY()+leftLegVertical.getHeight());
		return leftFoot;
	}
/*
 * Sets up and returns the right foot of the hangman.
 */
	private GImage createRightFoot(){
		rightFoot=new GImage("robotfoot.jpeg",rightLegVertical.getX()-0.5*rightFoot.getWidth(),rightLegVertical.getY()+rightLegVertical.getHeight());
		return rightFoot;
	}
/*
 * Creates the title at the top of the Hangman canvas screen.
 */
	private void createTitle(){
		GLabel title=new GLabel("HANGMAN", 0,0);
		title.move(getWidth()/2-(title.getWidth()/2), title.getAscent()+10);
		title.setFont("Earwig Factory-40");
		add (title);
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