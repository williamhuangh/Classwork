// This program contains the methods for the huffman encoding assignment.
#include "encoding.h"
#include "pqueue.h"
#include "filelib.h"

// Function prototypes.
PriorityQueue<HuffmanNode*> putNodesIntoQueue(const Map<int, int>& freqTable);
void buildEncodingMapHelper(HuffmanNode* tree, Map<int, string>& map, string chosen);
void addToOutput(string value, obitstream& output);
int decodeDataHelper(ibitstream& input, HuffmanNode* current);

// Builds and returns a map from the characters in the file to their frequencies.
Map<int, int> buildFrequencyTable(istream& input) {
    Map<int, int> freqTable;
    int currentCharacter = input.get();
    while(currentCharacter != -1){
        freqTable[currentCharacter]++;
        currentCharacter = input.get();
    }
    freqTable.put(PSEUDO_EOF, 1);
    return freqTable;
}

// Builds the binary tree based on the passed in frequency table.
HuffmanNode* buildEncodingTree(const Map<int, int>& freqTable) {
    PriorityQueue<HuffmanNode*> pq = putNodesIntoQueue(freqTable);
    while(!pq.isEmpty()){
        HuffmanNode* first = pq.dequeue();
        if(pq.isEmpty()){
            return first;
        }
        HuffmanNode* second = pq.dequeue();
        HuffmanNode* joinFirstSecond = new HuffmanNode (NOT_A_CHAR, second->count+first->count, first, second);
        pq.enqueue(joinFirstSecond, joinFirstSecond->count);
    }
    // If the map/pq is empty
    HuffmanNode* pointToNothing = NULL;
    return pointToNothing;
}

// Puts the original map's key/values into the pq as HuffmanNodes.
PriorityQueue<HuffmanNode*> putNodesIntoQueue(const Map<int, int>& freqTable){
    PriorityQueue<HuffmanNode*> pq;
    for(int character: freqTable){
        HuffmanNode* current = new HuffmanNode(character, freqTable[character], NULL, NULL);
        pq.enqueue(current, freqTable[character]);
    }
    return pq;
}

// Builds and returns a map from byte characters to their binary representation.
Map<int, string> buildEncodingMap(HuffmanNode* encodingTree) {
    Map<int, string> encodingMap;
    if(encodingTree != NULL && !(encodingTree->isLeaf())){
        buildEncodingMapHelper(encodingTree->zero, encodingMap, "0");
        buildEncodingMapHelper(encodingTree->one, encodingMap, "1");
    } else if (encodingTree != NULL){
        encodingMap.put(PSEUDO_EOF,"");
    }
    return encodingMap;
}

// Recursive function that puts all the byte to string representations into the map.
void buildEncodingMapHelper(HuffmanNode* tree, Map<int, string>& map, string chosen){
    if(tree->isLeaf()){
        map.put(tree->character, chosen);
    } else{
        buildEncodingMapHelper(tree->zero, map, chosen+"0");
        buildEncodingMapHelper(tree->one, map, chosen+"1");
    }
}

// Takes the input and puts all the characters in their binary representations into the output.
void encodeData(istream& input, const Map<int, string>& encodingMap, obitstream& output) {
    int currentCharacter = input.get();
    while(currentCharacter != -1){
        string value = encodingMap[currentCharacter];
        addToOutput(value, output);
        currentCharacter = input.get();
    }
    addToOutput(encodingMap[PSEUDO_EOF], output);
}

// Takes in a binary representation as a string and put it into the output.
void addToOutput(string value, obitstream& output){
    while(value.length() != 0){
        int out = stringToInteger(value.substr(0, 1));
        output.writeBit(out);
        value = value.substr(1);
    }
}

// Decodes the binary data in the input and writes it into the output.
void decodeData(ibitstream& input, HuffmanNode* encodingTree, ostream& output) {
    while(true){
        HuffmanNode* current = encodingTree;
        int character = decodeDataHelper(input, current);
        if (character == PSEUDO_EOF){
            break;
        }
        output.put(character);
    }
}

// Recursive algorithm that takes in bits from the input and writes a character into the output if it traces down to a leaf.
int decodeDataHelper(ibitstream& input, HuffmanNode* current){
    if(current->isLeaf()){
        return current->character;
    } else{
        int nextDigit = input.readBit();
        if(nextDigit == 1){
            return decodeDataHelper(input, current->one);
        } else if (nextDigit == 0){
            return decodeDataHelper(input, current->zero);
        } else{
            return PSEUDO_EOF;
        }
    }
}

// Compresses and encodes the input file into the output.
void compress(istream& input, obitstream& output) {
    Map<int, int> freqTable = buildFrequencyTable(input);
    output << freqTable;
    HuffmanNode* root = buildEncodingTree(freqTable);
    Map<int, string> mappedTree = buildEncodingMap(root);
    rewindStream(input);
    encodeData(input, mappedTree, output);
}

// Takes in the file using the current frequency table and decodes it to the output.
void decompress(ibitstream& input, ostream& output) {
    Map<int, int> freqTable;
    input >> freqTable;
    HuffmanNode* root = buildEncodingTree(freqTable);
    decodeData(input, root, output);
}

// Frees the memory used by the binary tree.
void freeTree(HuffmanNode* node) {
    if(node != NULL){
        freeTree(node->one);
        freeTree(node->zero);
        delete node;
    }
}
