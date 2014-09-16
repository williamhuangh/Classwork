import java.io.*;
import acm.util.*;
import java.util.*;
/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDataBase implements NameSurferConstants {
	Map<String,String> namesToLines;
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	public NameSurferDataBase(String filename) {
		try{
			BufferedReader rd=new BufferedReader(new FileReader(filename));
			namesToLines=new HashMap<String,String>();
			String line;
			while ((line=rd.readLine())!=null){
				loadHashMap(line);
			}
			rd.close();
		} catch(IOException ex){
			throw new ErrorException(ex);
		}
	}
/*
 * This method fills up the hashmap with the names in the file as a key mapping to the corresponding
 * line in the data file. It is not case sensitive.
 */
	private void loadHashMap(String line){
		StringTokenizer tokenizer=new StringTokenizer(line);
		String name=tokenizer.nextToken().toLowerCase();
		namesToLines.put(name, line);
	}
	
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	public NameSurferEntry findEntry(String name) {
		name=name.toLowerCase();
		if(namesToLines.containsKey(name)){
			String convertToNameSurferEntry=namesToLines.get(name);
			NameSurferEntry entry=new NameSurferEntry(convertToNameSurferEntry);
			return entry;
		} else{
			return null;
		}
	}
}

