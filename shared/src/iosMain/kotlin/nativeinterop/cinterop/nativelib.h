#include "../../../../../../nativelib/src/main/cpp/game.h"

#ifndef LIB2_H_GAME
#define LIB2_H_GAME

int** update(int** board, int len1, int len2) {
    updateStep(board, len1, len2);
    return board;
}

#endif