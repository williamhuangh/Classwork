// This program contains the algorithms for the trailblazer assignment
// EXTENSION implements bidirectional search algorithm.
#include "costs.h"
#include "trailblazer.h"
#include "queue.h"
#include "pqueue.h"
#include "map.h"
using namespace std;

// Function prototypes for DFS
bool depthFirstSearchHelper(Vertex* current, Vertex* end, Vector<Vertex*>& path);
void dfsVisitVertex(Vector<Vertex*>& path, Vertex* current);
bool dfsCheckNeighbors(Vertex* current, Vertex* end, Vector<Vertex*>& path);
// Function protoypes for BFS
void bfsCheckNeighbors(Vertex* current, Queue<Vertex*>& queue);
void retrace(Vector<Vertex*>& result, Vertex* current);
// Function prototypes for Dijkstras and A*
Vector<Vertex*> dijkstraAStarHelper(BasicGraph& graph, Vertex* start, Vertex* end, bool addHeuristics);
void dijkStarSetCosts(BasicGraph& graph, Vertex* start);
void dijkStarEnqueueStart(PriorityQueue<Vertex*>& pq, Vertex* start, Vertex* end, bool addHeuristics);
void dijkStarCheckNeighbors(PriorityQueue<Vertex*>& pq, Vertex* curr, Vertex* end, bool addHeuristics);
void dijkStarReplaceCost(PriorityQueue<Vertex*>& pq, Edge* edge, Vertex* end, double cost, bool inQueue, bool addHeuristics);
// Function prototypes for Kruskal's algorithm
void clusterVertecies(BasicGraph& graph, Map<int, Set<Vertex*> >& clusters);
void enqueueEdges(BasicGraph& graph, PriorityQueue<Edge*>& edgePQ);
int getCluster(Vertex* vertex, Map<int, Set<Vertex*> >& clusters);
void combineClusters(Map<int, Set<Vertex*> >& clusters, int firstCluster, int secondCluster, Edge* current, Set<Edge*>& result);
// EXTENSION: Function prototypes for bidirectional algorithm
void bidirectionalSetCosts(BasicGraph& graph, Vertex* start, Vertex* end);
void bidirectionalEnqueueStart(PriorityQueue<Vertex*>& pq, PriorityQueue<Vertex*>& pqEnd, Vertex* start, Vertex* end);
bool bidirectionalCheckNeighbors(PriorityQueue<Vertex*>& pqStart, Vertex* currStart, PriorityQueue<Vertex*>& pqEnd, Vertex* currEnd, Vertex* start, Vertex* end, Vector<Vertex*>& result);
void bidirectionalReplaceCost(PriorityQueue<Vertex*>& pq, Edge* edge, Vertex* end, double cost, bool inQueue, bool fromStart);
void bidirectionalRetrace(Vector<Vertex*>& result, Vertex* currentStart, Vertex* currentFinish);

// Performs a depth first search and returns a path.
Vector<Vertex*> depthFirstSearch(BasicGraph& graph, Vertex* start, Vertex* end) {
    graph.resetData();
    Vector<Vertex*> path;
    depthFirstSearchHelper(start, end, path);
    return path;
}
// Recusive function that visits a vertex and calls on its neighbors before backtracking.
bool depthFirstSearchHelper(Vertex* current, Vertex* end, Vector<Vertex*>& path){
    if(current->visited == false){
        dfsVisitVertex(path, current);
        if(current == end){
            return true;
        }
        if(dfsCheckNeighbors(current, end, path)){
            return true;
        }
        current->setColor(GRAY);
        path.remove(path.size()-1);
    }
    return false;
}
// Visits the vertex by adding it to the path, marking it, and coloring it green.
void dfsVisitVertex(Vector<Vertex*>& path, Vertex* current){
    path.add(current);
    current->visited = true;
    current->setColor(GREEN);
}
// Loops through and goes down all the neighbors. Returns true if a path is found.
bool dfsCheckNeighbors(Vertex* current, Vertex* end, Vector<Vertex*>& path){
    Set<Edge*> edges = current->edges;
    for (Edge* edge: edges){
        if(depthFirstSearchHelper(edge->finish, end, path)){
            return true;
        }
    }
    return false;
}

