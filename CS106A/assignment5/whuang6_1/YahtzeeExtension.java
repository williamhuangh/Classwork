/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.io.*;
import java.util.*;
import acm.graphics.*;
import acm.io.*;
import acm.program.*;
import acm.util.*;

public class YahtzeeExtension extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new YahtzeeExtension().start(args);
	}
	
	public void run() {
		// EXTENSION displays high scores first and tells the player to click to begin the game.
		displayHighScore();
		waitForClick();
		removeAll();
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}
/*
 * This method executes and shows the flow of the game.
 */
	private void playGame() {
		int [][] arrayOfScores=new int[nPlayers][N_CATEGORIES];
		boolean [][] categories=new boolean [nPlayers][N_CATEGORIES]; // To keep track of if a category for a player has been filled or not.
		int playerCounter=1;
		int categorySelected;
		int score;
		
		while(true){
			int currentPlayer=findCurrentPlayer(playerCounter);
			String player=playerNames[currentPlayer-1];
			display.printMessage(player+"'s turn! Click the \"Roll Dice\" button to roll the dice.");
			display.waitForPlayerToClickRoll(currentPlayer);
			int diceArray []= setUpDiceArray(N_DICE);
			display.displayDice(diceArray);
			twoReRolls(diceArray);
			addBonusPointsForYatzee(diceArray,arrayOfScores,currentPlayer);
			display.printMessage("Select a category for this roll");
			while(true){ // While loop to make sure the player selects a category that hasn't been selected before.
				categorySelected=display.waitForPlayerToSelectCategory();
				boolean selectedBefore=categories[currentPlayer-1][categorySelected-1];
				if(selectedBefore==true){
					display.printMessage("This category has been selected before. Select another one.");
				} else{
					categories[currentPlayer-1][categorySelected-1]=true;
					break;
				}
			}
			score=calculateScore(categorySelected,diceArray);
			arrayOfScores[currentPlayer-1][TOTAL-1]+=score; // Update the total score.
			arrayOfScores[currentPlayer-1][categorySelected-1]=score; // Update the score for the category selected.
			display.updateScorecard(categorySelected,currentPlayer,score);
			display.updateScorecard(TOTAL,currentPlayer,arrayOfScores[currentPlayer-1][TOTAL-1]);
			if(playerCounter/nPlayers==N_SCORING_CATEGORIES){ // Breaks while loop of all scoring categories have been selected.
				break;
			}
			playerCounter++; // Keeps track of how many total turns have been played by everybody.
		}
		updateUpperHalf(arrayOfScores);
		updateLowerHalf(arrayOfScores);
		int highestScore=findHighestScore(arrayOfScores);
		int highestPlayerNumber=findHighestPlayerNumber(arrayOfScores,highestScore);
		boolean newHighScore=checkHighScore(playerNames[highestPlayerNumber],highestScore); // EXTENSION checks to see if a new high score has been made.
		if(newHighScore==true){ // EXTENSION displays a new message if the player hits a high score.
			display.printMessage("Congradulations, "+playerNames[highestPlayerNumber]+", you're the winner with a total of "+highestScore+"! You have recorded a new high score!");
		} else{
			display.printMessage("Congradulations, "+playerNames[highestPlayerNumber]+", you're the winner with a total of "+highestScore+"!");
		}
	}
/*
 * EXTENSION This method displays the high score at the moment.
 */
	private void displayHighScore(){
		GLabel label=new GLabel("High Scores");
		label.setFont("Serif-30");
		label.move(APPLICATION_WIDTH/2-0.5*label.getWidth(), label.getAscent()+10);
		add(label);
		addScore(1,label.getY());
		addScore(2,label.getY());
		addScore(3,label.getY());
		GLabel clickToPlay=new GLabel("Click to Play");
		clickToPlay.setFont("Serif-30");
		clickToPlay.move(APPLICATION_WIDTH/2-0.5*clickToPlay.getWidth(), APPLICATION_HEIGHT-clickToPlay.getAscent()-10);
		add(clickToPlay);
	}
/*
 * EXTENSION This method draws the high scores onto the graphics window.
 */
	private void addScore(int number,double yCoordinate){
		String highScore=getHighScore(number);
		GLabel highScoreDisplay=new GLabel(number+". "+highScore);
		highScoreDisplay.setFont("Serif-30");
		highScoreDisplay.move(APPLICATION_WIDTH/2-0.5*highScoreDisplay.getWidth(), yCoordinate+number*highScoreDisplay.getHeight());
		add(highScoreDisplay);
	}
