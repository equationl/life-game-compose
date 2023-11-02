package model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import constant.DefaultGame

data class ViewState(
    val gameState: GameState = GameState.Wait,
    val playGroundState: PlayGroundState = PlayGroundState(
        PlayGroundState.randomGenerate(50, 50, 1),
        Size(50f, 50f),
        1,
        0
    ),
    val algorithm: Algorithm = Algorithm.Cpp
)

enum class GameState(val msg: String) {
    Wait("Start"),
    Running("Pause"),
    Pause("Resume"),
}

sealed class GameAction {
    data object RunStep: GameAction()
    data object Reset: GameAction()
    data object ToggleGameState: GameAction()
    data class ChangeGround(val scaleChange: Float, val offsetChange: Offset): GameAction()
    data class RandomGenerate(val width: Int, val height: Int, val seed: Long): GameAction()
    data class ChangeSpeed(val speed: RunningSpeed): GameAction()
    data class Import(val select: DefaultGame): GameAction()
    data class Load(val data: String): GameAction()
    data class ChangeAlgorithm(val algorithm: Algorithm): GameAction()
}