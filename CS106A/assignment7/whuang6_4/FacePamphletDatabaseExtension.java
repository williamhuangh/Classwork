/*
 * File: FacePamphletDatabaseExtension.java
 * -------------------------------
 * This class keeps track of the profiles of all users in the
 * FacePamphlet application.  Note that profile names are case
 * sensitive, so that "ALICE" and "alice" are NOT the same name.
 */
 
import java.util.*;
import java.io.*;
import acm.graphics.*;
import acm.util.*;
 
public class FacePamphletDatabaseExtension implements FacePamphletConstants {
	
    private Map<String, FacePamphletProfileExtension> profileTracker=new HashMap<String, FacePamphletProfileExtension>();
    /** 
     * Constructor
     * This method takes care of any initialization needed for 
     * the database.
     */
    public FacePamphletDatabaseExtension() {
        // Empty
    }
/*
 * EXTENSION writes the database into the passed in file name. Returns true if it is successful and false if it
 * isn't.
 */
    public boolean saveTextFile(String file){
    	try{
    		PrintWriter wr=new PrintWriter(new FileWriter(file));
    		int profiles=profileTracker.size();
    		wr.println(""+profiles);
    		writeFile(wr);
    		wr.close();
    		return true;
    	} catch(IOException ex){
    		return false;
    	}
    }
/*
 * EXTENSION This method loads the file into the database and returns true if it can and returns false if it 
 * cannot.
 */
    public boolean loadTextFile(String file){
    	try{
    		BufferedReader rd=new BufferedReader(new FileReader(file));
    		profileTracker.clear();
    		String profiles;
    		if((profiles=rd.readLine())!=null){
	    		int numberOfProfiles=Integer.parseInt(profiles);
	    		readFile(rd,numberOfProfiles);
    		}
    		rd.close();
    		return true;
    	} catch (IOException ex){
    		return false;
    	}
    }
/*
 * EXTENSION This method actually writes the text for each profile into the file.
 */
    private void writeFile(PrintWriter wr){
    	Iterator<String> it= profileTracker.keySet().iterator();
		while(it.hasNext()){
			String profileName=it.next();
			FacePamphletProfileExtension profile=profileTracker.get(profileName);
			wr.println(profileName);
			
			if(profile.getImageName()!=null){
				wr.println(profile.getImageName());
			} else{
				wr.println("");
			}
			
			if(profile.getStatus()!=null){
				wr.println(profile.getStatus());
			} else{
				wr.println("");
			}
			
			Iterator<String> friends=profile.getFriends();
			while(friends.hasNext()){
				wr.println(friends.next());
			}
			
			wr.println("");
		}
    }
/*
 * EXTENSION This method reads in each individual profile into the database.
 */
    private void readFile(BufferedReader rd,int numberOfProfiles){
    	try{
    		for(int i=0;i<numberOfProfiles;i++){
		    	String profileName=rd.readLine();
		    	String image=null;
		    	String status=null;
		    	String friend=null;
		    	
		    	FacePamphletProfileExtension profile=new FacePamphletProfileExtension(profileName);
		    	if(!(image=rd.readLine()).equals("")){
		    		GImage profileImage=new GImage(image);
		    		profile.setImage(profileImage,image);
		    	}
		    	if(!(status=rd.readLine()).equals("")){
		    		profile.setStatus(status);
		    	}
		    	while(!(friend=rd.readLine()).equals("")){
		    		profile.addFriend(friend);
		    	}
		    	profileTracker.put(profileName, profile);
    		}
    	} catch(IOException ex){
    		throw new ErrorException(ex);
    	}
    }
    /** 
     * This method adds the given profile to the database.  If the 
     * name associated with the profile is the same as an existing 
     * name in the database, the existing profile is replaced by 
     * the new profile passed in.
     */
    public void addProfile(FacePamphletProfileExtension profile) {
        String name=profile.getName();
        profileTracker.put(name, profile);
    }
 
     
    /** 
     * This method returns the profile associated with the given name 
     * in the database.  If there is no profile in the database with 
     * the given name, the method returns null.
     */
    public FacePamphletProfileExtension getProfile(String name) {
        if(profileTracker.containsKey(name)){
            return profileTracker.get(name);
        }
        return null;
    }
     
     
    /** 
     * This method removes the profile associated with the given name
     * from the database.  It also updates the list of friends of all
     * other profiles in the database to make sure that this name is
     * removed from the list of friends of any other profile.
     * 
     * If there is no profile in the database with the given name, then
     * the database is unchanged after calling this method.
     */
    public void deleteProfile(String name) {
        if(profileTracker.containsKey(name)){
            Iterator<String> it=profileTracker.get(name).getFriends();
            while(it.hasNext()){
                profileTracker.get(it.next()).removeFriend(name);
            }
            profileTracker.remove(name);
        }
    }
 
     
    /** 
     * This method returns true if there is a profile in the database 
     * that has the given name.  It returns false otherwise.
     */
    public boolean containsProfile(String name) {
        if(profileTracker.containsKey(name)){
            return true;
        }
        return false;
    }
}