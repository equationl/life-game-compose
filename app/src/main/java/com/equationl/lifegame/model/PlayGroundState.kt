package com.equationl.lifegame.model

import androidx.compose.ui.geometry.Size
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.utils.PlayGroundUtils
import kotlin.random.Random

private const val TAG = "PlayGroundState"

data class PlayGroundState(
    val lifeList: Array<IntArray>,
    val size: Size,
    val seed: Long,
    val step: Int,
    val speed: RunningSpeed = RunningSpeed.Normal
) {

    /**
     * 更新一步状态
     * */
    fun stepUpdate(): Array<IntArray> {
        return PlayGroundUtils.stepUpdate(lifeList)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayGroundState

        if (!lifeList.contentDeepEquals(other.lifeList)) return false
        if (size != other.size) return false
        if (seed != other.seed) return false
        if (step != other.step) return false
        if (speed != other.speed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lifeList.contentDeepHashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + seed.hashCode()
        result = 31 * result + step
        result = 31 * result + speed.hashCode()
        return result
    }

    /*private fun getRoundAliveCount(posX: Int, posY: Int, columnLastIndex: Int, rowLastIndex: Int): Int {
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
    }*/

    companion object {
        /**
         * 随机生成一个初始图
         * */
        fun randomGenerate(width: Int, height: Int, seed: Long = System.currentTimeMillis()): Array<IntArray> {
            val list: Array<IntArray> = Array(height) {
                IntArray(1)
            }
            val random = Random(seed)

            for (h in 0 until height) {
                val lineList = IntArray(width)
                for (w in 0 until width) {
                    lineList[w] = if (random.nextBoolean()) Block.ALIVE else Block.DEAD
                }
                list[h] = lineList
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