/*
 * EXTENSION This method gets the high score from the file.
 */
	private String getHighScore(int highScoreNumber){
		try{
			BufferedReader rd=new BufferedReader(new FileReader("highScores.txt"));
			for(int i=1;i<highScoreNumber;i++){
				rd.readLine();
			}
			String oldHighScoreString=rd.readLine();
			StringTokenizer tokenizer=new StringTokenizer(oldHighScoreString);
			String oldScoreString=tokenizer.nextToken();
			String highName=tokenizer.nextToken();
			int highScoreInt=Integer.parseInt(oldScoreString);
			String highScore=highName+" "+highScoreInt;
			return highScore;
		} catch(IOException ex){
			throw new ErrorException(ex);
		}
	}
/*
 * This method takes in the total number of turns that has been played and returns the current player number (1-4).
 */
	private int findCurrentPlayer(int playerCounter){
		int player=playerCounter%nPlayers;
		if(player!=0){
			return player;
		}
		else return nPlayers;
	}
/*
 * Sets up the dice array that keeps track of what numbers are displayed on the dice.
 */
	private int [] setUpDiceArray(int numberOfDice){
		int diceArray []=new int [numberOfDice];
		for(int i=0;i<numberOfDice;i++){
			diceArray[i]=rgen.nextInt(1, 6);
		}
		return diceArray;
	}
/*
 * Takes in the dice array and re-rolls it twice depending on which dice the player selects to reroll.
 */
	private void twoReRolls(int [] diceArray){
		for(int i=0;i<2;i++){
			display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
			display.waitForPlayerToSelectDice();
			diceArray= rerollTheDice(N_DICE,diceArray);
			display.displayDice(diceArray);
		}
	}
/*
 * Re-rolls the dice that the player selects to re-roll and generates new numbers for those dice.
 */
	private int[] rerollTheDice(int numberOfDice,int [] diceArray){
		for(int i=0;i<numberOfDice;i++){
			if(display.isDieSelected(i)==true){
				diceArray[i]=rgen.nextInt(1, 6);
			}
		}
		return diceArray;
	}
/*
 * Calculates the corresponding score depending on which category the player selects and what the player's
 * dice are.
 */
	private int calculateScore(int categorySelected,int [] diceArray){
		int score=0;
		switch(categorySelected){
			case ONES:
				for(int i=0;i<diceArray.length;i++){
					if(diceArray[i]==1){
						score++;
					}
				}
				break;
			case TWOS:
				for(int i=0;i<diceArray.length;i++){
					if(diceArray[i]==2){
						score+=2;
					}
				}
				break;
			case THREES:
				for(int i=0;i<diceArray.length;i++){
					if(diceArray[i]==3){
						score+=3;
					}
				}
				break;
			case FOURS:
				for(int i=0;i<diceArray.length;i++){
					if(diceArray[i]==4){
						score+=4;
					}
				}
				break;
			case FIVES:
				for(int i=0;i<diceArray.length;i++){
					if(diceArray[i]==5){
						score+=5;
					}
				}
				break;
			case SIXES:
				for(int i=0;i<diceArray.length;i++){
					if(diceArray[i]==6){
						score+=6;
					}
				}
				break;
			case THREE_OF_A_KIND:
				score=checkThreeOfAKind(diceArray);
				break;
			case FOUR_OF_A_KIND:
				score=checkFourOfAKind(diceArray);
				break;
			case FULL_HOUSE:
				if(checkFullHouse(diceArray)==true){
					score=25;
				}
				break;
			case SMALL_STRAIGHT:
				if(checkSmallStraight(diceArray)==true){
					score=30;
				}
				break;
			case LARGE_STRAIGHT:
				if(checkLargeStraight(diceArray)==true){
					score=40;
				}
				break;
			case YAHTZEE:
				if(checkYatzee(diceArray)==true){
					score=50;
				}
				break;
			case CHANCE:
				for(int i=0;i<diceArray.length;i++){
					score+=diceArray[i];
				}
				break;
			default:
				break;
		}
		return score;
	}
/*
 * This method returns 3* the number if there is at least 3 of a kind, otherwise, returns 0.
 */
	private int checkThreeOfAKind(int [] diceArray){
		int score=0;
		String highestRepetitionNumber= checkRepetition(diceArray);
		if(highestRepetitionNumber.length()>=3){
			score=highestRepetitionNumber.charAt(0)-'0';
		}
		return score*3;
	}
/*
 * This method returns 4* the number if there is at least 3 of a kind, otherwise, returns 0.
 */
	private int checkFourOfAKind(int [] diceArray){
		int score=0;
		String highestRepetitionNumber= checkRepetition(diceArray);
		if(highestRepetitionNumber.length()>=4){
			score=highestRepetitionNumber.charAt(0)-'0';
		}
		return score*4;
	}
