// .h file for the HeapPriorityQueue

#ifndef _heappriorityqueue_h
#define _heappriorityqueue_h

#include <iostream>
#include <string>
#include "PQEntry.h"
using namespace std;

// class element definitions for the HeapPriorityQueue
class HeapPriorityQueue {
// public elements for HeapPriorityQueue
public:
    HeapPriorityQueue();
    ~HeapPriorityQueue();
    void changePriority(string value, int newPriority);
    void clear();
    string dequeue();
    void enqueue(string value, int priority);
    bool isEmpty() const;
    string peek() const;
    int peekPriority() const;
    int size() const;
    friend ostream& operator <<(ostream& out, const HeapPriorityQueue& queue);
// private elements for HeapPriorityQueue
private:
    PQEntry* elements;
    int capacity;
    int queueSize;
    bool findAndChange(string value, int newPriority);
    void bubbleFromFront(int bubbleStart);
    int findLessUrgentPriority(int index1, int index2) const;
    void swapElements(int index1, int index2);
    void copyElements();
    void bubbleFromBack(int bubbleStart);
};

#endif
