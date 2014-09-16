/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.*;
import java.util.*;
import acm.util.*;
/*
 * This class sets up the lexicon that is utilized in the hangman assignment.
 */
public class HangmanLexicon {
	private ArrayList<String> fullLexicon=new ArrayList<String>(); // Instance variable fulllexicon ArrayList.

/*
 * Constructor for the hangman lexicon.
 */
	public HangmanLexicon(){
		try{ // Tries to open up and read the lexicon into the ArrayList.
			BufferedReader rd=new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while(rd.readLine()!=null){
				fullLexicon.add(rd.readLine());
			}
		}catch(IOException ex){ // Catches and throws and error exception if failed.
			throw new ErrorException(ex); 
		}
	}
	
/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return fullLexicon.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return fullLexicon.get(index);
	};
}
