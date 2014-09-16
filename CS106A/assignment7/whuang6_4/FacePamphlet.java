/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */
 
import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;
 
public class FacePamphlet extends Program 
                    implements FacePamphletConstants {

    private JTextField topText;
    private JTextField status;
    private JTextField pic;
    private JTextField friend;
    private FacePamphletDatabase database;
    private FacePamphletProfile currentProfile;
    private FacePamphletCanvas canvas;
    /**
     * This method has the responsibility for initializing the 
     * interactors in the application, and taking care of any other 
     * initialization that needs to be performed.
     */
    public void init() {
        initGui();
        database=new FacePamphletDatabase();
        canvas=new FacePamphletCanvas();
        add(canvas);
    }
/* 
 * This method draws up the GUI interactors on the top and left sides of the window.
 */
    private void initGui(){
        add(new JLabel("Name "),NORTH);
        topText=new JTextField(TEXT_FIELD_SIZE);
        add(topText, NORTH);
        add(new JButton("Add"),NORTH);
        add(new JButton("Delete"),NORTH);
        add(new JButton("Lookup"),NORTH);
         
        status=new JTextField(TEXT_FIELD_SIZE);
        status.setActionCommand("Change Status");
        add(status, WEST);
        status.addActionListener(this);
        add(new JButton("Change Status"),WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        pic=new JTextField(TEXT_FIELD_SIZE);
        pic.setActionCommand("Change Picture");
        add(pic, WEST);
        pic.addActionListener(this);
        add(new JButton("Change Picture"),WEST);
        add(new JLabel(EMPTY_LABEL_TEXT), WEST);
        friend=new JTextField(TEXT_FIELD_SIZE);
        friend.setActionCommand("Add Friend");
        add(friend, WEST);
        friend.addActionListener(this);
        add(new JButton("Add Friend"),WEST);
        addActionListeners();
    }
   
    /**
     * This class is responsible for detecting when the buttons are
     * clicked or interactors are used, so you will have to add code
     * to respond to these actions.
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Add")){
            addNewProfile();
        } else if(e.getActionCommand().equals("Delete")){
            deleteProfile();
        } else if(e.getActionCommand().equals("Lookup")){
            lookupProfile();
        } else if(e.getActionCommand().equals("Change Status")){
            changeStatus();
        } else if(e.getActionCommand().equals("Change Picture")){
            changeImage();
        } else if(e.getActionCommand().equals("Add Friend")){
            addFriend();
        } 
    }
/*
 * This method adds a new profile and displays it if the text in the textbox does not correspond with an already
 * existing one. If the textbox is empty, does nothing. If the textbox corresponds with an already existing profile,
 * displays the existing one.
 */
    private void addNewProfile(){
        if(database.containsProfile(topText.getText())){
            canvas.displayProfile(database.getProfile(topText.getText()));
            canvas.showMessage("Profile of the name "+topText.getText()+" already exists.");
            currentProfile=database.getProfile(topText.getText());
        } else if(!topText.getText().equals("")){
            database.addProfile(new FacePamphletProfile(topText.getText()));
            canvas.displayProfile(database.getProfile(topText.getText()));
            canvas.showMessage("New profile created");
            currentProfile=database.getProfile(topText.getText());
        }
    }
/*
 * This method deletes a profile from the database if the text in the textbox corresponds with an existing one.
 * If the text does not correspond with an existing profile, displays a message that informs that to the user.
 * Does nothing if the textbox is empty.
 */
    private void deleteProfile(){
        if(database.containsProfile(topText.getText())){
            canvas.removeAll();
            canvas.showMessage("Profile of "+topText.getText()+" deleted");
            database.deleteProfile(topText.getText());
            currentProfile=null;
        } else if(!topText.getText().equals("")){
            canvas.showMessage("A profile with the name "+topText.getText()+" does not exist");
            currentProfile=null;
        }
    }
/*
 * This method displays the profile that corresponds to the text in the textbox if it exists. If it 
 * does not exist, it will inform that to the user. Does nothing if textbox is empty.
 */
    private void lookupProfile(){
        if(database.containsProfile(topText.getText())){
            canvas.displayProfile(database.getProfile(topText.getText()));
            canvas.showMessage("Displaying "+topText.getText());
            currentProfile=database.getProfile(topText.getText());
        } else if(!topText.getText().equals("")){
            canvas.removeAll();
            canvas.showMessage("A profile with the name "+topText.getText()+" does not exist");
            currentProfile=null;
        }
    }
/*
 * This method changes the status of the current profile, but only if the current profile is not null.
 */
    private void changeStatus(){
        if(currentProfile!=null){
            currentProfile.setStatus(status.getText());
            canvas.displayProfile(currentProfile);
            if(status.getText().equals("")){
            	canvas.showMessage("Status updated");
            } else{
            	canvas.showMessage("Status updated to " +status.getText());
            }
        } else{
            canvas.showMessage("Please select a profile to change status");
        }
    }
/*
 * This method changes the image of the current profile, but only if the current profile is not null.
 * Throws an exception if the text in the textbox does not correspond with an image that can be opened.
 */
    private void changeImage(){
        if(currentProfile!=null){
            GImage image = null; 
            try { 
                if(containsLetters(pic.getText())==true){
                    image = new GImage(pic.getText());
                    currentProfile.setImage(image);
                    canvas.displayProfile(currentProfile);
                    canvas.showMessage("Picture updated");
                } else{
                	canvas.showMessage("Unable to open image file: "+pic.getText());
                }
            } catch (ErrorException ex) {
                canvas.showMessage("Unable to open image file: "+pic.getText());
            }
        } else{
            canvas.showMessage("Please select a profile to change picture");
        }
    }
/*
 * This method adds a friend to the current profile, but only if the current profile is not null and the
 * text in the textbox corresponds with a profile in the database. If the text in the textbox is either
 * the current profile name itself or a non-existant profile, will inform the user.
 */
    private void addFriend(){
        if(currentProfile!=null){
            if(database.getProfile(friend.getText())==currentProfile){
                canvas.showMessage(friend.getText()+" cannot friend himself/herself");
            } else if(database.getProfile(friend.getText())!=null&&database.getProfile(friend.getText()).hasFriend(friend.getText())==false){
                currentProfile.addFriend(friend.getText());
                database.getProfile(friend.getText()).addFriend(currentProfile.getName());
                canvas.displayProfile(currentProfile);
                canvas.showMessage(friend.getText()+" added as friend");
            } else if(database.getProfile(friend.getText())!=null){
                canvas.showMessage(currentProfile.getName()+" already has "+friend.getText()+" as a friend");
            } else if(!friend.getText().equals("")){
                canvas.showMessage(friend.getText()+" does not exist");
            }
        } else{
            canvas.showMessage("Please select a profile to add friend");
        }
    }
/*
 * This method returns true if the string passed in has letters.
 */
    private boolean containsLetters(String str){
    	for(int i=0;i<26;i++){
    		if(str.contains(""+(char)('a'+i))){
    			return true;
    		} else if(str.contains(""+(char)('A'+i))){
    			return true;
    		}
    	}
    	return false;
    }
}