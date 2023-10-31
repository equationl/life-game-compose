#include "../../../../../../nativelib/src/main/cpp/game.h"

#ifndef LIB2_H_GAME
#define LIB2_H_GAME

int* update(int** board, int row, int col, int* newList) {
    updateStep(board, row, col);

    // 将结果转为一维数组传回
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
            int value = board[i][j];
            int index = i * col + j;
            newList[index] = value;
        }
    }
    return newList;
}

#endif