// Performs the breadth first search to find the shortest path.
Vector<Vertex*> breadthFirstSearch(BasicGraph& graph, Vertex* start, Vertex* end) {
    graph.resetData();
    Vector<Vertex*> result;
    Queue<Vertex*> queue;
    queue.enqueue(start);
    start->visited = true;
    start->setColor(YELLOW);

    while(!queue.isEmpty()){
        Vertex* current = queue.dequeue();
        current->setColor(GREEN);
        if(current == end){
            retrace(result, current);
            return result;
        }
        bfsCheckNeighbors(current, queue);
    }
    return result;
}
// Explores down each of the unvisited neighbors.
void bfsCheckNeighbors(Vertex* current, Queue<Vertex*>& queue){
    Set<Edge*> edges = current->edges;
    for(Edge* edge: edges){
        if(edge->finish->visited == false){
            edge->finish->visited = true;
            edge->finish->previous = edge->start;
            edge->finish->setColor(YELLOW);
            queue.enqueue(edge->finish);
        }
    }
}
// Retraces the path given going through all the previous pointers.
void retrace(Vector<Vertex*>& result, Vertex* current){
    while(current != NULL){
        result.insert(0, current);
        current = current->previous;
    }
}

// Performs Dijkstras algorithm and finds the path of least cost.
Vector<Vertex*> dijkstrasAlgorithm(BasicGraph& graph, Vertex* start, Vertex* end) {
    return dijkstraAStarHelper(graph, start, end, false);
}
// Performs the bulk of Dijkstras and A* algorithms.
Vector<Vertex*> dijkstraAStarHelper(BasicGraph& graph, Vertex* start, Vertex* end, bool addHeuristics){
    graph.resetData();
    dijkStarSetCosts(graph, start);
    Vector<Vertex*> result;
    PriorityQueue<Vertex*> pq;
    dijkStarEnqueueStart(pq, start, end, addHeuristics);

    while(!pq.isEmpty()){
        Vertex* curr = pq.dequeue();
        curr->visited = true;
        curr->setColor(GREEN);
        if(curr == end){
            retrace(result, curr);
            return result;
        }
        dijkStarCheckNeighbors(pq, curr, end, addHeuristics);
    }
    return result;
}
// Sets all beginning vertecies to a cost of infinity.
void dijkStarSetCosts(BasicGraph& graph, Vertex* start){
    Set<Vertex*> vertecies = graph.getVertexSet();
    for (Vertex* vertex: vertecies){
        vertex->cost = POSITIVE_INFINITY;
    }
    start->cost=0;
}
// Sets up the starting vertex and enqueues it.
void dijkStarEnqueueStart(PriorityQueue<Vertex*>& pq, Vertex* start, Vertex* end, bool addHeuristics){
    double beginningPriority = 0;
    if (addHeuristics) beginningPriority = heuristicFunction(start, end);
    pq.enqueue(start, beginningPriority);
    start->setColor(YELLOW);
}
// Checks the unvisited neighbors of the current vertex.
void dijkStarCheckNeighbors(PriorityQueue<Vertex*>& pq, Vertex* curr, Vertex* end, bool addHeuristics){
    Set<Edge*> edges = curr->edges;
    for (Edge* edge: edges){
        if (edge->finish->visited == false){
            bool inQueue = (!(edge->finish->cost == POSITIVE_INFINITY));
            double cost = curr->cost + edge->cost;
            if (cost < edge->finish->cost){ // replaces the cost if the new cost is lower.
                dijkStarReplaceCost(pq, edge, end, cost, inQueue, addHeuristics);
            }
        }
    }
}
// Replaces the cost of a given vertex in the graph and in the pq.
void dijkStarReplaceCost(PriorityQueue<Vertex*>& pq, Edge* edge, Vertex* end, double cost, bool inQueue, bool addHeuristics){
    edge->finish->cost = cost;
    edge->finish->previous = edge->start;
    if(addHeuristics) cost += heuristicFunction(edge->finish, end);
    if(inQueue){
        pq.changePriority(edge->finish, cost);
    } else{
        edge->finish->setColor(YELLOW);
        pq.enqueue(edge->finish, cost);
    }
}
// Performs the A* algorithm, finds cheapest path with a heuristic.
Vector<Vertex*> aStar(BasicGraph& graph, Vertex* start, Vertex* end) {
    return dijkstraAStarHelper(graph, start, end, true);
}

