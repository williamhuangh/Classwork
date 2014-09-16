// EXTENSION: Extra priority queue implementation for a Map of ints (priority) to queues (values).
// .h file for the MapOfQueuesPQ

#ifndef _extrapriorityqueue_h
#define _extrapriorityqueue_h

#include <iostream>
#include <string>
#include "PQEntry.h"
#include "map.h"
#include "queue.h"
#include "strlib.h"
using namespace std;

// class element definitions for the MapOfQueuesPQ
class ExtraPriorityQueue {
// public element definitions for the MapOfQueuesPQ
public:
    ExtraPriorityQueue();
    ~ExtraPriorityQueue();
    void changePriority(string value, int newPriority);
    void clear();
    string dequeue();
    void enqueue(string value, int priority);
    bool isEmpty() const;
    string peek() const;
    int peekPriority() const;
    int size() const;
    friend ostream& operator <<(ostream& out, const ExtraPriorityQueue& queue);
// private element definitions for the MapOfQueuesPQ
private:
    Map<int, Queue<string> > pq;
    bool checkQueueForValue(Queue<string>& temp, int queuePriority, string value, int newPriority);
    bool iterateChangeTemporaryQueue(Queue<string>& queue, int queuePriority, string value, int newPriority);
    void putInExistingQueue(string value, int priority);
    bool insertValue(Queue<string>& oldQueue, Queue<string>& newQueue, string value, int priority);
    string iteratePrintQueue() const;
};

#endif
