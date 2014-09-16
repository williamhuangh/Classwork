/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraphExtension extends GCanvas
	implements NameSurferConstants, ComponentListener {
	
/*
 * NameCounter keeps track of how many names are on the screen not counting the first one.
 * graphPoints maps the name to an array of its ranks.
 * names is an ArrayList of names to keep the order the names were added.
 */
	private int nameCounter=0;
	private Map<String, int []> graphPoints=new HashMap<String, int []>();
	private ArrayList<String> names=new ArrayList<String>();
	/**
	* Creates a new NameSurferGraph object that displays the data.
	*/
	public NameSurferGraphExtension() {
		update();
		addComponentListener(this);
	}
	
	
	/**
	* Clears the list of name surfer entries stored inside this class.
	*/
	public void clear() {
		graphPoints.clear();
		names.clear();
	}
	
	
	/* Method: addEntry(entry) */
	/**
	* Adds a new NameSurferEntry to the list of entries on the display.
	* Note that this method does not actually draw the graph, but
	* simply stores the entry; the graph is drawn by calling update.
	* If the NameSurferEntry has been added already, it does not re-store
	* the entry.
	*/
	public void addEntry(NameSurferEntry entry) {
		String name=entry.getName();
		if (!graphPoints.containsKey(name)) {
			int [] hashMapArray=fillArray(entry);
			graphPoints.put(name, hashMapArray);
			names.add(name);
		}
	}
/*
 * EXTENSION This method deletes the entry from both the Hashmap and the arraylist.
 */
	public void deleteEntry(NameSurferEntry entry){
		String name=entry.getName();
		if (graphPoints.containsKey(name)) {
			graphPoints.remove(name);
			names.remove(name);
		}
	}
/*
 * This method returns an array filled with the ranks of the corresponding name surfer entry.
 */
	private int [] fillArray(NameSurferEntry entry){
		int [] filledArray=new int [NDECADES];
		for(int i=0;i<NDECADES;i++){
			filledArray[i]=entry.getRank(i);
		}
		return filledArray;
	}
	
	/**
	* Updates the display image by deleting all the graphical objects
	* from the canvas and then reassembling the display according to
	* the list of entries. Your application must call update after
	* calling either clear or addEntry; update is also called whenever
	* the size of the canvas changes.
	*/
	public void update() {
		removeAll();
		addVerticalLines();
		addHorizontalLines();
		addDecades();
		addPointsAndName();
	}
/*
 * This method adds the vertical lines of the graph onto the display.
 */
	private void addVerticalLines(){
		for(int i=0;i<NDECADES;i++){
			GLine verticalLine=drawVerticalLine(i);
			add(verticalLine);
		}
	}
/*
 * This method takes in the line number and returns one vertical line to draw onto the canvas.
 */
	private GLine drawVerticalLine(int lineNumber){
		GLine verticalLine= new GLine(lineNumber*(getWidth()/NDECADES),0,lineNumber*(getWidth()/NDECADES),getHeight());
		return(verticalLine);
	}
/*
 * This method adds the horizontal lines of the graph onto the display.
 */
	private void addHorizontalLines(){
		GLine topLine=new GLine(0,GRAPH_MARGIN_SIZE,getWidth(),GRAPH_MARGIN_SIZE);
		GLine bottomLine=new GLine(0,getHeight()-GRAPH_MARGIN_SIZE,getWidth(),getHeight()-GRAPH_MARGIN_SIZE);
		add(topLine);
		add(bottomLine);
	}
/*
 * This method adds the decade numbers of the graph onto the display.
 */
	private void addDecades(){
		for(int i=0;i<NDECADES;i++){
			GLabel decadeLabel=drawDecadeLabel(i);
			int size=getHeight()*getWidth()/20002;
			decadeLabel.setFont("Serif-"+size);
			add(decadeLabel);
		}
	}
/*
 * This method returns a decade label that corresponds to its nth decade after 1900.
 */
	private GLabel drawDecadeLabel(int nthDecade){
		int decadeNumber=START_DECADE+nthDecade*10;
		GLabel decadeLabel=new GLabel(""+decadeNumber);
		decadeLabel.move(getWidth()/NDECADES*nthDecade,getHeight());
		return decadeLabel;
	}
/*
 * This method adds on the points and the names of the entries onto the canvas.
 */
	private void addPointsAndName(){
		int height=getHeight()-2*GRAPH_MARGIN_SIZE;
		for(String name:names){
			int lowestRank=findlowestRank(name);
			for(int i=0;i<NDECADES-1;i++){
				drawLineAndLabel(height,i,name,lowestRank);
				drawPoints(height,i,name);
			}
			drawLastLabel(height,name,lowestRank);
			drawPoints(height,NDECADES-1,name);
			nameCounter++;
		}
		nameCounter=0;
	}
/*
 * EXTENSION finds the highest rank for a given name.
 */
	private int findlowestRank(String name){
		int lowestRank=1000;
		int currentRank;
		for(int i=0;i<NDECADES;i++){
			currentRank=graphPoints.get(name)[i];
			if(currentRank<lowestRank&&currentRank!=0){
				lowestRank=currentRank;
			}
		}
		return lowestRank;
	}
/*
 * This method draws a line (from current decade to the next decade) and a name label onto the graph
 * (at the current decade).
 * 
 * EXTENSION bolds the label if it is the most common rank for that name. If there are tied years for the
 * most common rank, both are bolded.
 */
	private void drawLineAndLabel(int height,int nthDecade,String name,int lowestRank){
		int currentRank=graphPoints.get(name)[nthDecade];
		int nextRank=graphPoints.get(name)[nthDecade+1];
		int drawnRank=currentRank;
		if(currentRank==0){
			name+="*";
			currentRank=1000;
		}
		if(nextRank==0){
			nextRank=1000;
		}
		GLine line=new GLine(getWidth()/NDECADES*nthDecade,GRAPH_MARGIN_SIZE+height/1000.0*currentRank,getWidth()/NDECADES*(nthDecade+1),GRAPH_MARGIN_SIZE+height/1000.0*nextRank);
		setTheColor(line);
		add(line);
		GLabel nameLabel=new GLabel (name+" "+drawnRank,line.getStartPoint().getX(),line.getStartPoint().getY());
		setTheColor(nameLabel);
		int size=getHeight()*getWidth()/20002;
		nameLabel.setFont("Serif-"+size);
		if(currentRank==lowestRank){
			nameLabel.setFont(new Font("Serif",Font.BOLD,size+2));
		}
		add(nameLabel);
	}
/*
 * EXTENSION This method draws points at each decade rank to better label the graph.
 * Switches between circles, squares, triangles, and Xs.
 */
	private void drawPoints(int height,int nthDecade,String name){
		int rank=graphPoints.get(name)[nthDecade];
		if (rank==0){
			rank=1000;
		}
		int remainder=nameCounter%4;
		switch(remainder){
			case 0:
				addCircles(height,nthDecade,rank);
				break;
			case 1:
				addSquares(height,nthDecade,rank);
				break;
			case 2:
				addTriangles(height,nthDecade,rank);
				break;
			case 3:
				addXs(height,nthDecade,rank);
				break;
			default:
				break;
		}
	}
/*
 * EXTENSION This method adds a circle to where the corresponding rank and decade are.
 */
	private void addCircles(int height,int nthDecade,int rank){
		GOval circle=new GOval(7,7);
		circle.setLocation(getWidth()/NDECADES*nthDecade-0.5*circle.getHeight(),GRAPH_MARGIN_SIZE+height/1000.0*rank-0.5*circle.getHeight());
		setTheColor(circle);
		circle.setFilled(true);
		add(circle);
	}
/*
 * EXTENSION This method adds a square to where the corresponding rank and decade are.
 */
	private void addSquares(int height,int nthDecade,int rank){
		GRect rect=new GRect(7,7);
		rect.setLocation(getWidth()/NDECADES*nthDecade-0.5*rect.getHeight(),GRAPH_MARGIN_SIZE+height/1000.0*rank-0.5*rect.getHeight());
		setTheColor(rect);
		rect.setFilled(true);
		add(rect);
	}
/*
 * EXTENSION This method adds a triangle to where the corresponding rank and decade are.
 */
	private void addTriangles(int height,int nthDecade,int rank){
		GPolygon triangle=new GPolygon();
		triangle.addVertex(0,-5);
		triangle.addVertex(4, 3);
		triangle.addVertex(-4, 3);
		triangle.setLocation(getWidth()/NDECADES*nthDecade,GRAPH_MARGIN_SIZE+height/1000.0*rank);
		setTheColor(triangle);
		triangle.setFilled(true);
		add(triangle);
	}
/*
 * EXTENSION This method adds an X to where the corresponding rank and decade are.
 */
	private void addXs(int height,int nthDecade,int rank){
		GLabel xS=new GLabel("X");
		xS.setFont("Comic Sans-13");
		xS.setLocation(getWidth()/NDECADES*nthDecade-0.5*xS.getWidth(),GRAPH_MARGIN_SIZE+height/1000.0*rank+0.5*xS.getAscent());
		setTheColor(xS);
		add(xS);
	}
/*
 * This method draws the last label of the name that isn't accounted for by drawLineAndLabel.
 * 
 * EXTENSION bolds the label if it is the most common rank for that name. If there are tied years
 * for the most common rank, both are bolded.
 */
	private void drawLastLabel(int height, String name,int lowestRank){
		int rank=graphPoints.get(name)[NDECADES-1];
		int drawnRank=rank;
		if(rank==0){
			name+="*";
			rank=1000;
		}
		GLabel label=new GLabel(name+" "+drawnRank,getWidth()/NDECADES*(NDECADES-1),GRAPH_MARGIN_SIZE+height/1000.0*rank);
		setTheColor(label);
		int size=getHeight()*getWidth()/20002;
		label.setFont("Serif-"+size);
		if(rank==lowestRank){
			label.setFont(new Font("Serif",Font.BOLD,size+2));
		}
		add(label);
	}
/*
 * This method sets the color of the GObject that is passed in.
 */
	private void setTheColor(GObject obj){
		if (nameCounter%4==0){
			obj.setColor(Color.BLACK);
		} else if (nameCounter%4==1){
			obj.setColor(Color.RED);
		} else if (nameCounter%4==2){
			obj.setColor(Color.BLUE);
		} else if (nameCounter%4==3){
			obj.setColor(Color.MAGENTA);
		}
	}

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
