/*
 * This program is the extended version of the game of life.
 * EXTENSION: altered to take into account wrap-around.
 * EXTENSION: included the option to create a random colony if "random" is typed in.
 */

#include <cctype>
#include <cmath>
#include <fstream>
#include <iostream>
#include <sstream>
#include <string>
#include "console.h"
#include "filelib.h"
#include "grid.h"
#include "gwindow.h"
#include "simpio.h"
#include "lifegui.h"
#include "random.h"
using namespace std;

/*
 * Function prototypes.
 */
void printIntro();
void storeFile(Grid<string>& colony);
void readFile(Grid<string>& colony, string file);
void storeLine(Grid<string>& colony, string line, int rowNumber);
void createRandom(Grid<string>& colony);
void startGame(Grid<string>& colony);
void updateGrid(Grid<string>& colony);
void countSurroundForSpecificI(Grid<string> colony, int i, int j, int& surroundingXs);
void createNextColony(int surroundingXs, int i, int j, Grid<string>& copy);
void printGrid(Grid<string>& colony);
const int pauseLength=50;

/*
 * The main method initiates the program and plays the game.
 */
int main() {
    setConsoleSize(750, 500);
    setConsoleFont("Monospaced-Bold-14");
    setConsoleEcho(true);

    Grid<string> colony;
    printIntro();
    storeFile(colony);
    startGame(colony);

    cout << "Have a nice Life!" << endl;
    return 0;
}

/*
 * This method prints out the introduction and the rules of the game.
 */
void printIntro(){
    cout << "Welcome to the CS 106B Game of Life," << endl;
    cout << "a simulation of the lifecycle of a bacteria colony." << endl;
    cout << "Cells (X) live and die by the following rules:" << endl;
    cout << "- A cell with 1 or fewer neighbors dies." << endl;
    cout << "- Locations with 2 neighbors remain stable." << endl;
    cout << "- Locations with 3 neighbors will create life." << endl;
    cout << "- A cell with 4 or more neighbors dies." << endl;
    cout << "" << endl;
}

/*
 * This method takes in a grid of strings. It prompts the user for a file
 * name and once the user inputs an existing file name, calls readFile passing
 * in the grid and the file name.
 */
void storeFile(Grid<string>& colony){
    string file="";
    bool fileExistance=false;
    while(fileExistance==false){
        cout << "Grid input file name? ";
        getline(cin, file);
        fileExistance=fileExists(file);

        if(file=="random"){ // EXTENSION
            fileExistance=true;
        }
        if(fileExistance==false){
            cout << "File not found; please try again." << endl;
        }
    }
    if(file!="random"){
        readFile(colony, file);
    } else{
        createRandom(colony); // EXTENSION
    }
}

/*
 * This method takes in a grid and a file name. It opens the file and
 * calls storeLine on every line which is read into the program from
 * the file.
 */
void readFile(Grid<string>& colony, string file){
    ifstream stream;
    string line;
    openFile(stream, file);
    getline(stream, line);
    int gridRows=stringToInteger(line);
    getline(stream, line);
    int gridColumns=stringToInteger(line);
    colony.resize(gridRows, gridColumns);
    for(int i=0;i<gridRows;i++){
        getline(stream, line);
        storeLine(colony, line, i);
    }
}

/*
 * This function takes in a grid, string, and the row number and stores
 * each character in the corresponding row in the grid.
 */
void storeLine(Grid<string>& colony, string line, int rowNumber){
    for (int i=0;i<colony.numCols();i++){
        colony[rowNumber][i]=line[i];
    }
}

/*
 * EXTENSION: Creates a random grid dimensions between 1 to 500 for columns
 * and rows with a 50% chance of being X or -.
 */
