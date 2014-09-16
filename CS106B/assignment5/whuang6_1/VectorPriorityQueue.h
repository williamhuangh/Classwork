// .h file for the VectorPriorityQueue

#ifndef _vectorpriorityqueue_h
#define _vectorpriorityqueue_h

#include <iostream>
#include <string>
#include "vector.h"
#include "PQEntry.h"
using namespace std;

// class element definitions for the VectorPriorityQueue
class VectorPriorityQueue {
// public elements for VectorPriorityQueue
public:
    VectorPriorityQueue();
    ~VectorPriorityQueue();
    void changePriority(string value, int newPriority);
    void clear();
    string dequeue();
    void enqueue(string value, int priority);
    bool isEmpty() const;
    string peek() const;
    int peekPriority() const;
    int size() const;
    friend ostream& operator <<(ostream& out, const VectorPriorityQueue& queue);
// private elements for VectorPriorityQueue
private:
    Vector<PQEntry> vectorQueue;
    bool findAndChange(string value, int newPriority);
    int findTopElement() const;
    int findUrgentPriority() const;
    void checkNextIndex(int index, int& topElementIndex, string& currentWord, int& urgent) const;
};

#endif
