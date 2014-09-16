/*
 * This program contains the code to run the recursion problems.
 */
#include <cmath>
#include <iostream>
#include <iomanip>
#include <sstream>
#include <string>
#include "gwindow.h"
#include "hashmap.h"
#include "map.h"
#include "random.h"
#include "simpio.h"
#include "strlib.h"
#include "vector.h"
#include "recursionproblems.h"
using namespace std;

/*
 * Function prototypes.
 */
int convertStringToIntegerHelper(string exp, int zeros=0, int result=0);
int findSubstring(string exp);
void drawSierpinskiTriangleHelper(GWindow& gw, int x, int y, int size, int order, int currentOrder);
void drawOutlineTriangle(GWindow& gw, int x, int y, int size);
void drawInnerTriangle(GWindow& gw, int x, int y, int size);
void callNextOrders(GWindow& gw, int x, int y, int size, int order, int currentOrder);
void checkPixel(int x, int y, int width, int height, int color, int currentColor);
HashMap<string,Vector<string> > storeInputToMap(istream& input);
void breakUpLine(HashMap <string, Vector<string> >& map, string fileString);
void recursiveGrammerGenerate(string symbol, HashMap <string, Vector<string> >& map, string& currentLine);
void linkNextSymbols(string symbol, HashMap <string, Vector<string> >& map, string& currentLine);
string extractAndCallNextSymbols(HashMap <string, Vector<string> >& map, string& currentLine, string nextSymbols);

int countKarelPaths(int street, int avenue) {
    // TODO: write this function
    return 0;
}

/*
 * This function takes in a string and returns the corresponding integer.
 */
int convertStringToInteger(string exp) {
    return convertStringToIntegerHelper(exp);
}

/*
 * This function recursively solves for the integer for the string that is passed in.
 */
int convertStringToIntegerHelper(string exp, int zeros, int result) {
    if(exp.empty()){ // Base case
        return result;
    } else if(exp[exp.size()-1]=='-'){
        return -result;
    } else{ // Recursive case
        int nextNumber=exp[exp.size()-1]-'0';
        exp=exp.substr(0,exp.size()-1);
        return convertStringToIntegerHelper(exp, zeros+1, result+pow(10.0,zeros)*nextNumber);
    }
}

/*
 * This function recursively finds if a string of brackets is balanced or not.
 */
bool isBalanced(string exp) {
    if(exp.empty()){ // Base case
        return true;
    } else{
        int subStringStart=findSubstring(exp);
        if (subStringStart==-1){
            return false;
        } else{ // Recursive case
            return isBalanced(exp.substr(0,subStringStart)+exp.substr(subStringStart+2));
        }
    }
}

/*
 * This function finds if there are {}, [], or () as a substring inside of the string.
 * If not, returns -1.
 */
int findSubstring(string exp){
    if(exp.find("{}")!=string::npos){
        return exp.find("{}");
    } else if(exp.find("[]")!=string::npos){
        return exp.find("[]");
    } else if(exp.find("()")!=string::npos){
        return exp.find("()");
    }
    return -1;
}

/*
 * This function draws the Sierpinski triangle for a particular order.
 */
void drawSierpinskiTriangle(GWindow& gw, int x, int y, int size, int order) {
    if(order>0){
        drawSierpinskiTriangleHelper(gw, x, y, size, order, 1);
    }
}

/*
 * This recursive function draws the Sierpinski triangle.
 */
void drawSierpinskiTriangleHelper(GWindow& gw, int x, int y, int size, int order, int currentOrder){
    if(currentOrder==1){
        drawOutlineTriangle(gw, x, y, size);
    }
    if(order!=currentOrder){ // Recursive case
        drawInnerTriangle(gw, x, y, size);
        callNextOrders(gw, x, y, size, order, currentOrder);
    }
}

/*
 * This function draws the first order outline of the triangle.
 */
void drawOutlineTriangle(GWindow& gw, int x, int y, int size){
    gw.drawLine(x, y, x+size, y);
    gw.drawLine(x, y, x+size/2.0, y+size/2.0*sqrt(3));
    gw.drawLine(x+size/2.0, y+size/2.0*sqrt(3), x+size, y);
}

/*
 * This function draws the current triangle on the window.
 */
void drawInnerTriangle(GWindow& gw, int x, int y, int size){
    gw.drawLine(x+size/2.0, y, x+size/4.0, y+size/4.0*sqrt(3));
    gw.drawLine(x+size/4.0, y+size/4.0*sqrt(3), x+3*size/4.0, y+size/4.0*sqrt(3));
    gw.drawLine(x+3*size/4.0, y+size/4.0*sqrt(3), x+size/2.0, y);
}

