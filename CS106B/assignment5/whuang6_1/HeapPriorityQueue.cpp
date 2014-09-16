// This class implements the heap priority queue.
#include "HeapPriorityQueue.h"

// Private member variables pointer to front of array, capacity, and size of queue.
PQEntry* elements;
int capacity;
int queueSize;

// Constructor initialized the array to capacity of 10 and size of 0.
HeapPriorityQueue::HeapPriorityQueue() {
    elements = new PQEntry[10];
    capacity = 10;
    queueSize = 0;
}

// Destructor deletes all the memory spaces used by the elements array.
HeapPriorityQueue::~HeapPriorityQueue() {
    delete[] elements;
}

// Changes the priority of a given value.
void HeapPriorityQueue::changePriority(string value, int newPriority) {
    if(queueSize == 0){
        throw "Error: priority queue is empty";
    }
    if(!findAndChange(value, newPriority)){
        throw "Error: The given value is not found in this priority queue";
    }
}

// Finds the first occurance of the given value, changes its priority and bubbles all the way to the front.
bool HeapPriorityQueue::findAndChange(string value, int newPriority){
    for(int i = 1;i <=queueSize;i++){
        if(elements[i].value == value && (newPriority<elements[i].priority)){
            elements[i].priority = newPriority;
            bubbleFromBack(i);
            return true;
        } else if (elements[i].value == value){
            throw "Error: new priority cannot be greater than or equal to old priority";
        }
    }
    return false;
}

// Clears out the queue by setting size to 0.
void HeapPriorityQueue::clear() {
    queueSize = 0;
}

// Dequeues most urgent priority, moves the back element to the front and bubbles all the way to the back.
string HeapPriorityQueue::dequeue() {
    if (queueSize>0){
        string result = elements[1].value;
        elements[1] = elements[queueSize];
        queueSize--;
        bubbleFromFront(1);
        return result;
    } else {
        throw "Error: priority queue is empty";
    }
}

// Recursive equation that bubbles from the front to the back.
void HeapPriorityQueue::bubbleFromFront(int bubbleStart){
    if(!(bubbleStart*2>queueSize) && (bubbleStart*2+1)>queueSize){
        // current*2+1 is NULL swap with only current*2 if necessary, NO CALL
        if(findLessUrgentPriority(bubbleStart, bubbleStart*2)==bubbleStart){
            swapElements(bubbleStart, bubbleStart*2);
        }
    } else if (!(bubbleStart*2>queueSize)){
        // else if current*2 and current*2+1 are both not null, check with both and swap with smallest, CALL on swapped
        int index;
        if(findLessUrgentPriority(bubbleStart*2, bubbleStart*2+1)==bubbleStart*2){
            index = bubbleStart*2+1;
        } else{
            index = bubbleStart*2;
        }
        if(findLessUrgentPriority(bubbleStart, index)==bubbleStart){
            swapElements(bubbleStart, index);
            bubbleFromFront(index);
        }
    }
    // else if current*2 is NULL dont do anything NO CALL
}

// Function that takes in two indexes and returns the index of the less urgent element.
int HeapPriorityQueue::findLessUrgentPriority(int index1, int index2) const{
    if(elements[index1].priority>elements[index2].priority ||
            (elements[index1].priority==elements[index2].priority &&
             elements[index1].value>elements[index2].value)){
        return index1;
    }
    return index2;
}

// Swqps the places of two elements with indexes passed in.
void HeapPriorityQueue::swapElements(int index1, int index2){
    PQEntry temp = elements[index1];
    elements[index1]= elements[index2];
    elements[index2]=temp;
}

// Enqueues the new entry to the back and bubbles to the front if necessary.
void HeapPriorityQueue::enqueue(string value, int priority) {
    PQEntry entry (value, priority);
    if(queueSize == 0){
        elements[1] = entry;
        queueSize++;
    } else {
        if (queueSize == capacity-1){
            copyElements();
        }
        queueSize++;
        elements[queueSize] = entry;
        bubbleFromBack(queueSize);
    }
}

// Copies elements of the array into a bigger array and deletes the old one.
void HeapPriorityQueue::copyElements(){
    PQEntry* temp = new PQEntry[2*capacity];
    for (int i=1;i<=queueSize;i++){
        temp[i] = elements[i];
    }
    delete[] elements;
    elements = temp;
    capacity*=2;
}

// Recursive function that bubbles from the back to the front.
void HeapPriorityQueue::bubbleFromBack(int bubbleStart){
    if(bubbleStart/2!=0 && findLessUrgentPriority(bubbleStart, bubbleStart/2)==bubbleStart/2){
        swapElements(bubbleStart, bubbleStart/2);
        bubbleFromBack(bubbleStart/2);
    }
}

// Returns whether or not the queue is empty.
bool HeapPriorityQueue::isEmpty() const {
    return (queueSize == 0);
}

// Returns the value of the most urgent element.
string HeapPriorityQueue::peek() const {
    if (queueSize>0){
        return elements[1].value;
    } else {
        throw "Error: priority queue is empty";
    }
}

// Returns the priority of the most urgen element.
int HeapPriorityQueue::peekPriority() const {
    if (queueSize>0){
        return elements[1].priority;
    } else {
        throw "Error: priority queue is empty";
    }
}

// Returns the size of the queue.
int HeapPriorityQueue::size() const {
    return queueSize;
}

// Overloads the << operator to print out elements of the queue.
ostream& operator<<(ostream& out, const HeapPriorityQueue& queue) {
    out << "{";
    if(queue.queueSize>0){
        out << queue.elements[1];
        for (int i = 2; i <= queue.queueSize; i++){
            out << ", " << queue.elements[i];
        }
    }
    out << "}" << endl;
    return out;
}
