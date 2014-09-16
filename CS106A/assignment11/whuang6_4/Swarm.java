/*
 * File: Swarm.java
 * ---------------------
 * Plays the Swarm Game.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import acm.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Swarm extends GraphicsProgram implements SwarmConstants{
	
	private GRect mouseRect;
	private ArrayList<SwarmEnemies> enemies=new ArrayList<SwarmEnemies>();
	private int counter;
	private String playerName;

/*
 * Initializes the game. Also restarts it unless the player closes the window.
 */
	public void init(){
		while(true){
			IODialog dialog = getDialog();
			playerName = dialog.readLine("Enter Player Name: ");
			displayHighScores();
			waitForClick();
			removeAll();
			startScreen();
			waitForClick();
			addMouseListeners();
			playGame();
			endGame();
			waitForClick();
			reset();
		}
	}
/*
 * This method gets the high scores and adds the high score display screen.
 */
	private void displayHighScores(){
		removeAll();
		setBackground(Color.WHITE);
		GLabel highScoreLabel=new GLabel("High Scores");
		highScoreLabel.setFont("Serif-20");
		highScoreLabel.move(APPLICATION_WIDTH/2-0.5*highScoreLabel.getWidth(), highScoreLabel.getAscent()+10);
		add(highScoreLabel);
		addScore(1,highScoreLabel.getY());
		addScore(2,highScoreLabel.getY());
		addScore(3,highScoreLabel.getY());
		GLabel clickToBegin=new GLabel("Click to Begin");
		clickToBegin.setFont("Serif-15");
		clickToBegin.move(APPLICATION_WIDTH/2-0.5*clickToBegin.getWidth(), APPLICATION_HEIGHT-clickToBegin.getAscent()-10);
		add(clickToBegin);
	}
/*
 * This method adds the individual high scores onto the screen.
 */
	private void addScore(int rank,double yCoordinate){
		String highScore=getHighScore(rank);
		GLabel highScoreDisplay=new GLabel(rank+". "+highScore);
		highScoreDisplay.setFont("Serif-30");
		highScoreDisplay.move(APPLICATION_WIDTH/2-0.5*highScoreDisplay.getWidth(), yCoordinate+rank*highScoreDisplay.getHeight());
		add(highScoreDisplay);
	}
/*
 * This method reads and returns a high score from the file. It assumes that highScores.txt is already
 * present and is in the form of score followed by a space and the player's name who got the score 
 * in all 3 rows.
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
 * This method sets up the opening start screen of the game.
 */
	private void startScreen(){
		setBackground(Color.RED);
		addSwarmLabel();
		addClickLabel();
	}
/*
 * This adds the main SWARM label to the screen.
 */
	private void addSwarmLabel(){
		GLabel title=new GLabel("SWARM");
		title.setFont("Impact-80");
		title.setColor(Color.YELLOW);
		title.move(APPLICATION_WIDTH/2-0.5*title.getWidth(), APPLICATION_HEIGHT/3);
		add(title);
	}
/*
 * This adds the "Click To Play" label to the screen.
 */
	private void addClickLabel(){
		GLabel label=new GLabel("Click To Play");
		label.setFont("Impact-30");
		label.setColor(Color.YELLOW);
		label.move(APPLICATION_WIDTH/2-0.5*label.getWidth(), APPLICATION_HEIGHT/3*2);
		add(label);
	}
/*
 * This method begins and plays the SWARM game.
 */
	private void playGame(){
		removeAll();
		setBackground(Color.CYAN);
		createMouseRect();
		createEnemies();
	}
/*
 * This method creates the square that will follow the mouse.
 */
	private void createMouseRect(){
		mouseRect=new GRect(MOUSERECT_LENGTH,MOUSERECT_LENGTH);
		mouseRect.setColor(Color.BLACK);
		mouseRect.setFilled(true);
		add(mouseRect,0,0);
	}
/*
 * This method creates and moves the enemies in the game.
 */
	private void createEnemies(){
		addAnEnemy();
		mouseRect.sendToFront();
		boolean lose=false;
		boolean breakLoop=false;
		GLabel timeCounter=addLabel(counter);
		
		while(breakLoop==false){
			if(counter%100==99){ // Adds another enemy after a certain time has passed in the game.
				addAnEnemy();
				mouseRect.sendToFront();
				makeEnemiesFaster();
			}
			for(int i=0;i<enemies.size();i++){ // Iterates through the arraylist of enemies.
				enemies.get(i).moveEnemy();
				lose=checkForCollisions(enemies.get(i));
				if(lose==true){
					breakLoop=true;
				}
				pause(10);
			}
			counter++;
			remove(timeCounter);
			timeCounter=addLabel(counter);
		}
	}