/*
 * This method returns the longest repeated number in the array in a String.
 */
	private String checkRepetition(int [] diceArray){
		int [] orderedArray=orderTheArray(diceArray);
		String longest=""+orderedArray[0];
		String currentNumber=""+orderedArray[0];
		for(int i=1;i<orderedArray.length;i++){
			if(orderedArray[i]==orderedArray[i-1]){
				currentNumber+=orderedArray[i];
			} else{
				if(currentNumber.length()>longest.length()){
					longest=currentNumber;
				}
				currentNumber=""+orderedArray[i];
			}
			if(orderedArray[i]==orderedArray[i-1]&&orderedArray.length==(i+1)){
				currentNumber+=orderedArray[i];
				longest=currentNumber;
			}
		}
		return longest;
	}
/*
 * This method orders the array that is passed in and returns the ordered array.
 */
	private int [] orderTheArray(int [] diceArray){
		int [] orderedArray=diceArray;
		for(int i=0;i<orderedArray.length-1;i++){
			for(int j=1;j<orderedArray.length;j++){
				if(orderedArray[j]<orderedArray[j-1]){
					int temp=orderedArray[j];
					orderedArray[j]=orderedArray[j-1];
					orderedArray[j-1]=temp;
				}
			}
		}
		return orderedArray;
	}
/*
 * This method checks for if the array of dice numbers is a yatzee.
 */
	private boolean checkYatzee(int[]diceArray){
		for(int i=1;i<diceArray.length;i++){
			if(diceArray[i]!=diceArray[i-1]){
				return false;
			}
		}
		return true;
	}
/*
 * This method checks for if the array of dice numbers contains a full house.
 */
	private boolean checkFullHouse(int[]diceArray){
		int [] orderedArray=orderTheArray(diceArray);
		if(orderedArray[0]==orderedArray[1]){
			if(orderedArray[1]!=orderedArray[2]){
				if(orderedArray[2]==orderedArray[3]&&orderedArray[3]==orderedArray[4]){
					return true;
				}
			} else{
				if(orderedArray[2]!=orderedArray[3]&&orderedArray[3]==orderedArray[4]){
					return true;
				}
			}
		}
		return false;
	}
/*
 * This method checks for if the array of dice numbers contains a small straight.
 */
	private boolean checkSmallStraight(int[]diceArray){
		int [] orderedArray=orderTheArray(diceArray);
		String diceString="";
		for(int i=0;i<orderedArray.length;i++){
			diceString+=orderedArray[i];
		}
		diceString=eliminateRepeats(diceString);
		if(diceString.contains("1234")){
			return true;
		} else if (diceString.contains("2345")){
			return true;
		} else if (diceString.contains("3456")){
			return true;
		} else{
			return false;
		}
	}
/*
 * This method checks for if the array of dice numbers is a large straight.
 */
	private boolean checkLargeStraight(int[]diceArray){
		int [] orderedArray=orderTheArray(diceArray);
		String diceString="";
		for(int i=0;i<orderedArray.length;i++){
			diceString+=orderedArray[i];
		}
		if(diceString.equals("12345")){
			return true;
		} else if (diceString.equals("23456")){
			return true;
		} else{
			return false;
		}
	}
/*
 * This method takes in an ordered string and eliminates the repetitions in the String. 
 */
	private String eliminateRepeats(String diceString){
		String returnString=""+diceString.charAt(0);
		for(int i=1;i<diceString.length();i++){
			if(diceString.charAt(i)!=diceString.charAt(i-1)){
				returnString+=diceString.charAt(i);
			}
		}
		return returnString;
	}
/*
 * EXTENSION This method gives you bonus points for rolling extra Yahtzees. It adds 100 to both the total and the Yahtzee categories
 * and displays them, but you can still choose a category to use that same set of dice on.
 */
	private void addBonusPointsForYatzee(int [] diceArray,int [][] arrayOfScores,int currentPlayer){
		if(checkYatzee(diceArray)==true && arrayOfScores[currentPlayer-1][YAHTZEE-1]!=0){
			arrayOfScores[currentPlayer-1][YAHTZEE-1]+=100;
			arrayOfScores[currentPlayer-1][TOTAL-1]+=100; 
			display.updateScorecard(YAHTZEE,currentPlayer,arrayOfScores[currentPlayer-1][YAHTZEE-1]);
			display.updateScorecard(TOTAL,currentPlayer,arrayOfScores[currentPlayer-1][TOTAL-1]);
		}
	}
