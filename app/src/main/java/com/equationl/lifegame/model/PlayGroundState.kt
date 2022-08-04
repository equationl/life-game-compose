package com.equationl.lifegame.model

import androidx.compose.ui.geometry.Size
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.dataModel.Block.isAlive
import kotlin.random.Random

private const val TAG = "PlayGroundState"

data class PlayGroundState(
    val lifeList: MutableList<MutableList<Int>>,
    val size: Size,
    val seed: Long,
    val step: Int,
    val speed: RunningSpeed = RunningSpeed.Normal
) {

    /**
     * 更新一步状态
     * */
    fun stepUpdate(): MutableList<MutableList<Int>> {
        // 深度复制，不然无法 recompose
        val newLifeList: MutableList<MutableList<Int>> = mutableListOf()
        lifeList.forEach { lineList ->
            newLifeList.add(lineList.map { it }.toMutableList())
        }

        val columnLastIndex = newLifeList.size - 1
        val rowLastIndex = newLifeList[0].size - 1

        newLifeList.forEachIndexed { columnIndex, lineList ->
            lineList.forEachIndexed { rowIndex, block ->
                val aroundAliveCount = getRoundAliveCount(rowIndex, columnIndex, columnLastIndex, rowLastIndex)
                if (block.isAlive()) { // 当前细胞存活
                    if (aroundAliveCount < 2) newLifeList[columnIndex][rowIndex] = Block.DEAD
                    if (aroundAliveCount > 3) newLifeList[columnIndex][rowIndex] = Block.DEAD
                }
                else { // 当前细胞死亡
                    if (aroundAliveCount == 3) newLifeList[columnIndex][rowIndex] = Block.ALIVE
                }
            }
        }

        return newLifeList
    }

    private fun getRoundAliveCount(posX: Int, posY: Int, columnLastIndex: Int, rowLastIndex: Int): Int {
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

    companion object {
        /**
         * 随机生成一个初始图
         * */
        fun randomGenerate(width: Int, height: Int, seed: Long = System.currentTimeMillis()): MutableList<MutableList<Int>> {
            val list = mutableListOf<MutableList<Int>>()
            val random = Random(seed)

            for (h in 0 until height) {
                val lineList = mutableListOf<Int>()
                for (w in 0 until width) {
                    lineList.add(if (random.nextBoolean()) Block.ALIVE else Block.DEAD)
                }
                list.add(lineList)
            }

            return list
        }
    }
}

enum class RunningSpeed(val title: String, val delayTime: Long) {
    Slow1("0.25", 164L),
    Slow2("0.5", 82L),
    Normal("1", 41L),
    Fast1("1.5", 26L),
    Fast2("2", 13L),
    Fast4("6", 6L),
}