package com.equationl.lifegame.model

import android.graphics.Point
import android.util.Log
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.dataModel.BlockState
import kotlin.random.Random

private const val TAG = "PlayGroundState"

data class PlayGroundState(
    val lifeList: List<List<Block>>
) {

    /**
     * 更新一步状态
     * */
    fun stepUpdate(): List<List<Block>> {
        // 深度复制，不然无法 recompose
        val newLifeList: MutableList<List<Block>> = mutableListOf()
        lifeList.forEach { lineList ->
            newLifeList.add(lineList.map { it.copy() })
        }

        newLifeList.forEachIndexed { columnIndex, lineList ->
            lineList.forEachIndexed { rowIndex, block ->
                val aroundAliveCount = getRoundAliveCount(Point(rowIndex, columnIndex))
                if (block.State.isAlive()) { // 当前细胞存活
                    if (aroundAliveCount < 2) block.State = BlockState.DEAD
                    if (aroundAliveCount > 3) block.State = BlockState.DEAD
                }
                else { // 当前细胞死亡
                    if (aroundAliveCount == 3) block.State = BlockState.ALIVE
                }
            }
        }

        return newLifeList
    }

    private fun getRoundAliveCount(pos: Point): Int {
        var count = 0
        // 将当前细胞周围细胞按照下面序号编号
        //   y  y  y
        // x 0  1  2
        // x 3 pos 4
        // x 5  6  7

        // 查找 0 号
        if (pos.x > 0 && pos.y > 0 && lifeList[pos.y-1][pos.x-1].State.isAlive()) count++
        // 查找 1 号
        if (pos.y > 0 && lifeList[pos.y-1][pos.x].State.isAlive()) count++
        // 查找 2 号
        if (pos.x < lifeList[0].lastIndex && pos.y > 0 && lifeList[pos.y-1][pos.x+1].State.isAlive()) count++
        // 查找 3 号
        if (pos.x > 0 && lifeList[pos.y][pos.x-1].State.isAlive()) count++
        // 查找 4 号
        if (pos.x < lifeList[0].lastIndex && lifeList[pos.y][pos.x+1].State.isAlive()) count++
        // 查找 5 号
        if (pos.x > 0 && pos.y < lifeList.lastIndex && lifeList[pos.y+1][pos.x-1].State.isAlive()) count++
        // 查找 6 号
        if (pos.y < lifeList.lastIndex && lifeList[pos.y+1][pos.x].State.isAlive()) count++
        // 查找 7 号
        if (pos.x < lifeList[0].lastIndex && pos.y < lifeList.lastIndex && lifeList[pos.y+1][pos.x+1].State.isAlive()) count++

        return count
    }

    companion object {
        /**
         * 随机生成一个初始图
         * */
        fun randomGenerate(width: Int, height: Int, seed: Long = System.currentTimeMillis()): List<List<Block>> {
            val list = mutableListOf<MutableList<Block>>()
            val random = Random(seed)

            for (h in 0 until height) {
                val lineList = mutableListOf<Block>()
                for (w in 0 until width) {
                    lineList.add(Block(if (random.nextBoolean()) BlockState.ALIVE else BlockState.DEAD))
                }
                list.add(lineList)
            }

            return list
        }
    }
}