void createRandom(Grid<string>& colony){
    int rows = randomInteger(1,50);
    int columns = randomInteger(1,50);
    colony.resize(rows, columns);
    for(int i=0;i<colony.numRows();i++){
        for(int j=0;j<colony.numCols();j++){
            if(randomChance(0.5)){
                colony[i][j]="X";
            } else{
                colony[i][j]="-";
            }
        }
    }
}

/*
 * This function starts the interactive game. It first prints the grid out
 * to the console and advances the grid by 1 iteration if t is the input, animates
 * the grid if a is the input by a certain number of frames, and quits the while loop
 * if q is the input.
 *
 */
void startGame(Grid<string>& colony){
    string response1="";
    int frames;
    printGrid(colony);
    while(true){
        response1=getLine("a)nimate, t)ick, q)uit? ");
        if(response1=="a"){
            frames=getInteger("How many frames? ");
            for (int i=frames;i>0;i--){
                pause(pauseLength);
                clearConsole();
                updateGrid(colony);
                printGrid(colony);
            }
        } else if(response1=="t"){
            updateGrid(colony);
            printGrid(colony);
        } else if(response1=="q"){
            break;
        } else{
            cout << "Invalid choice; please try again." << endl;
        }
    }
}

/*
 * This function takes in a grid and advances the colony by 1 frame.
 * EXTENSION: altered to take into account wrap-around.
 */
void updateGrid(Grid<string>& colony){
    Grid<string> copy=colony;
    int surroundingXs=0;
    for (int i=0;i<colony.numRows();i++){
        for(int j=0;j<colony.numCols();j++){
            if(i!=0){
                countSurroundForSpecificI(colony, i-1, j, surroundingXs);
            } else{
                countSurroundForSpecificI(colony, colony.numRows()-1, j, surroundingXs);
            }
            if(i!=colony.numRows()-1){
                countSurroundForSpecificI(colony, i+1, j, surroundingXs);
            } else{
                countSurroundForSpecificI(colony, 0, j, surroundingXs);
            }
            if(j!=0){
                if(colony[i][j-1]=="X"){
                    surroundingXs++;
                }
            } else if(colony[i][colony.numCols()-1]=="X"){
                surroundingXs++;
            }
            if(j!=colony.numCols()-1){
                if(colony[i][j+1]=="X"){
                    surroundingXs++;
                }
            } else if (colony[i][0]=="X"){
                surroundingXs++;
            }
            createNextColony(surroundingXs, i, j, copy);
            surroundingXs=0;
        }
    }
    colony=copy;
}

/*
 * This function is an algorithm to count the number of surrounding Xs in the three
 * squares in a line given a certain i-j grid index.
 * EXTENSION: altered to take into account wrap-around.
 */
void countSurroundForSpecificI(Grid<string> colony, int i, int j, int& surroundingXs){
    if(colony[i][j]=="X"){
        surroundingXs++;
    }
    if(j!=0){
        if(colony[i][j-1]=="X"){
            surroundingXs++;
        }
    } else if(colony[i][colony.numCols()-1]=="X"){
        surroundingXs++;
    }
    if(j!=colony.numCols()-1){
        if(colony[i][j+1]=="X"){
            surroundingXs++;
        }
    } else if (colony[i][0]=="X"){
        surroundingXs++;
    }
}

/*
 * This method creates the new colony (next frame) index by index, given
 * the number of Xs that was surrounding the indexes of the previous colony.
 */
void createNextColony(int surroundingXs, int i, int j, Grid<string>& copy){
    if(surroundingXs==0 || surroundingXs==1){
        copy[i][j]="-";
    } else if (surroundingXs==3){
        copy[i][j]="X";
    } else if (surroundingXs>3){
        copy[i][j]="-";
    }
}

/*
 * This function prints out the grid colony that is passed into it out to the
 * console.
 */
void printGrid(Grid<string>& colony){
    for(int i=0;i<colony.numRows();i++){
        for(int j=0;j<colony.numCols();j++){
            cout << colony[i][j];
        }
    cout << endl;
    }
}
