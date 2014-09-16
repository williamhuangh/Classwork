/*
 * File: FacePamphletCanvasExtension.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */
 
 
import acm.graphics.*;
import java.awt.*;
import java.util.*;
 
public class FacePamphletCanvasExtension extends GCanvas 
                    implements FacePamphletConstants {
    private GLabel message;
    /** 
     * Constructor
     * This method takes care of any initialization needed for 
     * the display
     */
    public FacePamphletCanvasExtension() {
    	// Empty
    }
 
     
    /** 
     * This method displays a message string near the bottom of the 
     * canvas.  Every time this method is called, the previously 
     * displayed message (if any) is replaced by the new message text 
     * passed in.
     */
    public void showMessage(String msg) {
        if(message!=null){
            remove(message);
        }
        message=new GLabel(msg);
        message.setFont(MESSAGE_FONT);
        message.setLocation(getWidth()/2-message.getWidth()/2,getHeight()-BOTTOM_MESSAGE_MARGIN);
        add(message);
    }
     
     
    /** 
     * This method displays the given profile on the canvas.  The 
     * canvas is first cleared of all existing items (including 
     * messages displayed near the bottom of the screen) and then the 
     * given profile is displayed.  The profile display includes the 
     * name of the user from the profile, the corresponding image 
     * (or an indication that an image does not exist), the status of
     * the user, and a list of the user's friends in the social network.
     */
    public void displayProfile(FacePamphletProfileExtension profile) {
        removeAll();
        double nameBaseline=displayName(profile);
        double imageBaseline=displayImage(profile,nameBaseline);
        displayStatus(profile,imageBaseline);
        displayFriends(profile,nameBaseline);
    }
/*
 * This method displays the name of the profile onto the canvas and returns the lower boundary y-coordinate
 * of the GLabel.
 */
    private double displayName(FacePamphletProfileExtension profile){
        GLabel name=new GLabel(profile.getName());
        name.setFont(PROFILE_NAME_FONT);
        name.setLocation(LEFT_MARGIN, TOP_MARGIN+name.getAscent());
        name.setColor(Color.BLUE);
        add(name);
        return TOP_MARGIN+name.getAscent();
    }
/*
 * This method displays the image of the profile onto the canvas and returns the lower boundary y-coordinate
 * of the GLabel. If the image is null, displays a rectangle where the image should be with "No Image" centered
 * in it. Returns the same coordinates as if the image was not null.
 */
    private double displayImage(FacePamphletProfileExtension profile,double nameBaseline){
        if(profile.getImage()==null){
            GRect rect=new GRect(LEFT_MARGIN,IMAGE_MARGIN+nameBaseline,IMAGE_WIDTH,IMAGE_HEIGHT);
            add(rect);
            GLabel noImage=new GLabel("No Image");
            noImage.setFont(PROFILE_IMAGE_FONT);
            noImage.setLocation(rect.getX()+rect.getWidth()/2-noImage.getWidth()/2,rect.getY()+rect.getHeight()/2+noImage.getAscent()/2);
            add(noImage);
            return IMAGE_MARGIN+nameBaseline+rect.getHeight();
        } else{
            GImage image=profile.getImage();
            image.scale(1.0/image.getWidth()*IMAGE_WIDTH, 1.0/image.getHeight()*IMAGE_HEIGHT);
            image.setLocation(LEFT_MARGIN, IMAGE_MARGIN+nameBaseline);
            add(image);
            return IMAGE_MARGIN+nameBaseline+image.getHeight();
        }
    }
/*
 * This method displays the status of the profile onto the canvas. If the status is an empty string, 
 * displays that there is no current status.
 */
    private void displayStatus(FacePamphletProfileExtension profile,double imageBaseline){
        GLabel status;
        if(profile.getStatus().equals("")){
            status=new GLabel("No current status");
        } else{
            status=new GLabel(profile.getName()+" is "+profile.getStatus());
        }
        status.setFont(PROFILE_STATUS_FONT);
        status.setLocation(LEFT_MARGIN, imageBaseline+status.getAscent()+STATUS_MARGIN);
        add(status);
    }
/*
 * This method displays the friends of the profile on the right hand side of the canvas.
 */
    private void displayFriends(FacePamphletProfileExtension profile,double nameBaseline){
        int friendCounter=0;
        GLabel friends=new GLabel("Friends:");
        friends.setFont(PROFILE_FRIEND_LABEL_FONT);
        friends.setLocation(getWidth()/2, nameBaseline+IMAGE_MARGIN);
        add(friends);
        Iterator<String> it=profile.getFriends();
        while(it.hasNext()){
            friendCounter++;
            GLabel nextFriend=new GLabel(it.next());
            nextFriend.setFont(PROFILE_FRIEND_FONT);
            nextFriend.setLocation(getWidth()/2,nameBaseline+IMAGE_MARGIN+nextFriend.getHeight()*friendCounter);
            add(nextFriend);
        }
    }
}