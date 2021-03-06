// EXTENSION: added LIGHTRED in GUI for bidirectional search.
/*
 * CS 106B Trailblazer
 * This file implements fundamental types used by the Trailblazer assignment.
 *
 * Please do not modify this provided file. Your turned-in files should work
 * with an unmodified version of all provided code files.
 *
 * Author: Marty Stepp, Keith Schwarz, et al
 * Version: 2014/03/05 (initial version for 14wi)
 */

#include "types.h"

/*
 * A large prime number used in hash code computation.
 */
const int kLargePrime = 78979871;

/*
 * A value that can be bitwise-ANDed with an integer to force it to be nonnegative,
 * which is useful when writing hash functions.
 */
const int kHashMask = 0x7FFFFFF;

const Color UNCOLORED = 0;
const Color WHITE = 1;
const Color GRAY = 2;
const Color YELLOW = 3;
const Color GREEN = 4;
const Color RED = 5;
const Color LIGHTRED = 6;


/*
 * Overloaded comparison operators.
 */
bool operator < (TBLoc lhs, TBLoc rhs) {
        if (lhs.row != rhs.row)
                return lhs.row < rhs.row;
        return lhs.col < rhs.col;
}

bool operator > (TBLoc lhs, TBLoc rhs) {
    return rhs < lhs;
}

bool operator <= (TBLoc lhs, TBLoc rhs) {
    return !(rhs < lhs);
}

bool operator >= (TBLoc lhs, TBLoc rhs) {
    return !(lhs < rhs);
}

bool operator == (TBLoc lhs, TBLoc rhs) {
    return lhs.row == rhs.row && lhs.col == rhs.col;
}

bool operator != (TBLoc lhs, TBLoc rhs) {
    return !(lhs == rhs);
}

/*
 * Hash code function to make it possible to store nodes in a hash set/map.
 */
int hashCode(TBLoc l) {
    return (l.row + kLargePrime * l.col) & kHashMask;
}

/*
 * Overloaded comparison operators.
 */
bool operator < (TBEdge lhs, TBEdge rhs) {
    if (lhs.start != rhs.start) return lhs.start < rhs.start;
    return lhs.end < rhs.end;
}

bool operator > (TBEdge lhs, TBEdge rhs) {
    return rhs < lhs;
}

bool operator <= (TBEdge lhs, TBEdge rhs) {
    return !(rhs < lhs);
}

bool operator >= (TBEdge lhs, TBEdge rhs) {
    return !(lhs < rhs);
}

bool operator == (TBEdge lhs, TBEdge rhs) {
    return lhs.start == rhs.start && lhs.end == rhs.end;
}

bool operator != (TBEdge lhs, TBEdge rhs) {
    return !(lhs == rhs);
}

/*
 * Hash code function to make it possible to store arcs in a hash set/map.
 */
int hashCode(TBEdge e) {
    return (hashCode(e.start) + kLargePrime * hashCode(e.end)) & kHashMask;
}
