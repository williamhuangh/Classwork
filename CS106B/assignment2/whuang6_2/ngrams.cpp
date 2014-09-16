/*
 * This program executes the ngrams assignment.
 * EXTENSION: always begins with a word with a capital letter.
 * EXTENSION: always ends with a complete sentence (period, question mark, or exclamation point).
 */

#include <cctype>
#include <cmath>
#include <fstream>
#include <iostream>
#include <string>
#include "console.h"
#include "filelib.h"
#include "simpio.h"
#include "hashmap.h"
#include "random.h"

/*
 * Function prototypes.
 */
using namespace std;
void printIntro();
Vector<string> storeAllWords(ifstream& stream);
HashMap<Vector<string>,Vector<string> > storeHashMap(Vector<string>& allWords, int n);
void generateWords(HashMap<Vector<string>,Vector<string> >& nGramHashMap, int randomWords);
Vector<string> generateRandomStart(HashMap<Vector<string>,Vector<string> >& nGramHashMap);

/*
 * The main function starts the ngrams program.
 */
int main() {
    setConsoleSize(750, 450);
    setConsoleFont("Monospaced-Bold-16");
    setConsoleEcho(true);
    setConsolePrintExceptions(true);
    ifstream stream;
    int n;
    int randomWords;

    printIntro();
    promptUserForFile(stream, "Input file name? "); // User input
    while(true){
        n = getInteger("Value of N? ");
        if(n>1){
            break;
        }
        cout << "N must be 2 or greater." << endl;
    }
    Vector<string> allWords=storeAllWords(stream);
    HashMap<Vector<string>,Vector<string> > nGramHashMap = storeHashMap(allWords, n);

    while(true){
        randomWords = getInteger("# of random words to generate (0 to quit)? ");
        if(randomWords==0){
            break;
        } else if(randomWords>=n){
            generateWords(nGramHashMap, randomWords);
            cout << endl;
        } else{
            cout << "Must be at least "<< n << " words" << endl;
            cout << endl;
        }
    }
    cout << "Exiting." << endl;
    return 0;
}

/*
 * Prints the intro of the program to the console.
 */
void printIntro(){
    cout << "Welcome to CS 106B Random Writer ('N-Grams')." << endl;
    cout << "This program makes random text based on a document." << endl;
    cout << "Give me an input file and an 'N' value for groups" << endl;
    cout << "of words, and I'll create random text for you." << endl;
    cout << endl;
}

/*
 * This function stores all words in the file with their punctuation inside a large vector and
 * returns it.
 */
Vector<string> storeAllWords(ifstream& stream){
    Vector<string> allWords;
    string line;

    while(true){
        getline(stream, line);
        if(stream.fail()){
            break;
        }
        line=trim(line);
        while(line.find(" ")!=string::npos){
            allWords.add(line.substr(0,line.find(" ")));
            line.erase(0,line.find(" ")+1);
        }
        if (line!=""){
            allWords.add(line);
        }
    }
    return allWords;
}

/*
 * This function takes the allWords vector and the N number for ngrams and stores
 * the words into a HashMap accordingly, mapping a window of N-1 words to the words
 * that it follows in the text. Returns that HashMap.
 */
HashMap<Vector<string>,Vector<string> > storeHashMap(Vector<string>& allWords, int n){
    HashMap<Vector<string>,Vector<string> > nGramHashMap;
    Vector<string> window;
    string nextWord;

    for(int i=0;i<n-1 ;i++){ // Sets up the first window.
        window.add(allWords.get(i));
    }
    for(int j=n-1;j<allWords.size()+n-1;j++){ // Iterates through all possible windows by deleting the first value
        if(j>allWords.size()-1){              // in the window and adding the next from allWords.
            nextWord=allWords[j-allWords.size()];
        } else{
            nextWord=allWords[j];
        }
        if(!nGramHashMap.containsKey(window)){
            Vector<string> newVector;
            newVector.add(nextWord);
            nGramHashMap.put(window, newVector);
        } else{
            Vector<string> newVector=nGramHashMap.get(window);
            newVector.add(nextWord);
            nGramHashMap.put(window, newVector);
        }
        window.remove(0);
        window.add(nextWord);
    }
    return nGramHashMap;
}

/*
 * This function generates the random words and prints them to the screen using the HashMap.
 * EXTENSION: always begins with a word with a capital letter and always ends with a period,
 * exclamation mark, or question mark (a complete sentence).
 */
void generateWords(HashMap<Vector<string>,Vector<string> >& nGramHashMap, int randomWords){
    int randomNextWordNumber;
    string nextWord;
    Vector<string> outputWindow = generateRandomStart(nGramHashMap);

    for(int i=0;i<outputWindow.size();i++){ // Prints out the first window.
        cout << outputWindow[i] << " ";
    }
    int j=outputWindow.size();
    while(true){ // Randomly generates the next word depending on the HashMap key, prints it out.
        if(j<randomWords || (nextWord.at(nextWord.length()-1)!='.' && nextWord.at(nextWord.length()-1)!='!' && nextWord.at(nextWord.length()-1)!='?')){
            randomNextWordNumber=randomInteger(0,nGramHashMap.get(outputWindow).size()-1);
            nextWord=nGramHashMap.get(outputWindow).get(randomNextWordNumber);
            cout << nextWord << " ";
            outputWindow.remove(0);
            outputWindow.add(nextWord);
            j++;
        } else{
            break;
        }
    }
    cout << endl;
}

/*
 * This function uses the HashMap to generate a random starting window vector and returns it.
 * EXTENSION: always generates the first window as something with a capital letter.
 */
Vector<string> generateRandomStart(HashMap<Vector<string>,Vector<string> >& nGramHashMap){
    int hashNumber=1;
    int start = randomInteger(1,nGramHashMap.size());
    while(true){
        for (Vector<string> currentWindow: nGramHashMap){
            if (hashNumber==start){
                if(isupper(currentWindow.get(0).at(0))){
                    return currentWindow;
                }
                hashNumber=1;
                start=randomInteger(1,nGramHashMap.size());
            }
            hashNumber++;
        }
    }
}