// Kruskal's algorithm, returns a minimum spanning tree as a set of edges.
Set<Edge*> kruskal(BasicGraph& graph) {
    graph.resetData();
    Set<Edge*> result;
    PriorityQueue<Edge*> edgePQ;
    Map<int, Set<Vertex*> > clusters;

    clusterVertecies(graph, clusters);
    enqueueEdges(graph, edgePQ);
    while(clusters.size()>1){ // When the entire graph is not in 1 cluster
        Edge* current = edgePQ.dequeue();
        int firstCluster = getCluster(current->start, clusters);
        int secondCluster = getCluster(current->finish, clusters);
        if(!(firstCluster == secondCluster)){ // If the two current vertecies are not in the same cluster.
            combineClusters(clusters, firstCluster, secondCluster, current, result);
        }
    }
    return result;
}
// Sets up the vertecies in the beginning as each belonging to a different cluster.
void clusterVertecies(BasicGraph& graph, Map<int, Set<Vertex*> >& clusters){
    Set<Vertex*> vertecies = graph.getVertexSet();
    int startingCluster = 1;
    for(Vertex* currentVertex: vertecies){
        Set<Vertex*> set;
        set.add(currentVertex);
        clusters.put(startingCluster, set);
        startingCluster++;
    }
}
// Enqueues all the edges in the beginning in the priority queue.
void enqueueEdges(BasicGraph& graph, PriorityQueue<Edge*>& edgePQ){
    Set<Edge*> edges = graph.getEdgeSet();
    for(Edge* edge: edges){
        edgePQ.enqueue(edge, edge->cost);
    }
}
// Returns the cluster key that the given vertex is in.
int getCluster(Vertex* vertex, Map<int, Set<Vertex*> >& clusters){
    for(int key: clusters){
        if(clusters.get(key).contains(vertex)){
            return key;
        }
    }
    return -1;
}
// Combines the two clusters (keys are firstCluster and secondCluster) and adds the edge to the result set.
void combineClusters(Map<int, Set<Vertex*> >& clusters, int firstCluster, int secondCluster, Edge* current, Set<Edge*>& result){
    Set<Vertex*> currentSet = clusters.get(firstCluster);
    currentSet += clusters.get(secondCluster);
    clusters.put(firstCluster, currentSet);
    clusters.remove(secondCluster);
    result.add(current);
}

