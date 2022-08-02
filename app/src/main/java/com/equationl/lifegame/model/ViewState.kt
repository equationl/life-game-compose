package com.equationl.lifegame.model

import androidx.compose.ui.geometry.Size

data class ViewState(
    val gameState: GameState = GameState.Wait,
    val playGroundState: PlayGroundState = PlayGroundState(
        PlayGroundState.randomGenerate(50, 50, 1),
        Size(50f, 50f),
        1,
        0
    )
)

enum class GameState(val msg: String) {
    Wait("Start"),
    Running("Pause"),
    Pause("Resume"),
}

sealed class GameAction {
    object RunStep: GameAction()
    object ToggleGameState: GameAction()
    data class RandomGenerate(val width: Int, val height: Int, val seed: Long): GameAction()
    data class ChangeSpeed(val speed: RunningSpeed): GameAction()
}