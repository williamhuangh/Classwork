// This program plays the evil hangman game.
#include <iostream>
#include <sstream>
#include <fstream>
#include <cmath>
#include <vector>

using namespace std;

// Function prototypes
void readDictionary(vector<string>& v);
int promptLength(vector<string>& v);
int getInteger(string prompt);
bool searchLength(vector<string>& v, int length);
int promptGuesses();
bool promptWordList();
void playGame(vector<string>& v, int length, int guesses, bool wordList);
vector<string> filterWordsByLength(vector<string>& v, int length);
void playOneRound(vector<vector<string> >& master, vector<string>& v, int& guesses, bool wordList, string& currentWord, string& guessed);
void constructNewList(vector<vector<string> >& master, vector<string>& v, int& guesses, string& currentWord, char ch);
string newCurrentWord(string currentWord, string compare, char ch);
int findBiggestFam(vector<vector<string> >& master);
bool checkFam(string check, string s, char ch);
char getCharacter(string& guessed);

// Main function starts of the game.
int main(){
    vector<string> dictionary;
    readDictionary(dictionary);
    while(true){
        int length = promptLength(dictionary);
        int guesses = promptGuesses();
        bool wordList = promptWordList();
        playGame(dictionary, length, guesses, wordList);
        string response;
        while(true){
            cout << "Want to play again? Y or N? ";
            getline(cin, response);
            if(response == "Y" || response == "N") break;
        }
        if(response == "N") break;
    }
    return 0;
}

// Reads in the dictionary text file into a vector.
void readDictionary(vector<string>& v){
    ifstream input;
    input.open("dictionary.txt");
    string word;
    while(true){
        input >> word;
        if(input.fail()) break;
        v.push_back(word);
    }
}

// Asks the user for a word length.
int promptLength(vector<string>& v){
    while(true){
        int length = getInteger("Enter a word length: ");
        if(searchLength(v, length)) return length;
        cout << "There is no word in the dictionary with length " << length << endl;
    }
}

// Gets an integer from cin. This is the code we went over in class.
int getInteger(string prompt) {
    int value;
    string line;
    while (true) {
        cout << prompt;
        getline(cin, line);
        istringstream stream(line);
        stream >> value >> ws;
        if (!stream.fail() && stream.eof()) break;
        cout << "Illegal integer format. Try again." << endl;
        if (prompt == "") prompt = "Enter an integer: ";
    }
    return value;
}

// Returns true if there is a string in the dictionary with given length.
bool searchLength(vector<string>& v, int length){
    for(string str: v){
        if(str.length() == length) return true;
    }
    return false;
}

// Prompts the user for a valid number of guesses.
int promptGuesses(){
    while (true) {
        int i = getInteger("Enter number of guesses: ");
        if(i > 0) return i;
        cout << "Guesses must be greater than 0" << endl;
    }
}

// Asks the user if he/she wants the count of words left or not.
bool promptWordList(){
    while(true){
        int i = getInteger("Enter 1 if you want the number of words left in the word list. Enter 0 if not: ");
        if(i == 1) return true;
        if(i == 0) return false;
    }
}

// Plays the evil hangman game.
void playGame(vector<string>& v, int length, int guesses, bool wordList){
    vector<string> wordsLeft = filterWordsByLength(v, length);
    vector<vector<string> > master;
    string currentWord;
    string guessed = "";
    for(int i = length; i > 0; i--){
        currentWord += '-';
    }
    while(guesses > 0 && currentWord.find('-') != std::string::npos){
        playOneRound(master, wordsLeft, guesses, wordList, currentWord, guessed);
        master.clear();
    }
    if(wordsLeft.size() == 1){
        cout << "You win! Word was: " << wordsLeft.at(0) << endl;
    } else{
        cout << "You lose! Word was: " << wordsLeft.at(0) << endl;
    }
}

// Filters the dictionary in the very beginning, returning only the words that have a given length.
vector<string> filterWordsByLength(vector<string>& v, int length){
    vector<string> result;
    for(string s: v){
        if(s.length() == length) result.push_back(s);
    }
    return result;
}

// Plays one round of the evil hangman game.
void playOneRound(vector<vector<string> >& master, vector<string>& v, int& guesses, bool wordList, string& currentWord, string& guessed){
    cout << "Guesses left: " << guesses << endl;
    cout << "You have already guessed: " << guessed << endl;
    cout << "Current word is: " << currentWord << endl;
    if(wordList) cout << "Words available remaining: " << v.size() << endl;
    char ch = getCharacter(guessed);
    constructNewList(master, v, guesses, currentWord, ch);
}

// Constructs a new list of leftover words depending on the character guess.
void constructNewList(vector<vector<string> >& master, vector<string>& v, int& guesses, string& currentWord, char ch){
    for(string s: v){
        bool foundFam = false;
        for(int i = 0; i < master.size(); i++){
            if(checkFam(master.at(i).at(0), s, ch)){
                master.at(i).push_back(s);
                foundFam = true;
                break;
            }
        }
        if(!foundFam){ // Create new word family as a new vector
            vector<string> newVec;
            newVec.push_back(s);
            master.push_back(newVec);
        }
    }
    int index = findBiggestFam(master);
    v = master.at(index);
    string newWord = newCurrentWord(currentWord, v.at(0), ch);
    if(newWord == currentWord){
        guesses--;
    }
    currentWord = newWord;
}

// Constructs the new current word display depending on the character guessed and the word family chosen.
string newCurrentWord(string currentWord, string compare, char ch){
    for(int i = 0; i < currentWord.length(); i++){
        if(compare[i] == ch) currentWord[i] = ch;
    }
    return currentWord;
}

// Finds and returns the index of the largest word family. If there are ties, returns the first one of the largest number.
int findBiggestFam(vector<vector<string> >& master){
    int index = 0;
    int bigSize = 0;
    for(int i = 0; i < master.size(); i++){
        if(master.at(i).size() > bigSize){
            bigSize = master.at(i).size();
            index = i;
        }
    }
    return index;
}

// Checks to see if a passed in string is the same family as the first parameter passed in string.
bool checkFam(string check, string s, char ch){
    for(int i = 0; i < check.length(); i++){
        if((check[i] == ch && s[i] != ch) || (s[i] == ch && check[i] != ch)) return false;
    }
    return true;
}

// Gets a character from cin and returns it.
char getCharacter(string& guessed){
    char ch;
    while(true){
        cout << "Guess a character: ";
        string currGuess;
        getline(cin, currGuess);
        if(currGuess.length() == 1 && currGuess[0]>='a' && currGuess[0]<='z'){
            ch = currGuess[0];
            bool found = false;
            for(int i = 0; i < guessed.length(); i++){
                if(guessed[i] == ch) found = true;
            }
            if(!found) break;
        }
        cout << endl;
    }
    cout << endl;
    guessed += ch;
    return ch;
}
