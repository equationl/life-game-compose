//
// Created by DELL on 2023/10/25.
//

#ifndef LIFEGAME_GAME_H
#define LIFEGAME_GAME_H

/*
* 该算法来源如下：
*
* 作者：Time-Limit
* 链接：https://leetcode.cn/problems/game-of-life/solution/c-wei-yun-suan-yuan-di-cao-zuo-ji-bai-shuang-bai-b/
* 来源：力扣（LeetCode）
* 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
*/
void updateStep(int **board, int len1, int len2) {
    int dx[] = {-1,  0,  1, -1, 1, -1, 0, 1};
    int dy[] = {-1, -1, -1,  0, 0,  1, 1, 1};

    for(int i = 0; i < len1; i++) {
        for(int j = 0 ; j < len2; j++) {
            int sum = 0;
            for(int k = 0; k < 8; k++) {
                int nx = i + dx[k];
                int ny = j + dy[k];
                if(nx >= 0 && nx < len1 && ny >= 0 && ny < len2) {
                    sum += (board[nx][ny]&1); // 只累加最低位
                }
            }
            if(board[i][j] == 1) {
                if(sum == 2 || sum == 3) {
                    board[i][j] |= 2;  // 使用第二个bit标记是否存活
                }
            } else {
                if(sum == 3) {
                    board[i][j] |= 2; // 使用第二个bit标记是否存活
                }
            }
        }
    }
    for(int i = 0; i < len1; i++) {
        for(int j = 0; j < len2 ; j++) {
            board[i][j] >>= 1; //右移一位，用第二bit覆盖第一个bit。
        }
    }
}

#endif //LIFEGAME_GAME_H

