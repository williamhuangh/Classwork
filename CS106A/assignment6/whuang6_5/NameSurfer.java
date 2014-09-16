/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {
	
	private NameSurferDataBase database;
	private JTextField text;
	private NameSurferGraph graph;
/* Method: init() */
/**
 * This method has the responsibility for reading in the data base
 * and initializing the interactors at the top of the window as well
 * as the graph.
 */
	public void init() {
		assembleGui();
		database=new NameSurferDataBase(NAMES_DATA_FILE);
		graph=new NameSurferGraph();
		add(graph);
	}
/*
 * This method draws the GUIs at the top of the window.
 */
	private void assembleGui(){
		add(new JLabel("Name "),NORTH);
		text=new JTextField(20);
		text.setActionCommand("Graph");
		text.addActionListener(this);
		add(text, NORTH);
		add(new JButton("Graph"),NORTH);
		add(new JButton("Clear"),NORTH);
		addActionListeners();
	}

/**
 * This method detects when the buttons are clicked or if enter is pressed.
 * If enter is pressed or the Graph button is clicked, the corresponding points and lines
 * are drawn for the text in the textfield. If clear is clicked, it clears the graph.
 */
	public void actionPerformed(ActionEvent e) {
		String cmd=e.getActionCommand();
		if (cmd.equals("Graph")){
			NameSurferEntry entry=database.findEntry(text.getText());
			if(entry!=null){
				graph.addEntry(entry);
				graph.update();
			}
		} else if(cmd.equals("Clear")){
			graph.clear();
			graph.update();
		}
	}
}
