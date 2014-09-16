/*
 * File: NameSurferEntry.java
 * --------------------------
 * This class represents a single entry in the database.  Each
 * NameSurferEntry contains a name and a list giving the popularity
 * of that name for each decade stretching back to 1900.
 */

import acm.util.*;
import java.util.*;

public class NameSurferEntry implements NameSurferConstants {
	String name;
	int[] popularitiesByDecade;
/**
 * Creates a new NameSurferEntry from a data line as it appears
 * in the data file.  Each line begins with the name, which is
 * followed by integers giving the rank of that name for each
 * decade. Sets the instance variable name with the corresponding
 * name and sets the instance variable popularitiesByDecade
 * with the corresponding ranks.
 */
	public NameSurferEntry(String line) {
		StringTokenizer tokenizer=new StringTokenizer(line);
		name=tokenizer.nextToken();
		popularitiesByDecade=new int[NDECADES];
		for(int i=0;i<NDECADES;i++){
			String currentPopularity=tokenizer.nextToken();
			int numericalPopularity=Integer.parseInt(currentPopularity);
			popularitiesByDecade[i]=numericalPopularity;
		}
	}

/**
 * Returns the name associated with this entry.
 */
	public String getName() {
		return name;
	}

/**
 * Returns the rank associated with an entry for a particular
 * decade.  The decade value is an integer indicating how many
 * decades have passed since the first year in the database,
 * which is given by the constant START_DECADE.  If a name does
 * not appear in a decade, the rank value is 0.
 */
	public int getRank(int decade) {
		int rank=popularitiesByDecade[decade];
		return rank;
	}

/**
 * Returns a string that makes it easy to see the value of a
 * NameSurferEntry.
 */
	public String toString() {
		String returnString=name+" ["+popularitiesByDecade[0];
		for(int i=1;i<NDECADES;i++){
			returnString+=(" "+popularitiesByDecade[i]);
		}
		returnString+="]";
		return returnString;
	}
}