/*
 * This method updates the UPPER_SCORE and UPPER_BONUS categories on the gameboard and in the array of scores.
 */
	private void updateUpperHalf(int [][] arrayOfScores){
		for(int i=0;i<nPlayers;i++){
			int upperTotal=0;
			for(int j=0;j<6;j++){
				upperTotal+=arrayOfScores[i][j];
			}
			arrayOfScores[i][UPPER_SCORE-1]=upperTotal;
			if (upperTotal>=63){
				arrayOfScores[i][UPPER_BONUS-1]=35;
			} else{
				arrayOfScores[i][UPPER_BONUS-1]=0;
			}
			display.updateScorecard(UPPER_SCORE,i+1,arrayOfScores[i][UPPER_SCORE-1]);
			display.updateScorecard(UPPER_BONUS,i+1,arrayOfScores[i][UPPER_BONUS-1]);
		}
	}
/*
 * This method updates the LOWER_SCORE and TOTAL categories of the gameboard and the array of scores.
 */
	private void updateLowerHalf(int [][] arrayOfScores){
		for(int i=0;i<nPlayers;i++){
			int lowerTotal=0;
			for(int j=8;j<N_CATEGORIES-2;j++){
				lowerTotal+=arrayOfScores[i][j];
			}
			arrayOfScores[i][LOWER_SCORE-1]=lowerTotal;
			arrayOfScores[i][TOTAL-1]+=arrayOfScores[i][UPPER_BONUS-1];
			display.updateScorecard(LOWER_SCORE,i+1,arrayOfScores[i][LOWER_SCORE-1]);
			display.updateScorecard(TOTAL,i+1,arrayOfScores[i][TOTAL-1]);
		}
	}
/*
 * This method returns the highest score out of the player's scores.
 */
	private int findHighestScore(int[][]arrayOfScores){
		int highestScore=0;
		for(int i=0;i<nPlayers;i++){
			boolean isHigher=highestScore<arrayOfScores[i][TOTAL-1];
			if(isHigher==true){
				highestScore=arrayOfScores[i][TOTAL-1];
			}
		}
		return highestScore;
	}
/*
 * This method returns the number of the player who got the highest score.
 */
	private int findHighestPlayerNumber(int [][] arrayOfScores, int highestScore){
		int playerNumber=0;
		for(int i=0;i<nPlayers;i++){
			if(highestScore==arrayOfScores[i][TOTAL-1]){
				playerNumber=i;
			}
		}
		return playerNumber;
	}
/*
 * EXTENSION This method checks the score and updates the file with the high score and updates it if the player makes a new high
 * score. It assumes that highScores.txt is already present and is in the form of score followed by a space and the 
 * player's name who got the score in all 3 rows. Only the winner of a game can make a new high score.
 */
	private boolean checkHighScore(String highestPlayerName,int highestScore){
		try{
			BufferedReader rd=new BufferedReader(new FileReader("highScores.txt"));
			
			String oldHighScoreString1=rd.readLine();
			StringTokenizer tokenizer1=new StringTokenizer(oldHighScoreString1);
			String oldScoreString1=tokenizer1.nextToken();
			int oldHighScore1= Integer.parseInt(oldScoreString1);
			
			String oldHighScoreString2=rd.readLine();
			StringTokenizer tokenizer2=new StringTokenizer(oldHighScoreString2);
			String oldScoreString2=tokenizer2.nextToken();
			int oldHighScore2= Integer.parseInt(oldScoreString2);
			
			String oldHighScoreString3=rd.readLine();
			StringTokenizer tokenizer3=new StringTokenizer(oldHighScoreString3);
			String oldScoreString3=tokenizer3.nextToken();
			int oldHighScore3= Integer.parseInt(oldScoreString3);
			
			if(highestScore>oldHighScore1){
				try{
					PrintWriter wr=new PrintWriter(new FileWriter("highScores.txt"));
					wr.println(highestScore+" "+highestPlayerName);
					wr.println(oldHighScoreString1);
					wr.println(oldHighScoreString2);
					wr.close();
					return true;
				} catch(IOException ex){
					throw new ErrorException(ex);
				}
			} else if(highestScore>oldHighScore2){
				try{
					PrintWriter wr=new PrintWriter(new FileWriter("highScores.txt"));
					wr.println(oldHighScoreString1);
					wr.println(highestScore+" "+highestPlayerName);
					wr.println(oldHighScoreString2);
					wr.close();
					return true;
				} catch(IOException ex){
					throw new ErrorException(ex);
				}
			} else if(highestScore>oldHighScore3){
				try{
					PrintWriter wr=new PrintWriter(new FileWriter("highScores.txt"));
					wr.println(oldHighScoreString1);
					wr.println(oldHighScoreString2);
					wr.println(highestScore+" "+highestPlayerName);
					wr.close();
					return true;
				} catch(IOException ex){
					throw new ErrorException(ex);
				}
			}
			rd.close();
			return false;
		} catch(IOException ex){
			throw new ErrorException(ex);
		}
	}
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
