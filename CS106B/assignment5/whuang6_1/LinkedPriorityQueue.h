// .h file for the LinkedPriorityQueue

#ifndef _linkedpriorityqueue_h
#define _linkedpriorityqueue_h

#include <cstddef>    // for NULL
#include <iostream>
#include <string>
#include "ListNode.h"
#include "PQEntry.h"
using namespace std;

// class element definitions for the LinkedPriorityQueue
class LinkedPriorityQueue {
// public elements for LinkedPriorityQueue
public:
    LinkedPriorityQueue();
    ~LinkedPriorityQueue();
    void changePriority(string value, int newPriority);
    void clear();
    string dequeue();
    void enqueue(string value, int priority);
    bool isEmpty() const;
    string peek() const;
    int peekPriority() const;
    int size() const;
    friend ostream& operator <<(ostream& out, const LinkedPriorityQueue& queue);
// private elements for LinkedPriorityQueue
private:
    ListNode* front;
    void changeFirstElement(ListNode* current, string value, int newPriority);
    bool changeMiddleElement (ListNode* current, string value, int newPriority);
    void deleteAndAddNode(ListNode* current, string value, int newPriority);
    void enqueueInMiddle(ListNode* current, string value, int priority);
};

#endif
