// This class implements the vector priority queue.

#include "LinkedPriorityQueue.h"

// Private member variable pointer to front of LinkedList.
ListNode* front;

// Constructor for class sets front to NULL.
LinkedPriorityQueue::LinkedPriorityQueue() {
    front = NULL;
}

// Destructor for class calls clear to delete the memory for the LinkedList.
LinkedPriorityQueue::~LinkedPriorityQueue() {
    clear();
}

// Changes the priority of a given value. Cases are: if the list is empty, if the element is first, or if
// the element is somewhere after the first in the queue.
void LinkedPriorityQueue::changePriority(string value, int newPriority) {
    ListNode* current = front;
    bool changed = false;
    if (current == NULL){
        throw "Error: priority queue is empty";
    } else if (current->value == value && current->priority>newPriority){
        changeFirstElement(current, value, newPriority);
        changed = true;
    } else {
        changed = changeMiddleElement(current, value, newPriority);
    }
    if(!changed){
        throw "Error: The given value is not found in this priority queue";
    }
}

// Gets called if the first element priority is changed. Delete and enqueues another node with the new priority.
void LinkedPriorityQueue::changeFirstElement (ListNode* current, string value, int newPriority){
    front = front->next;
    delete current;
    enqueue(value, newPriority);
}

// Gets called if the element called to change is not the first one. Deletes and adds a new node if
// the value is found and the newPriority is more urgent. Returns if the value was changed or not.
bool LinkedPriorityQueue::changeMiddleElement (ListNode* current, string value, int newPriority){
    while (current->next != NULL){
        if(current->next->value == value && current->next->priority>newPriority){
            deleteAndAddNode(current, value, newPriority);
            return true;
        } else if (current->next->value == value){
            throw "Error: new priority cannot be greater than or equal to old priority";
        }
        current = current->next;
    }
    return false;
}

// Deletes the specified node and requeues it with the new priority.
void LinkedPriorityQueue::deleteAndAddNode(ListNode* current, string value, int newPriority){
    ListNode* trash = current->next;
    current->next=current->next->next;
    delete trash;
    enqueue(value, newPriority);
}

// Clears the queue by deleting all the nodes.
void LinkedPriorityQueue::clear() {
    ListNode* current = front;
    while(current != NULL){
        ListNode* trash = current;
        current = current->next;
        delete trash;
    }
}

// Deletes the first node and returns the value of it.
string LinkedPriorityQueue::dequeue() {
    ListNode* trash = front;
    if(trash == NULL){
        throw "Error: priority queue is empty";
    }
    front = front->next;
    string firstPriority = trash->value;
    delete trash;
    return firstPriority;
}

// Enqueues another node. Cases: if the queue is empty, if it is more urgent than the first node, or if it is to
// be added somewhere after the first.
void LinkedPriorityQueue::enqueue(string value, int priority) {
    ListNode* current = front;
    if (current == NULL){
        front = new ListNode(value, priority, NULL);
    } else if (current->priority>priority || (current->priority==priority && current->value>value)){
        front = new ListNode(value, priority, front);
    } else {
        enqueueInMiddle(current, value, priority);
    }
}

// Checks the LinkedList and adds the new node in the appropriate priority order.
void LinkedPriorityQueue::enqueueInMiddle(ListNode* current, string value, int priority){
    while(current->next != NULL && (current->next->priority<priority || (current->next->priority==priority &&
                                                                        current->next->value<value))){
        current = current->next;
    }
    if(current->next == NULL){
        current->next = new ListNode(value, priority, NULL);
    } else{
        current->next = new ListNode(value, priority, current->next);
    }
}

// Returns whether or not the queue is empty or not.
bool LinkedPriorityQueue::isEmpty() const {
    return (front == NULL);
}

// Returns the value of the first node.
string LinkedPriorityQueue::peek() const {
    if (front == NULL){
        throw "Error: priority queue is empty";
    }
    return front->value;
}

// Returns the priority number of the first node.
int LinkedPriorityQueue::peekPriority() const {
    if (front == NULL){
        throw "Error: priority queue is empty";
    }
    return front->priority;
}

// Traverses the queue and returns the size of the queue.
int LinkedPriorityQueue::size() const {
    int result = 0;
    ListNode* current = front;
    while(current != NULL){
        current = current->next;
        result++;
    }
    return result;
}

// Overloads the << operator to print out elements of the queue.
ostream& operator<<(ostream& out, const LinkedPriorityQueue& queue) {
    ListNode* current = queue.front;
    out << "{";
    if(current != NULL){
        out << *current;
        while(current->next != NULL){
            current = current->next;
            out << ", "<< *current;
        }
    }
    out << "}"<< endl;
    return out;
}
