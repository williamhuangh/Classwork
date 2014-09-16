/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
/*
 * This program plays the Hangman Game.
 */
public class HangmanExtension extends ConsoleProgram{
	
/*
 * Instance variables for the lexicon, random generator, the number of guesses
 * at the beginning of the game, and the hangman canvas.
 */
	private HangmanLexicon hangmanLex= new HangmanLexicon();
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private static final int GUESSES=8;
	private HangmanCanvasExtension canvas;
	
/*
 * Initializes the canvas at the right half of the screen.
 */
	public void init() { 
		 canvas = new HangmanCanvasExtension(); 
		 add(canvas); 
	}
    public void run() {
    	while(true){
	    	canvas.reset(); // Resets the canvas for the new game.
	    	int guessesLeft=GUESSES;
			int randomSelectionNumber=rgen.nextInt(hangmanLex.getWordCount()); 
			String word=hangmanLex.getWord(randomSelectionNumber); // Randomly selects a word from the lexicon.
			String wordGuessDisplay=setUpGuessDisplay(word); // Sets up the beginning word display on the left side.
			String currentGuess;
			println("Welcome to Hangman!");
			while(guessesLeft>0){ // While the player still has guesses left, the game goes on.
				println("The word now looks like this: "+wordGuessDisplay);
				canvas.displayWord(wordGuessDisplay); // Displays the word on the canvas (below the hangman).
				println("You have "+guessesLeft+" guesses left.");
				currentGuess=readLine("Your guess: "); // Reads player guess.
				while(checkLine(currentGuess)==false||currentGuess==null){ // Checks for if the input by the user is a valid input.
					println("Please guess a single character in the alphabet.");
					currentGuess=readLine("Your guess: ");
				}
				currentGuess=convertUpperCase(currentGuess); // Converts the guessed letter into uppercase if not already.
				if(word.contains(currentGuess)){ // If the word has the guessed letter, change the display and print out a message.
					wordGuessDisplay=changeWordDisplay(word, currentGuess, wordGuessDisplay);
					println("Your guess is correct.");
				} else{ //If the guess is not in the word, subtract the guesses left and record the incorrect guess on the canvas.
					println("There are no "+currentGuess+"'s in the word.");
					guessesLeft--;
					canvas.noteIncorrectGuess(currentGuess.charAt(0));
				}
				if(wordGuessDisplay.equals(word)){ // If the current word display is the entire word, the player has guessed all the letter and won.
					canvas.displayWord(wordGuessDisplay);
					println("You guessed the word: "+ word);
					println("You win.");
					break;
				}
			}
			if(!wordGuessDisplay.equals(word)){ // If the word display doesn't equal the original word once the loop breaks, you lose.
				println("You are completely hung.");
				println("The word was: "+word);
				println("You lose.");
			}
			if(wantToPlay()==false){ // If the player does not want to play again, breaks the loop.
				break;
			}
    	}
    	println("Thanks for playing!");
	}
/*
 * Sets up the first display of the word before the player guesses anything.
 * The number of dashes equals the number of letters in the word.
 */
    private String setUpGuessDisplay(String word){
    	String result="";
    	for(int i=0;i<word.length();i++){
    		result+="-";
    	}
    	return result;
    }
/*
 * This method takes in the current guess and returns true if it is a valid one-character guess.
 */
    private boolean checkLine(String currentGuess){
    	boolean upperCase=(currentGuess.charAt(0)>='A' && currentGuess.charAt(0)<='Z');
    	boolean lowerCase=(currentGuess.charAt(0)>='a' && currentGuess.charAt(0)<='z');
    	if(currentGuess.length()==1 && (upperCase||lowerCase)){
    		return true;
    	} else{
    		return false;
    	}
    }
/*
 * This method converts the String to uppercase and returns the uppercase version.
 */
    private String convertUpperCase(String str){
    	String convertedString="";
    	if(str.charAt(0)>'Z'){
    		convertedString+=(char) (str.charAt(0)-'a'+'A');
    	} else{
    		convertedString+= str.charAt(0);
    	}
    	return convertedString;
    }
/*
 * This method takes in the word from the lexicon, the current guess, and the current word display.
 * It then checks to see if the word has the current guess and if so, it corrects the word display so that
 * it accounts for the new guessed letter. Then it returns the new word display.
 */
    private String changeWordDisplay(String word, String currentGuess, String wordGuessDisplay){
    	int lettersSubtracted=0;
    	while(word.contains(currentGuess)){
    		int index=word.indexOf(currentGuess)+lettersSubtracted; // Finds first index of current guess, accounting for subtracted letters.
	    	wordGuessDisplay=wordGuessDisplay.substring(0, index)+currentGuess+wordGuessDisplay.substring(index+1); //Changes word guess display.
	    	word=word.substring(0, index-lettersSubtracted)+word.substring(index-lettersSubtracted+1); // Subtracts the first index of letter guessed from the word.
    		lettersSubtracted++; // Adds 1 to letters subtracted each time the word contains a letter of the guess.
    	}
    	return wordGuessDisplay;
    }
/*
 * This method asks the user if he/she wants to play again and returns true or false.
 */
    private boolean wantToPlay(){
    	while(true){
	    	String answer=readLine("Want to Play Again? Yes or No: ");
	    	if(answer.equals("No")){
	    		return false;
	    	} else if(answer.equals("Yes")){
	    		return true;
	    	} else{
	    		println("That is not a valid answer.");
	    	}
    	}
    }
}
