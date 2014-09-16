// This class is for Boggle and contains its private and public methods.

#include "Boggle.h"
#include "bogglegui.h"

// letters on all 6 sides of every cube
static string CUBES[16] = {
    "AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
    "EIOSST", "ELRTTY", "HIMNQU", "HLNNRZ"
};

// Class variables.
Grid<char> boardLetters;
Set<string> wordsFoundByHuman;
Lexicon dictionary;
int humansScore;
int computersScore;
const int size=4;
const int cubeSize=6;
const int minimumWordLength=4;
const int pauseTime=100;

// Constructor that initializes the boggle grid and puts in the letters.
Boggle::Boggle(Lexicon& dictionary, string boardText) {
    boardLetters.resize(size, size);
    humansScore=0;
    computersScore=0;
    this->dictionary=dictionary;

    if(!boardText.empty()){
        putLettersInBoard(boardText);
    } else{
        randomizeBoard();
    }
}

// This private function puts the user input letters into the board.
void Boggle::putLettersInBoard(string boardText){
    int i=0;
    for(int j=0;j<size;j++){
        for(int k=0;k<size;k++){
            boardLetters[j][k]=toupper(boardText[i++]);
        }
    }
    BoggleGUI::labelAllCubes(toUpperCase(boardText));
}

// This private function randomizes the letters onto the boggle board.
void Boggle::randomizeBoard(){
    int i = 0;
    for(int j=0;j<size;j++){
        for(int k=0;k<size;k++){
            int letterNumber = randomInteger(0, cubeSize-1);
            boardLetters[j][k]=CUBES[i++].at(letterNumber);
        }
    }
    shuffle(boardLetters);
    matchGUI();
}

// This private method matches the GUI to the grid letters.
void Boggle::matchGUI(){
    string letters="";
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            letters+=boardLetters[i][j];
        }
    }
    BoggleGUI::labelAllCubes(letters);
}

// This function returns the letter corresponding to the passed in row and column.
// Throws an exception if the row or column are out of range.
char Boggle::getLetter(int row, int col) const{
    if(row>=0 && row<size && col>=0 && col<size){
        return boardLetters[row][col];
    } else{
        throw row;
    }
}

// Checks if the word is a valid word. Has to be at least 4 in length, in the dictionary, and not found yet.
bool Boggle::checkWord(string word) const{
    if(!word.length()>=minimumWordLength){
        return false;
    } else if(!dictionary.contains(word)){
        return false;
    } else if(wordsFoundByHuman.contains(toUpperCase(word))){
        return false;
    } else{
        return true;
    }
}

// This function starts off the human word search algorithm. Loops through the letters on the board as first letters
// in the word.
bool Boggle::humanWordSearch(string word) {
    BoggleGUI::clearHighlighting();
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            animate(i, j, true);
            if(boardLetters[i][j]==word[0]){
                if(exploreFirstLetter(i, j, word)){
                    return true;
                }
            }
            animate(i, j, false);
        }
    }
    return false;
}

// This function highlights or unhighlights a specific square on the GUI.
void Boggle::animate(int row, int col, bool highlight){
    BoggleGUI::labelCube(row,col,getLetter(row,col), highlight);
    BoggleGUI::setAnimationDelay(pauseTime);
}

// This function calls the recursive function on the first letter if it matches the first letter of the input.
bool Boggle::exploreFirstLetter(int row, int col, string word){
    boardLetters[row][col]='0';
    if(humanWordSearchHelper(word.substr(1), row, col)){
        wordsFoundByHuman.add(word);
        BoggleGUI::recordWord(word, BoggleGUI::HUMAN);
        boardLetters[row][col]=word[0];
        addPoints(word.length(), 0);
        return true;
    }
    boardLetters[row][col]=word[0];
    return false;
}

// This function is the recursive function. Returns true if word is empty and if not, checks the squares around the
// passed in row and column.
bool Boggle::humanWordSearchHelper(string word, int row, int col){
    if(word.empty()){
        return true;
    } else {
        for(int i=-1;i<=1;i++){ // Double for loop to call checkIndividualGrids on all squares around the passed in row/col.
            for(int j=-1;j<=1;j++){
                if(!(i==0 && j==0)){
                    if(checkIndividualGrids(word, row+i, col+j)){
                        return true;
                    }
                }
            }
        }
    }
    return false;
}

