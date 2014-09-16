// This program plays the Boggle game.

#include "lexicon.h"
#include <cctype>
#include "console.h"
#include "simpio.h"
#include "grid.h"
#include "Boggle.h"
#include "bogglegui.h"
#include "strlib.h"

// Function prototypes.
void initializeGUI();
bool checkBoardLetters(string boardLetters);
string getUserLetters(bool randomBoard);
void enterWords(Boggle& b);
string setUpAndGetInput(Boggle& b);
void checkInput(Boggle& b, string input);
void computerTurn(Boggle& b);
void checkWin(Boggle& b);

const int size=4;

// Plays one game of Boggle.
void playOneGame(Lexicon& dictionary) {
    initializeGUI();
    bool randomBoard = getYesOrNo("Do you want to generate a random board? ");
    string boardLetters = getUserLetters(randomBoard);
    Boggle b (dictionary, boardLetters);
    enterWords(b);
    computerTurn(b);
    checkWin(b);
}

// Initializes and resets the GUI.
void initializeGUI(){
    if(!BoggleGUI::isInitialized()){
        BoggleGUI::initialize(size, size);
    }
    BoggleGUI::reset();
    BoggleGUI::setStatusMessage("");
}

// Returns the board letters the user wants to put on the board if the player chooses no
// random board. Else returns an empty string.
string getUserLetters(bool randomBoard){
    string boardLetters="";
    while(!randomBoard){
        boardLetters=getLine("Type the "+ integerToString(size*size) +" letters to appear on the board: ");
        if(checkBoardLetters(boardLetters)){
            break;
        }
        cout << "That is not a valid "<< size*size <<"-letter board string. Try again." << endl;
    }
    return boardLetters;
}

// Checks and returns if board letters input is valid or not.
bool checkBoardLetters(string boardLetters){
    if(boardLetters.size() != size*size){
        return false;
    } else{
        for(int i=0;i<size*size;i++){
            if(!isalpha(boardLetters[i])){
                return false;
            }
        }
    }
    return true;
}

// Allows the user to enter words calls functions to check if the word can be found on the board or not.
void enterWords(Boggle& b){
    clearConsole();
    cout << "It's your turn!" << endl;
    while(true){
        string input=setUpAndGetInput(b);
        if(input.empty()){
            break;
        }
        clearConsole();
        checkInput(b, input);
    }
}

// Prints necessary instructions and statuses to the console. Gets and returns the next word the user inputs.
string setUpAndGetInput(Boggle& b){
    cout << b << endl;
    cout << "Your words (" << b.getHumanWordSet().size() << "): "<< b.getHumanWordSet().toString() << endl;
    cout << "Your score: " << b.humanScore() << endl;
    string input = getLine("Type a word (or Enter to stop): ");
    return input;
}

// Checks to see if the word is valid and can be found or not.
void checkInput(Boggle& b, string input){
    if(b.checkWord(input)){
        if(b.humanWordSearch(toUpperCase(input))){
            cout << "You found a new word! \"" << toUpperCase(input) << "\"" << endl;
            BoggleGUI::setStatusMessage("You found a new word! \"" + toUpperCase(input) + "\"");
        } else{
            cout << "That word can't be formed on this board." << endl;
            BoggleGUI::setStatusMessage("That word can't be formed on this board.");
        }
    } else{
        cout << "You must enter an unfound 4+ letter word from the dictionary." << endl;
        BoggleGUI::setStatusMessage("You must enter an unfound 4+ letter word from the dictionary.");
    }
}

// Calls the computer to find remaining words.
void computerTurn(Boggle& b){
    Set<string> computerSet=b.computerWordSearch();
    BoggleGUI::setScore(b.getScoreComputer(), BoggleGUI::COMPUTER);
    cout << "It's my turn!" << endl;
    cout << "My words (" << computerSet.size() << "): " << computerSet.toString() << endl;
    cout << "My score: " << b.getScoreComputer() << endl;
}

// Checks if the player has won or not.
void checkWin(Boggle& b){
    if(b.getScoreComputer()>b.humanScore()){
        cout << "Ha ha ha, I destroyed you. Better luck next time, puny human!" << endl;
        BoggleGUI::setStatusMessage("Ha ha ha, I destroyed you. Better luck next time, puny human!");
    } else{
        cout << "WOW, you defeated me! Congratulations!" << endl;
        BoggleGUI::setStatusMessage("WOW, you defeated me! Congratulations!");
    }
}
