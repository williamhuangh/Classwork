// EXTENSION: Extra priority queue implementation for a Map of ints (priority) to queues (values).
// This class implements the map of queues priority queue.
#include "ExtraPriorityQueue.h"

// Private member variable map from integers to a queue of strings.
Map<int, Queue<string> > pq;

// Constructor for class is empty.
ExtraPriorityQueue::ExtraPriorityQueue() {}

// Destructor for class is empty since map is automatically destructed when is out of scope.
ExtraPriorityQueue::~ExtraPriorityQueue() {}

// Iterates through the map and changes the priority of the value.
void ExtraPriorityQueue::changePriority(string value, int newPriority) {
    bool changed = false;
    for(int keys: pq){
        Queue<string> temp = pq.get(keys);
        if(checkQueueForValue(temp, keys, value, newPriority)){
            changed = true;
            break;
        }
    }
    if(!changed){
        throw "Error: The given value is not found in this priority queue";
    }
}

// Puts in a new queue without the value for a given priority, then re-enqueues it if the priority is changed.
// Returns whether or not the priority of a value was changed or not.
bool ExtraPriorityQueue::checkQueueForValue(Queue<string>& temp, int queuePriority, string value, int newPriority){
    if(iterateChangeTemporaryQueue(temp, queuePriority, value, newPriority)){
        pq.put(queuePriority, temp);
        if(pq.get(queuePriority).isEmpty()){
            pq.remove(queuePriority);
        }
        enqueue(value, newPriority);
        return true;
    }
    return false;
}

// Iterates through a particular queue in the map and if it finds the value that's is to be changed,
// alters the queue so that the value is deleted. Returns if the queue has been altered or not.
bool ExtraPriorityQueue::iterateChangeTemporaryQueue(Queue<string>& queue, int queuePriority, string value, int newPriority){
    int tempSize = queue.size();
    bool changed = false;
    for(int i =0;i<tempSize;i++){
        string currentValue= queue.dequeue();
        if(currentValue!=value || changed){
            queue.enqueue(currentValue);
        } else{
            if(queuePriority<=newPriority){
                throw "Error: new priority cannot be greater than or equal to old priority";
            } else{
                changed = true;
            }
        }
    }
    return changed;
}

// Clears the queue.
void ExtraPriorityQueue::clear() {
    pq.clear();
}

// Dequeues the first value in the first queue of the map and replaces the queue with a new queue without
// the first element.
string ExtraPriorityQueue::dequeue() {
    int index = peekPriority();
    Queue<string> temp = pq.get(index);
    string result= temp.dequeue();
    pq.put(index, temp);
    if(pq.get(index).isEmpty()){
        pq.remove(index);
    }
    return result;
}

// Enqueues the passed in value and priority.
void ExtraPriorityQueue::enqueue(string value, int priority) {
    if(pq.containsKey(priority)){
        putInExistingQueue(value, priority);
    } else{
        Queue<string> queue;
        queue.enqueue(value);
        pq.put(priority, queue);
    }
}

// Puts the passed in value and priority into an existing queue.
void ExtraPriorityQueue::putInExistingQueue(string value, int priority){
    Queue<string> newQueue;
    Queue<string> oldQueue = pq.get(priority);
    if(!insertValue(oldQueue, newQueue, value, priority)){
        newQueue.enqueue(value);
    }
    pq.put(priority, newQueue);
}

// Loops through a passed in queue and inserts the new value/priority pair into the correct spot in the map of queues.
// Returns whether or not a value has been inserted.
bool ExtraPriorityQueue::insertValue(Queue<string>& oldQueue, Queue<string>& newQueue, string value, int priority){
    bool inserted = false;
    while(!oldQueue.isEmpty()){
        string oldValue = oldQueue.dequeue();
        if (!inserted && value<oldValue){
            newQueue.enqueue(value);
            inserted = true;
        }
        newQueue.enqueue(oldValue);
    }
    return inserted;
}

// Returns whether or not the queue is empty.
bool ExtraPriorityQueue::isEmpty() const {
    return (pq.isEmpty());
}

// Returns the most urgent value.
string ExtraPriorityQueue::peek() const {
    int index = peekPriority();
    return pq.get(index).peek();
}

// Returns the most urgent priority.
int ExtraPriorityQueue::peekPriority() const {
    if(pq.isEmpty()){
        throw "Error: priority queue is empty";
    }
    for(int key: pq){
        return key;
    }
}

// Returns the size of the queue.
int ExtraPriorityQueue::size() const {
    int result = 0;
    for(int key: pq){
        result += pq.get(key).size();
    }
    return result;
}

// Loops through the elements of the queue and returns them in the correct format.
string ExtraPriorityQueue::iteratePrintQueue() const{
    string print = "";
    for(int keys: pq){
        Queue<string> currentQueue = pq.get(keys);
        while(!currentQueue.isEmpty()){
            string dequeued = currentQueue.dequeue();
            print += ", \""+dequeued+"\":"+integerToString(keys);
        }
    }
    return print;
}

// Overloads the << operator to print out elements of the queue.
ostream& operator<<(ostream& out, const ExtraPriorityQueue& queue) {
    out << "{";
    if(!queue.isEmpty()){
        string print = queue.iteratePrintQueue();
        out << print.substr(2);
    }
    out << "}" << endl;
    return out;
}
