package platform

import dataModel.Block
import dataModel.Block.isAlive

actual object PlayGroundUtils {
    actual fun stepUpdate(sourceData: Array<IntArray>): Array<IntArray> {
        // TODO 移植到 cinterop

        val newLifeList: MutableList<IntArray> = mutableListOf()
        sourceData.forEach { lineList ->
            newLifeList.add(lineList.map { it }.toIntArray())
        }

        val columnLastIndex = newLifeList.size - 1
        val rowLastIndex = newLifeList[0].size - 1

        newLifeList.forEachIndexed { columnIndex, lineList ->
            lineList.forEachIndexed { rowIndex, block ->
                val aroundAliveCount = getRoundAliveCount(sourceData, rowIndex, columnIndex, columnLastIndex, rowLastIndex)
                if (block.isAlive()) { // 当前细胞存活
                    if (aroundAliveCount < 2) newLifeList[columnIndex][rowIndex] = Block.DEAD
                    if (aroundAliveCount > 3) newLifeList[columnIndex][rowIndex] = Block.DEAD
                }
                else { // 当前细胞死亡
                    if (aroundAliveCount == 3) newLifeList[columnIndex][rowIndex] = Block.ALIVE
                }
            }
        }

        return newLifeList.toTypedArray()
    }

    private fun getRoundAliveCount(lifeList: Array<IntArray>, posX: Int, posY: Int, columnLastIndex: Int, rowLastIndex: Int): Int {
        var count = 0
        // 将当前细胞周围细胞按照下面序号编号
        //   y  y  y
        // x 0  1  2
        // x 3 pos 4
        // x 5  6  7

        if (posY > 0) {
            val topLine = lifeList[posY-1]

            // 查找 0 号
            if (posX > 0 && topLine[posX-1].isAlive()) count++
            // 查找 1 号
            if (topLine[posX].isAlive()) count++
            // 查找 2 号
            if (posX < rowLastIndex && topLine[posX+1].isAlive()) count++
        }

        if (posY < columnLastIndex) {
            val bottomLine = lifeList[posY+1]

            // 查找 5 号
            if (posX > 0 && bottomLine[posX-1].isAlive()) count++
            // 查找 6 号
            if ( bottomLine[posX].isAlive()) count++
            // 查找 7 号
            if (posX < rowLastIndex && bottomLine[posX+1].isAlive()) count++
        }

        val currentLine = lifeList[posY]
        // 查找 3 号
        if (posX > 0 && currentLine[posX-1].isAlive()) count++
        // 查找 4 号
        if (posX < rowLastIndex && currentLine[posX+1].isAlive()) count++


        return count
    }
}