// Checks the specific passed in square to see if it matches the first letter of the word. If it does, calls
// humanWordSearchHelper recursive function with the word without the first letter.
bool Boggle::checkIndividualGrids(string word, int row, int col){
    if(boardLetters.inBounds(row,col)){
        if(boardLetters[row][col]!='0'){
            animate(row, col, true);
            if (boardLetters[row][col]==word[0]){
                boardLetters[row][col]='0';
                if(humanWordSearchHelper(word.substr(1), row, col)){
                    boardLetters[row][col]=word[0];
                    return true;
                }
                boardLetters[row][col]=word[0];
            }
            animate(row, col, false);
        }
    }
    return false;
}

// Returns the human's current score.
int Boggle::humanScore() const {
    return humansScore;
}

// Function that leads up to the computer search recursive function. Loops through all letters on the grid as first letters
// of a word.
Set<string> Boggle::computerWordSearch() {
    BoggleGUI::clearHighlighting();
    Set<string> wordsFoundByComputer;
    string currentPrefix="";
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            exploreComputerLetter(wordsFoundByComputer, i, j, currentPrefix);
        }
    }
    return wordsFoundByComputer;
}

// This function explores all possibilities of the letter that is passed in for position row/col.
void Boggle::exploreComputerLetter(Set<string>& wordsFoundByComputer, int row, int col, string firstLetter){
    char temp=boardLetters[row][col];
    boardLetters[row][col]='0';
    firstLetter+=temp;
    computerWordSearchHelper(wordsFoundByComputer, firstLetter, row, col);
    boardLetters[row][col]=temp;
}

// Recursive function. If the current prefix is a word itself, records it, adds it onto the GUI, and adds points.
// Explores further bordering squares if there is still an english word with the prefix of currentPrefix.
void Boggle::computerWordSearchHelper(Set<string>& wordsFoundByComputer, string currentPrefix, int row, int col){
    if(currentPrefix.size()>=minimumWordLength && dictionary.contains(currentPrefix) && !wordsFoundByHuman.contains(currentPrefix)
            && !wordsFoundByComputer.contains(currentPrefix)){
        wordsFoundByComputer.add(currentPrefix);
        addPoints(currentPrefix.length(), 1);
        BoggleGUI::recordWord(currentPrefix, BoggleGUI::COMPUTER);
    }
    if(dictionary.containsPrefix(currentPrefix)){
        for(int i=-1;i<=1;i++){
            for(int j=-1;j<=1;j++){ // Double for loop to explore bordering squares.
                if(!(i==0 && j==0)){
                    exploreBorderingSquare(wordsFoundByComputer, currentPrefix, row+i, col+j);
                }
            }
        }
    }
}

// Calls the recursive computerWordSearchHelper function on all bordering squares.
void Boggle::exploreBorderingSquare(Set<string>& wordsFoundByComputer, string currentPrefix, int row, int col){
    if(boardLetters.inBounds(row, col)){
        if(boardLetters[row][col]!='0'){
            char temp=boardLetters[row][col];
            boardLetters[row][col]='0';
            currentPrefix+=temp;
            computerWordSearchHelper(wordsFoundByComputer, currentPrefix, row, col);
            boardLetters[row][col]=temp;
        }
    }
}

// Returns the computer's current score.
int Boggle::getScoreComputer() const{
    return computersScore;
}

// Adds points to either the human or the computer. Redisplays the human score each time.
void Boggle::addPoints(int wordLength, int player){
    int points=wordLength-minimumWordLength+1;
    if (player==0){
        humansScore+=points;
        BoggleGUI::setScore(humansScore, BoggleGUI::HUMAN);
    } else {
        computersScore+=points;
    }
}

// Returns the set of strings that are the words the human player has found.
Set<string> Boggle::getHumanWordSet() const{
    return wordsFoundByHuman;
}

// Prints out characters of the boggle board if "<< b" is called.
ostream& operator<<(ostream& out, const Boggle& b) {
    for(int i=0;i<size;i++){
        for(int j=0;j<size;j++){
            out << b.getLetter(i, j);
        }
        out << endl;
    }
    return out;
}
