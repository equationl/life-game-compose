package viewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import constant.DefaultGame
import dataModel.Block
import kotlinx.coroutines.flow.Flow
import model.Algorithm
import model.GameAction
import model.GameState
import model.PlayGroundState
import model.RunningSpeed
import model.ViewState
import `object`.DurationObj
import platform.readResourceAsString
import kotlin.time.measureTime


@Composable
fun GamePresenter(
    gameActionFlow: Flow<GameAction>
): ViewState {
    val viewState = remember { mutableStateOf(ViewState()) }

    LaunchedEffect(Unit) {
        gameActionFlow.collect { action ->
            when (action) {
                is GameAction.RunStep -> runStep(viewState)
                is GameAction.ToggleGameState -> toggleGameState(viewState)
                is GameAction.RandomGenerate -> randomGenerate(viewState, action.width, action.height, action.seed)
                is GameAction.ChangeSpeed -> changeSpeed(viewState, action.speed)
                is GameAction.Import -> import(viewState, action.select)
                is GameAction.Load -> load(viewState, action.data)
                is GameAction.ChangeGround -> changeGround(viewState, action.scaleChange, action.offsetChange)
                is GameAction.Reset -> reset(viewState)
                is GameAction.ChangeAlgorithm -> changeAlgorithm(viewState, action.algorithm)
            }
        }
    }

    return viewState.value
}

private fun load(viewState: MutableState<ViewState>, data: String) {
    loadFromText(viewState, data)
}

private fun changeAlgorithm(viewState: MutableState<ViewState>, algorithm: Algorithm) {
    viewState.value = viewState.value.copy(
        gameState = GameState.Pause,
        algorithm = algorithm
    )
}

private fun reset(viewStates: MutableState<ViewState>) {
    val playGroundState = viewStates.value.playGroundState

    viewStates.value = viewStates.value.copy(
        playGroundState = playGroundState.copy(
            scale = 1f,
            offset = Offset.Zero
        )
    )
}

private fun changeGround(viewStates: MutableState<ViewState>, scaleChange: Float, offsetChange: Offset) {
    val playGroundState = viewStates.value.playGroundState

    viewStates.value = viewStates.value.copy(
        playGroundState = playGroundState.copy(
            scale = playGroundState.scale * scaleChange,
            offset = playGroundState.offset + offsetChange
        )
    )
}

private suspend fun import(viewStates: MutableState<ViewState>, select: DefaultGame) {

    val sourceString = readResourceAsString(select.loadName, "assets/") // context.resources.openRawResource(R.raw.bomber).bufferedReader().use { it.readText() }
    loadFromText(viewStates, sourceString)
}

private fun changeSpeed(viewStates: MutableState<ViewState>, speed: RunningSpeed) {
    viewStates.value = viewStates.value.copy(
        playGroundState = viewStates.value.playGroundState.copy(speed = speed)
    )
}

private fun runStep(viewStates: MutableState<ViewState>) {

    val newList: Array<IntArray>

    val duration = measureTime {
        newList = viewStates.value.playGroundState.stepUpdate(viewStates.value.algorithm)
    }

    viewStates.value = viewStates.value.copy(
        playGroundState = viewStates.value.playGroundState.copy(
            lifeList = newList,
            step = viewStates.value.playGroundState.step+1,
            nowStepDuration = duration,
            totalDuration = duration + viewStates.value.playGroundState.totalDuration
        )
    )

    DurationObj.lastStepDuration = duration
}

private fun toggleGameState(viewStates: MutableState<ViewState>) {
    //Log.i(TAG, "toggleGameState: call!")
    viewStates.value = if (viewStates.value.gameState == GameState.Running) {
        viewStates.value.copy(gameState = GameState.Pause)
    } else {
        viewStates.value.copy(gameState = GameState.Running)
    }
}

private fun randomGenerate(viewStates: MutableState<ViewState>, width: Int, height: Int, seed: Long) {
    viewStates.value = viewStates.value.copy(gameState = GameState.Wait,
        playGroundState = PlayGroundState(
            PlayGroundState.randomGenerate(width, height, seed),
            Size(width.toFloat(), height.toFloat()),
            seed,
            0
        )
    )
}

private fun loadFromText(viewStates: MutableState<ViewState>, sourceString: String) {
    val lifeList: Array<IntArray> = Array(sourceString.lines().size) {
        IntArray(1)
    }

    sourceString.lines().forEachIndexed { lineIndex, string ->
        val line = IntArray(string.length)
        string.forEachIndexed { index, char ->
            if (char == '.') line[index] = Block.DEAD
            if (char == '*') line[index] = Block.ALIVE
        }
        lifeList[lineIndex] = line
    }

    viewStates.value = viewStates.value.copy(gameState = GameState.Wait,
        playGroundState = PlayGroundState(
            lifeList,
            Size(lifeList[0].size.toFloat(), lifeList.size.toFloat()),
            -1,
            0
        )
    )
}