// EXTENSION: Starts the bidirectional search. Uses the LIGHTRED color defined by types.h, trailblazer.gui, and types.cpp.
Vector<Vertex*> bidirectionalSearch(BasicGraph& graph, Vertex* start, Vertex* end){
    graph.resetData();
    bidirectionalSetCosts(graph, start, end);
    Vector<Vertex*> result;
    PriorityQueue<Vertex*> pqStart;
    PriorityQueue<Vertex*> pqEnd;
    bidirectionalEnqueueStart(pqStart, pqEnd, start, end);

    while(!pqStart.isEmpty() && !pqEnd.isEmpty()){
        Vertex* currStart = pqStart.dequeue();
        currStart->visited = true;
        currStart->setColor(GREEN);

        Vertex* currEnd = pqEnd.dequeue();
        currEnd->visited = true;
        currEnd->setColor(LIGHTRED);

        if(bidirectionalCheckNeighbors(pqStart, currStart, pqEnd, currEnd, start, end, result)){
            return result;
        }
    }
    return result; // No path
}
// EXTENSION: Sets all beginning vertecies to a cost of infinity for bidirectional algorithm.
void bidirectionalSetCosts(BasicGraph& graph, Vertex* start, Vertex* end){
    Set<Vertex*> vertecies = graph.getVertexSet();
    for (Vertex* vertex: vertecies){
        vertex->cost = POSITIVE_INFINITY;
    }
    start->cost=0;
    end->cost=0;
}
// EXTENSION: Sets up the starting vertecies (start and end) and enqueues them.
void bidirectionalEnqueueStart(PriorityQueue<Vertex*>& pqStart, PriorityQueue<Vertex*>& pqEnd, Vertex* start, Vertex* end){
    double beginningPriority = heuristicFunction(start, end);
    pqStart.enqueue(start, beginningPriority);
    double endPriority = heuristicFunction(end, start);
    pqEnd.enqueue(end, endPriority);
    start->setColor(YELLOW);
    end->setColor(GRAY);
}
// EXTENSION: Checks the unvisited neighbors of the current vertecies (from start and from end).
bool bidirectionalCheckNeighbors(PriorityQueue<Vertex*>& pqStart, Vertex* currStart, PriorityQueue<Vertex*>& pqEnd, Vertex* currEnd, Vertex* start, Vertex* end, Vector<Vertex*>& result){
    Set<Edge*> edges = currStart->edges;
    for (Edge* edge: edges){
        if (edge->finish->visited == false){
            bool inQueue = (!(edge->finish->cost == POSITIVE_INFINITY) && edge->finish->getColor()!=LIGHTRED && edge->finish->getColor()!=GRAY);
            double cost = currStart->cost + edge->cost;
            if (cost < edge->finish->cost){ // replaces the cost if the new cost is lower.
                bidirectionalReplaceCost(pqStart, edge, end, cost, inQueue, true);
            }
        } else if (edge->finish->getColor()==LIGHTRED || edge->finish->getColor()==GRAY){ // If the vertex is already visited by the other end, retrace and stop.
            bidirectionalRetrace(result, edge->start, edge->finish);
            return true;
        }
    }
    edges = currEnd->edges;
    for (Edge* edge: edges){
        if (edge->finish->visited == false){
            bool inQueue = (!(edge->finish->cost == POSITIVE_INFINITY) && edge->finish->getColor()!=YELLOW && edge->finish->getColor()!=GREEN);
            double cost = currEnd->cost + edge->cost;
            if (cost < edge->finish->cost){ // replaces the cost if the new cost is lower.
                bidirectionalReplaceCost(pqEnd, edge, start, cost, inQueue, false);
            }
        } else if (edge->finish->getColor()==YELLOW || edge->finish->getColor()==GREEN){ // If the vertex is already visited by the other end, retrace and stop.
            bidirectionalRetrace(result, edge->start, edge->finish);
            return true;
        }
    }
    return false;
}
// EXTENSION: Replaces the cost of a given vertex in the graph and in its pq.
void bidirectionalReplaceCost(PriorityQueue<Vertex*>& pq, Edge* edge, Vertex* end, double cost, bool inQueue, bool fromStart){
    edge->finish->cost = cost;
    edge->finish->previous = edge->start;
    cost += heuristicFunction(edge->finish, end);
    if(inQueue){
        pq.changePriority(edge->finish, cost);
    } else{
        if(fromStart){
            edge->finish->setColor(YELLOW);
        } else{
            edge->finish->setColor(GRAY);
        }
        pq.enqueue(edge->finish, cost);
    }
}
// EXTENSION: Takes in two vertecies and retraces both to their respective starting vertex.
void bidirectionalRetrace(Vector<Vertex*>& result, Vertex* currentStart, Vertex* currentFinish){
    while(currentStart != NULL){
        result.insert(0, currentStart);
        currentStart = currentStart->previous;
    }
    while(currentFinish != NULL){
        result.add(currentFinish);
        currentFinish = currentFinish->previous;
    }
}
