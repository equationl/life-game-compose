package com.equationl.lifegame.dataModel

import androidx.compose.ui.graphics.Color

object Block {
    /**
     * 每个格子的大小
     * */
    const val SIZE = 5

    const val DEAD = 0
    const val ALIVE = 1

    fun Int.getColor() = if (this.isAlive()) Color.White else Color.Black
    fun Int.isAlive() = this == ALIVE
}