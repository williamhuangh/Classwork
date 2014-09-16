// This .h file lists the public and private components of Boggle.

#ifndef _boggle_h
#define _boggle_h

#include <iostream>
#include <string>
#include "set.h"
#include "lexicon.h"
#include "grid.h"
using namespace std;

class Boggle {
public:
    Boggle(Lexicon& dictionary, string boardText = "");
    char getLetter(int row, int col) const;
    bool checkWord(string word) const;
    bool humanWordSearch(string word);
    int humanScore() const;
    Set<string> computerWordSearch();
    int getScoreComputer() const;
    Set<string> getHumanWordSet() const;

private:
    // Private variables.
    Grid<char> boardLetters;
    Set<string> wordsFoundByHuman;
    Lexicon dictionary;
    int humansScore;
    int computersScore;

    // Private methods.
    void putLettersInBoard(string boardText);
    void matchGUI();
    void randomizeBoard();
    void animate(int row, int col, bool highlight);
    bool exploreFirstLetter(int row, int col, string word);
    bool humanWordSearchHelper(string word, int row, int col);
    bool checkIndividualGrids(string word, int row, int col);
    void exploreComputerLetter(Set<string>& wordsFoundByComputer, int row, int col, string firstLetter);
    void computerWordSearchHelper(Set<string>& wordsFoundByComputer, string currentPrefix, int row, int col);
    void exploreBorderingSquare(Set<string>& wordsFoundByComputer, string currentPrefix, int row, int col);
    void addPoints(int wordLength, int player);
};

ostream& operator<<(ostream& out, const Boggle& b);

#endif // _boggle_h
