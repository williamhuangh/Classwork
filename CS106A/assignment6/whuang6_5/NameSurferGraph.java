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

public class NameSurferGraph extends GCanvas
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
	public NameSurferGraph() {
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
			for(int i=0;i<NDECADES-1;i++){
				drawLineAndLabel(height,i,name);
			}
			drawLastLabel(height,name);
			nameCounter++;
		}
		nameCounter=0;
	}
/*
 * This method draws a line (from current decade to the next decade) and a name label onto the graph
 * (at the current decade).
 */
	private void drawLineAndLabel(int height,int nthDecade,String name){
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
		add(nameLabel);
	}
/*
 * This method draws the last label of the name that isn't accounted for by drawLineAndLabel.
 */
	private void drawLastLabel(int height, String name){
		int rank=graphPoints.get(name)[NDECADES-1];
		int drawnRank=rank;
		if(rank==0){
			name+="*";
			rank=1000;
		}
		GLabel label=new GLabel(name+" "+drawnRank,getWidth()/NDECADES*(NDECADES-1),GRAPH_MARGIN_SIZE+height/1000.0*rank);
		setTheColor(label);
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
