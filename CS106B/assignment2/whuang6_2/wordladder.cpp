/*
 * This program takes two words and finds a word ladder between them.
 * EXTENSION: can add letters to the string if necessary to go to the next word.
 * EXTENSION: can subtract letters in the string if necessary to go to the next word.
 * EXTENSION: end words can be outside of the dictionary.
 */

#include <cctype>
#include <cmath>
#include <fstream>
#include <iostream>
#include <string>
#include "console.h"
#include "simpio.h"
#include "filelib.h"
#include "lexicon.h"
#include "queue.h"
#include "stack.h"
#include "hashset.h"

using namespace std;

/*
 * Function prototypes.
 */
void printIntro();
Lexicon readDictionary();
bool checkValidWords(Lexicon& storedDictionary, string firstWord, string secondWord);
void findWordLadder(Lexicon& storedDictionary, string firstWord, string secondWord);
HashSet<string> findNextWords(Lexicon& storedDictionary, string word, bool addLetters, bool subtractLetters);
void printSolution(Stack<string> stack, string firstWord, string secondWord);

/*
 * This main function begins the word ladder program. It takes in two words and
 * finds a word ladder in between them. Uppercase and lowercase are treated the same.
 */
int main() {
    setConsoleSize(750, 450);
    setConsoleFont("Monospaced-Bold-16");
    setConsoleEcho(true);
    setConsolePrintExceptions(true);

    Lexicon storedDictionary = readDictionary();
    string firstWord;
    string secondWord;
    bool validWords=false;
    printIntro();

    while(true){
        firstWord=getLine("Word #1 (or Enter to quit): ");
        if (firstWord==""){
            break;
        }
        secondWord=getLine("Word #2 (or Enter to quit): ");
        if (secondWord==""){
            break;
        }
        firstWord=toLowerCase(firstWord);
        secondWord=toLowerCase(secondWord);
        validWords=checkValidWords(storedDictionary, firstWord, secondWord);
        if(validWords){
            findWordLadder(storedDictionary, firstWord, secondWord);
            validWords=false;
        }
        cout << endl;
    }
    cout << "Have a nice day." << endl;
    return 0;
}

/*
 * Prints the introduction to the program.
 */
void printIntro(){
    cout << "Welcome to CS 106B Word Ladder." << endl;
    cout << "Please give me two English words, and I will change the" << endl;
    cout << "first into the second by changing one letter at a time." << endl;
    cout << endl;
}

/*
 * Reads in the dictionary and stores it as a Lexicon.
 */
Lexicon readDictionary(){
    Lexicon storedDictionary;
    storedDictionary.addWordsFromFile("dictionary.txt");
    return storedDictionary;
}

/*
 * Checks the words to see if they are valid words to input.
 * EXTENSION: end words can be outside of the dictionary.
 */
bool checkValidWords(Lexicon& storedDictionary, string firstWord, string secondWord){
//    if (firstWord.length()!=secondWord.length()){
//        cout << "The two words must be the same length." << endl;
//        return false;
//    } else
    if (firstWord==secondWord){
        cout << "The two words must be different." << endl;
        return false;
    }
    return true;
}

/*
 * The algorithm explained in the assignment instructions. Queues a stack of the first word,
 * then repeatedly takes stacks out of the queue and queues a new copy of the stack with the
 * next word until the queue is empty or the word is found.
 * EXTENSION: can add letters to the string if necessary.
 * EXTENSION: can subtract letters in the string if necessary.
 */
void findWordLadder(Lexicon& storedDictionary, string firstWord, string secondWord){
    Queue<Stack<string> > queue;
    Stack<string> stack;
    HashSet<string> wordsUsed;
    bool endFind=false;
    string currentWord;

    stack.push(firstWord);
    queue.enqueue(stack);
    wordsUsed.add(firstWord);

    while(!queue.isEmpty()){
        stack=queue.dequeue();
        string topWord=stack.peek();
        HashSet<string> nextWords = findNextWords(storedDictionary, topWord, topWord.length()<secondWord.length(), topWord.length()>secondWord.length());
        for (string word: nextWords){ // Iterates through the next words.
            if(!wordsUsed.contains(word)){
                if(word==secondWord){
                    currentWord=word;
                    break;
                } else{ // Enqueues a new stack containing a new next word.
                    Stack<string> copy=stack;
                    copy.push(word);
                    wordsUsed.add(word);
                    queue.enqueue(copy);
                }
            }
        }
        if(currentWord==secondWord){
            break;
        }
    }
    if(!queue.isEmpty()) {
        printSolution(stack, firstWord, secondWord);
    } else{
        cout << "No word ladder found from " << secondWord << " back to " << firstWord << "." << endl;
    }
}

/*
 * Iterates through the word and returns a HashSet of words that are possible next
 * words. A word is a possible next word if it is one letter off from the previous word
 * and is a valid English word.
 * EXTENSION: can add letters to the string if necessary.
 * EXTENSION: can subtract letters in the string if necessary.
 */
HashSet<string> findNextWords(Lexicon& storedDictionary, string word, bool addLetters, bool subtractLetters){
    HashSet<string> nextWords;
    string copyWord;
    for(int i=0;i<word.length();i++){
        for(int j='a';j<'z'+1;j++){
            copyWord=word;
            copyWord[i]= (char) j;
            if(storedDictionary.contains(copyWord)){
                nextWords.add(copyWord);
            }
            if(addLetters){
                copyWord=word.substr(0,i)+(char) j+word.substr(i);
                if(storedDictionary.contains(copyWord)){
                    nextWords.add(copyWord);
                }
            }
            if(subtractLetters){
                copyWord=word.substr(0,i-1)+word.substr(i);
                if(storedDictionary.contains(copyWord)){
                    nextWords.add(copyWord);
                }
            }
        }
    }
    return nextWords;
}

/*
 * Prints the word ladder from the second input word back to the first.
 */
void printSolution(Stack<string> stack, string firstWord, string secondWord){
    cout << "A ladder from " << secondWord << " back to " << firstWord << ":" << endl;
    cout << secondWord;
    while(!stack.isEmpty()){
        cout << " " << stack.pop();
    }
    cout << endl;
}
