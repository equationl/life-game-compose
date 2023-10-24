#include <jni.h>
#include <string>

#include <valarray>

// #include <iostream>
// #include <fstream>
// #include <cstring>
// using namespace std;


extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_equationl_nativelib_NativeLib_stepUpdate(
        JNIEnv* env,
        jobject,
        jobjectArray lifeList
        ) {

    //ofstream out("/Users/equationl/testtest.txt", ios::out | ios::binary);

    //out.write("start", 5);


    int len1 = env -> GetArrayLength(lifeList);
    auto dim =  (jintArray)env->GetObjectArrayElement(lifeList, 0);
    int len2 = env -> GetArrayLength(dim);
    int **board;
    board = new int *[len1];
    for(int i=0; i<len1; ++i){
        auto oneDim = (jintArray)env->GetObjectArrayElement(lifeList, i);
        jint *element = env->GetIntArrayElements(oneDim, JNI_FALSE);
        board[i] = new int [len2];
        for(int j=0; j<len2; ++j) {
            //out.write(std::to_string(element[j]).c_str(), 1);
            board[i][j]= element[j];
        }
        // 释放数组
        // ① 模式 0 : 刷新 Java 数组 , 释放 C/C++ 数组
        // ② 模式 1 ( JNI_COMMIT ) : 刷新 Java 数组 , 不释放 C/C ++ 数组
        // ③ 模式 2 ( JNI_ABORT ) : 不刷新 Java 数组 , 释放 C/C++ 数组
        env->ReleaseIntArrayElements(oneDim, element, JNI_ABORT);
        // 释放引用
        env->DeleteLocalRef(oneDim);
    }


    /*int neighbors[3] = {0, 1, -1};

    int rows = len1;
    int cols = len2;

    // 遍历面板每一个格子里的细胞
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {

            // 对于每一个细胞统计其八个相邻位置里的活细胞数量
            int liveNeighbors = 0;

            for (int neighbor : neighbors) {
                for (int j : neighbors) {

                    if (!(neighbor == 0 && j == 0)) {
                        // 相邻位置的坐标
                        int r = (row + neighbor);
                        int c = (col + j);

                        // 查看相邻的细胞是否是活细胞
                        if ((r < rows && r >= 0) && (c < cols && c >= 0) && (abs(board[r][c]) == 1)) {
                            liveNeighbors += 1;
                        }
                    }
                }
            }

            // 规则 1 或规则 3
            if ((board[row][col] == 1) && (liveNeighbors < 2 || liveNeighbors > 3)) {
                // -1 代表这个细胞过去是活的现在死了
                board[row][col] = -1;
            }
            // 规则 4
            if (board[row][col] == 0 && liveNeighbors == 3) {
                // 2 代表这个细胞过去是死的现在活了
                board[row][col] = 2;
            }
        }
    }

    // 遍历 board 得到一次更新后的状态
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            if (board[row][col] > 0) {
                board[row][col] = 1;
            } else {
                board[row][col] = 0;
            }
        }
    }*/


    /*
     * 该算法来源如下：
     *
     * 作者：Time-Limit
     * 链接：https://leetcode.cn/problems/game-of-life/solution/c-wei-yun-suan-yuan-di-cao-zuo-ji-bai-shuang-bai-b/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    */
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

    jclass cls = env->FindClass("[I");
    jintArray iniVal = env->NewIntArray(len2);
    jobjectArray result = env->NewObjectArray(len1, cls, iniVal);

    for (int i = 0; i < len1; i++)
    {
        jintArray inner = env->NewIntArray(len2);
        env->SetIntArrayRegion(inner, 0, len2, (jint*)board[i]);
        env->SetObjectArrayElement(result, i, inner);
        env->DeleteLocalRef(inner);
    }

    //out.close();

    return result;
}