/*
 * This function calls the next 3 recursive triangles.
 */
void callNextOrders(GWindow& gw, int x, int y, int size, int order, int currentOrder){
    drawSierpinskiTriangleHelper(gw, x, y, size/2, order, currentOrder+1);
    drawSierpinskiTriangleHelper(gw, x+size/2.0, y, size/2, order, currentOrder+1);
    drawSierpinskiTriangleHelper(gw, x+size/4.0, y+size/4.0*sqrt(3), size/2, order, currentOrder+1);
}

/*
 * This function recursively fills in the new color for a specific color-area.
 */
void floodFill(int x, int y, int width, int height, int color) {
    int currentColor=getPixelColor(x, y);
    setPixelColor(x, y, color);
    if(x>0){ // Recursive if statements, base case doesnt call 1 or more of these.
        checkPixel(x-1, y, width, height, color, currentColor);
    }
    if(x<width-1){
        checkPixel(x+1, y, width, height, color, currentColor);
    }
    if(y>0){
        checkPixel(x, y-1, width, height, color, currentColor);
    }
    if(y<height-1){
        checkPixel(x, y+1, width, height, color, currentColor);
    }
}

/*
 * This function calls floodFill again if the current pixel is of the same color as the first clicked
 * pixel.
 */
void checkPixel(int x, int y, int width, int height, int color, int currentColor){
    if(getPixelColor(x, y)==currentColor){
        floodFill(x, y, width, height, color);
    }
}

/*
 * This function generates grammar for a particular file.
 */
Vector<string> grammarGenerate(istream& input, string symbol, int times) {
    Vector<string> result;
    HashMap<string,Vector<string> > map=storeInputToMap(input);

    if(symbol.empty()){ // Never reaches exception since recursionmain checks for it.
        throw "Symbol parameter is empty.";
    }
    if(times>0){
        for(int i=0;i<times;i++){
            string currentLine="";
            recursiveGrammerGenerate(symbol, map, currentLine);
            currentLine=trim(currentLine);
            result.add(currentLine);
        }
    }
    return result;
}

/*
 * This function stores all the input lines into a map.
 */
HashMap<string,Vector<string> > storeInputToMap(istream& input){
    HashMap <string, Vector<string> > map;
    string fileString;
    while(true){
        getline(input, fileString);
        if(input.fail()){
            break;
        }
        breakUpLine(map, fileString);
    }
    return map;
}

/*
 * This function breaks up the individual lines and puts the non-terminals as the key, mapped to
 * their terminals.
 */
void breakUpLine(HashMap <string, Vector<string> >& map, string fileString){
    int index=fileString.find("::=");
    string nonTerminals=fileString.substr(0, index);
    fileString=fileString.substr(index+3);
    Vector<string> rules= stringSplit(fileString, "|");
    if(!map.containsKey(nonTerminals)){
        map.put(nonTerminals, rules);
    } else{
        throw "Two lines cannot contain the same non-terminal.";
    }
}

/*
 * This method either adds the terminal to the string or recursively calls the next non-terminals.
 */
void recursiveGrammerGenerate(string symbol, HashMap <string, Vector<string> >& map, string& currentLine){
    if(!map.containsKey(symbol)){
        currentLine=currentLine+" "+symbol;
    } else{ // Recursive case
        linkNextSymbols(symbol, map, currentLine);
    }
}

/*
 * This method chooses a random rule and calls extractAndCallNextSymbols.
 */
void linkNextSymbols(string symbol, HashMap <string, Vector<string> >& map, string& currentLine){
    int ruleNumber=randomInteger(0,map.get(symbol).size()-1);
    string nextSymbols=map.get(symbol).get(ruleNumber);
    while(!nextSymbols.empty()){
        nextSymbols=extractAndCallNextSymbols(map, currentLine, nextSymbols);
    }
}

/*
 * This method calls the recursiveGrammerGenerate for the next element in the rule.
 */
string extractAndCallNextSymbols(HashMap <string, Vector<string> >& map, string& currentLine, string nextSymbols){
    int index=nextSymbols.length();
    if(nextSymbols.find(" ")!=string::npos){
        index=nextSymbols.find(" ");
    }
    string nextCall=nextSymbols.substr(0,index);
    recursiveGrammerGenerate(nextCall, map, currentLine);
    if(nextSymbols.length()>index){
        nextSymbols=nextSymbols.substr(index+1);
    } else{
        nextSymbols="";
    }
    return nextSymbols;
}
