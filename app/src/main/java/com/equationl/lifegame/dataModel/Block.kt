package com.equationl.lifegame.dataModel

import androidx.compose.ui.graphics.Color

data class Block(
    var State: BlockState,
) {
    fun getColor() = if (this.State.isAlive()) Color.White else Color.Black

    companion object {
        /**
         * 每个格子的大小
         * */
        const val SIZE = 5

    }
}

enum class BlockState {
    ALIVE,
    DEAD;

    fun isAlive() = this == ALIVE
}