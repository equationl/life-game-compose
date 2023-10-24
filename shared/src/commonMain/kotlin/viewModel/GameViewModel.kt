package viewModel

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import dataModel.Block
import kotlinx.coroutines.flow.Flow
import model.*
import platform.readResourceAsString


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
                is GameAction.Import -> import(viewState, action.no)
                is GameAction.ChangeGround -> changeGround(viewState, action.scaleChange, action.offsetChange)
                is GameAction.Reset -> reset(viewState)
            }
        }
    }

    return viewState.value
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

private fun import(viewStates: MutableState<ViewState>, no: Int) {
    val sourceString = readResourceAsString("bomber.txt", "assets/") // context.resources.openRawResource(R.raw.bomber).bufferedReader().use { it.readText() }
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
            Size(100f, 100f),
            -1,
            0
        )
    )
}

private fun changeSpeed(viewStates: MutableState<ViewState>, speed: RunningSpeed) {
    viewStates.value = viewStates.value.copy(
        playGroundState = viewStates.value.playGroundState.copy(speed = speed)
    )
}

private fun runStep(viewStates: MutableState<ViewState>) {
    // val startTime = Clock.System.now().epochSeconds
    val newList = viewStates.value.playGroundState.stepUpdate()
    //Log.i(TAG, "runStep: step duration: ${System.currentTimeMillis() - startTime} ms")
    viewStates.value = viewStates.value.copy(
        playGroundState = viewStates.value.playGroundState.copy(
            lifeList = newList,
            step = viewStates.value.playGroundState.step+1
        )
    )
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