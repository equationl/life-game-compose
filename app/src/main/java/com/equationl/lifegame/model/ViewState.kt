package com.equationl.lifegame.model

data class ViewState(
    val gameState: GameState = GameState.Wait,
    val playGroundState: PlayGroundState = PlayGroundState(PlayGroundState.randomGenerate(50, 50))
)

enum class GameState(val msg: String) {
    Wait("Start"),
    Running("Pause"),
    Pause("Resume"),
}

sealed class GameAction {
    object RunStep: GameAction()
    object ToggleGameState: GameAction()
    object Clear: GameAction()
    data class RandomGenerate(val width: Int, val height: Int, val seed: Long): GameAction()
}