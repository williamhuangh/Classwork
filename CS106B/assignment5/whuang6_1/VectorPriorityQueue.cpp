// This class implements the vector priority queue.

#include "VectorPriorityQueue.h"

// Private member variable vector of PQEntries.
Vector<PQEntry> vectorQueue;

// Constructor for class is empty.
VectorPriorityQueue::VectorPriorityQueue() {}

// Destructor for class is empty since vector is automatically destructed when is out of scope.
VectorPriorityQueue::~VectorPriorityQueue() {}

// Changes the priority of a given value.
void VectorPriorityQueue::changePriority(string value, int newPriority) {
    if(!findAndChange(value, newPriority)){
        throw "Error: The given value is not found in this priority queue";
    }
}

// Iterates through the array, finds and changes the first found value. Returns if the value is found or not.
bool VectorPriorityQueue::findAndChange(string value, int newPriority){
    for (int i=0; i<size();i++){
        if(vectorQueue[i].value == value){
            if (vectorQueue[i].priority<=newPriority){
                throw "Error: new priority cannot be greater than or equal to old priority";
            }
            vectorQueue[i].priority=newPriority;
            return true;
        }
    }
    return false;
}

// Clears the vector.
void VectorPriorityQueue::clear() {
    vectorQueue.clear();
}

// Deletes and returns the most urgent value by looping through the array.
string VectorPriorityQueue::dequeue() {
    int index = findTopElement();
    string result = vectorQueue[index].value;
    vectorQueue.remove(index);
    return result;
}

// Adds a new PQEntry to the end of the vector.
void VectorPriorityQueue::enqueue(string value, int priority) {
    PQEntry entry (value, priority);
    vectorQueue.add(entry);
}

// Returns whether or not the queue is empty.
bool VectorPriorityQueue::isEmpty() const {
    return vectorQueue.isEmpty();
}

// Returns the most urgent value by looping through the vector and finding most urgent priority.
string VectorPriorityQueue::peek() const {
    int index = findTopElement();
    return vectorQueue[index].value;
}

// Finds and returns the index of the most urgent value.
int VectorPriorityQueue::findTopElement() const{
    if(isEmpty()){
        throw "Error: priority queue is empty";
    }
    int topElementIndex = 0;
    string currentWord = vectorQueue[0].value;
    int urgent = vectorQueue[0].priority;
    for(int i=1;i<size();i++){
        checkNextIndex(i, topElementIndex, currentWord, urgent);
    }
    return topElementIndex;
}

// Checks and replaces the topElement information with the current index's information if it is more urgent.
void VectorPriorityQueue::checkNextIndex(int index, int& topElementIndex, string& currentWord, int& urgent) const{
    if(vectorQueue[index].priority<urgent){
        urgent = vectorQueue[index].priority;
        topElementIndex = index;
        currentWord = vectorQueue[index].value;
    } else if (vectorQueue[index].priority==urgent && vectorQueue[index].value<currentWord){
        topElementIndex = index;
        currentWord = vectorQueue[index].value;
    }
}

// Returns the most urgent priority number.
int VectorPriorityQueue::peekPriority() const {
    if(!isEmpty()){
        return findUrgentPriority();
    } else{
        throw "Error: priority queue is empty";
    }
}

// Loops through the queue and finds the number for the most urgent priority.
int VectorPriorityQueue::findUrgentPriority() const{
    int urgent = vectorQueue[0].priority;
    for(int i=1;i<size();i++){
        if(vectorQueue[i].priority<urgent){
            urgent = vectorQueue[i].priority;
        }
    }
    return urgent;
}

// Returns the number of elements in the queue.
int VectorPriorityQueue::size() const {
    return vectorQueue.size();
}

// Overrides the << operator to print out the queue in string format.
ostream& operator<<(ostream& out, const VectorPriorityQueue& queue) {
    out << "{";
    if(!queue.isEmpty()){
        out << queue.vectorQueue[0];
        for (int i = 1;i<queue.size();i++){
            out << ", " << queue.vectorQueue[i];
        }
    }
    out << "}"<< endl;
    return out;
}