/*
 * This method adds and returns the points counter at the left hand bottom of the screen.
 */
	private GLabel addLabel(int counter){
		GLabel timeLabel=new GLabel("Points: "+counter,0,APPLICATION_HEIGHT);
		add(timeLabel);
		return(timeLabel);
	}
/*
 * This method adds a new swarm enemy.
 */
	private void addAnEnemy(){
		SwarmEnemies nextEnemy=new SwarmEnemies();
		add(nextEnemy,100,100);
		enemies.add(nextEnemy);
	}
/*
 * This method iterates through the arraylist and makes the enemies faster both to make the game
 * more difficult to survive and to account for the lag that is added to the game with each additional
 * enemy.
 */
	private void makeEnemiesFaster(){
		for(int i=0;i<enemies.size();i++){
			enemies.get(i).cutX();
			enemies.get(i).cutX();
		}
	}
/*
 * This method checks to see if the enemy has collided with something. It will bounce at the wall
 * and return false if it hits the wall but will return true if it hits the player's square.
 */
	private boolean checkForCollisions(SwarmEnemies enemy){
		if(enemy.getX()<=0||enemy.getX()+MOUSERECT_LENGTH>=APPLICATION_WIDTH){
			enemy.reverseX();
		}
		if(enemy.getY()<=0||enemy.getY()+MOUSERECT_LENGTH>=APPLICATION_HEIGHT){
			enemy.reverseY();
		}
		if(getElementAt(enemy.getX(),enemy.getY())==mouseRect || getElementAt(enemy.getX()+MOUSERECT_LENGTH,enemy.getY())==mouseRect
				|| getElementAt(enemy.getX(),enemy.getY()+MOUSERECT_LENGTH)==mouseRect
				|| getElementAt(enemy.getX()+MOUSERECT_LENGTH,enemy.getY()+MOUSERECT_LENGTH)==mouseRect){
			return true;
		}
		return false;
	}
/*
 * This method writes the text to the window at the end of the game. Only adds newHighScore if a new score
 * is recorded.
 */
	private void endGame(){
		GLabel clickToPlayAgain=new GLabel("Click to Play Again!",0, APPLICATION_HEIGHT-40);
		add(clickToPlayAgain);
		boolean newScore=recordHighScores();
		if(newScore==true){
			GLabel newHighScore=new GLabel("New High Score Recorded!",0,clickToPlayAgain.getY()-clickToPlayAgain.getAscent());
			add(newHighScore);
		}
	}
/*
 * This method records the new high scores to the high score file and overrides it if there is a new high score.
 * Otherwise, reads and closes the file.
 */
	private boolean recordHighScores(){
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
			
			if(counter>oldHighScore1){
				try{
					PrintWriter wr=new PrintWriter(new FileWriter("highScores.txt"));
					wr.println(counter+" "+playerName);
					wr.println(oldHighScoreString1);
					wr.println(oldHighScoreString2);
					wr.close();
				} catch(IOException ex){
					throw new ErrorException(ex);
				}
				return true;
			} else if(counter>oldHighScore2){
				try{
					PrintWriter wr=new PrintWriter(new FileWriter("highScores.txt"));
					wr.println(oldHighScoreString1);
					wr.println(counter+" "+playerName);
					wr.println(oldHighScoreString2);
					wr.close();
				} catch(IOException ex){
					throw new ErrorException(ex);
				}
				return true;
			} else if(counter>oldHighScore3){
				try{
					PrintWriter wr=new PrintWriter(new FileWriter("highScores.txt"));
					wr.println(oldHighScoreString1);
					wr.println(oldHighScoreString2);
					wr.println(counter+" "+playerName);
					wr.close();
				} catch(IOException ex){
					throw new ErrorException(ex);
				}
				return true;
			}
			rd.close();
			return false;
		} catch(IOException ex){
			throw new ErrorException(ex);
		}
	}
/*
 * This method resets the counter, enemy arraylist, and mouseRect for a new game.
 */
	private void reset(){
		counter=0;
		enemies.clear();
		mouseRect=null;
	}
/*
 * This method detects mouse movements and sets the location of the mouseRect depending on where the mouse
 * has moved.
 */
	public void mouseMoved(MouseEvent e){
		if(mouseRect!=null){
			if(e.getX()>=MOUSERECT_LENGTH/2 && e.getX()<=getWidth()-MOUSERECT_LENGTH/2
					&& e.getY()>=MOUSERECT_LENGTH/2 && e.getY()<=getHeight()-MOUSERECT_LENGTH/2){
				mouseRect.setLocation(e.getX()-0.5*MOUSERECT_LENGTH, e.getY()-0.5*MOUSERECT_LENGTH);
			}
		}
	}
}
