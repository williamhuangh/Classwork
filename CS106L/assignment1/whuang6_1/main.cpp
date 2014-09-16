// This program runs GraphViz
#include <iostream>
#include <sstream>
#include <fstream>
#include <cmath>
#include "SimpleGraph.h"
using namespace std;

// Constants
const double kPi = 3.14159265358979323;
const double kConst = 1/1000.0;

// Function prototypes
void Welcome();
void openFile(ifstream& stream);
int getInteger(string prompt);
void addNodes(SimpleGraph& graph, int vertecies, vector<Node>& nodes);
void addEdges(SimpleGraph& graph, ifstream& stream, vector<Edge>& edges);
void iterateGraph(SimpleGraph& graph);
void addDeltas(vector<double>& deltaX, vector<double>& deltaY, SimpleGraph& graph);

// Main function starts the program
int main() {
    SimpleGraph graph;
    Welcome();
    ifstream stream;
    openFile(stream);

    stringstream converter;
    string temp;
    getline(stream, temp);
    converter << temp;
    int vertecies;
    converter >> vertecies;

    vector<Node> nodes;
    vector<Edge> edges;
    addNodes(graph, vertecies, nodes);
    addEdges(graph, stream, edges);
    int runTime;
    while(runTime<=0){ // runTime must be a positive integer
        runTime = getInteger("How many seconds to run for?: ");
    }
    time_t startTime = time(NULL);
    InitGraphVisualizer(graph);
    DrawGraph(graph);
    while(true){
        iterateGraph(graph);
        if(difftime(time(NULL), startTime)>runTime) break; // breaks when time is up
    }
    cout << "Have a nice day!" << endl;
    return 0;
}

/* Prints a message to the console welcoming the user and
 * describing the program. */
void Welcome() {
    cout << "Welcome to CS106L GraphViz!" << endl;
    cout << "This program uses a force-directed graph layout algorithm" << endl;
    cout << "to render sleek, snazzy pictures of various graphs." << endl;
    cout << endl;
}

// Prompts the user for a file until a valid file is inputted. Opened into the ifstream.
void openFile(ifstream& stream){
    while(true){
        cout << endl;
        cout << "Please input a file to read: ";
        string file;
        stream.clear();
        getline(cin, file);
        stream.open(file.c_str());
        if(stream.fail()==false) break;
    }
}

// Prompts the user for an integer. This is the implementation we went over in class.
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

// Adds the starting positions of the nodes into the graph.
void addNodes(SimpleGraph& graph, int vertecies, vector<Node>& nodes){
    for(int i = 0; i<vertecies; i++){
        Node newNode;
        newNode.x = cos(2*kPi*(i)/vertecies);
        newNode.y = sin(2*kPi*(i)/vertecies);
        nodes.push_back(newNode);
    }
    graph.nodes = nodes;
}

// Adds the file's edges into the graph.
void addEdges(SimpleGraph& graph, ifstream& stream, vector<Edge>& edges){
    while(true){
        string temp;
        stringstream converter;
        getline(stream, temp);
        if(stream.fail()) break;
        converter << temp;
        int vertex1;
        converter >> vertex1;
        int vertex2;
        converter >> vertex2;
        Edge newEdge;
        newEdge.start = vertex1;
        newEdge.end = vertex2;
        edges.push_back(newEdge);
    }
    graph.edges = edges;
}

// Iterates through the vertecies of the graph, computes and adds forces for one round for each of them.
void iterateGraph(SimpleGraph& graph){
    vector<double> deltaX(graph.nodes.size());
    vector<double> deltaY(graph.nodes.size());
    for(int i = 0;i<graph.nodes.size();i++){
        for(int j = i+1;j<graph.nodes.size();j++){
            double fRepel = kConst/sqrt(pow(graph.nodes.at(i).y-graph.nodes.at(j).y,2)
                                         +pow(graph.nodes.at(i).x-graph.nodes.at(j).x,2));
            double theta = atan2((graph.nodes.at(i).y-graph.nodes.at(j).y),(graph.nodes.at(i).x-graph.nodes.at(j).x));
            deltaX[j]-=fRepel*cos(theta);
            deltaY[j]-=fRepel*sin(theta);
            deltaX[i]+=fRepel*cos(theta);
            deltaY[i]+=fRepel*sin(theta);
        }
    }
    for(Edge edge: graph.edges){
        int i = edge.end;
        int j = edge.start;
        double fAttract = kConst*(pow(graph.nodes.at(i).y-graph.nodes.at(j).y,2)
                                  +pow(graph.nodes.at(i).x-graph.nodes.at(j).x,2));
        double theta = atan2((graph.nodes.at(i).y-graph.nodes.at(j).y),(graph.nodes.at(i).x-graph.nodes.at(j).x));
        deltaX[j]+=fAttract*cos(theta);
        deltaY[j]+=fAttract*sin(theta);
        deltaX[i]-=fAttract*cos(theta);
        deltaY[i]-=fAttract*sin(theta);
    }
    addDeltas(deltaX, deltaY, graph);
}

// Adds the previously computed values to the graph nodes and redraws the graph with the new values.
void addDeltas(vector<double>& deltaX, vector<double>& deltaY, SimpleGraph& graph){
    vector<Node> nodes = graph.nodes;
    for(int i = 0;i<graph.nodes.size();i++){
        Node currentNode = nodes.at(i);
        currentNode.x = currentNode.x+deltaX[i];
        currentNode.y = currentNode.y+deltaY[i];
        nodes.at(i) = currentNode;
    }
    graph.nodes = nodes;
    DrawGraph(graph);
}
