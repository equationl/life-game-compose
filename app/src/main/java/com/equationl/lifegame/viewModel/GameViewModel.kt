package com.equationl.lifegame.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.equationl.lifegame.R
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.dataModel.BlockState
import com.equationl.lifegame.model.*

class GameViewModel : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }

    var viewStates by mutableStateOf(ViewState())
        private set

    fun dispatch(action: GameAction) {
        when (action) {
            is GameAction.RunStep -> runStep()
            is GameAction.ToggleGameState -> toggleGameState()
            is GameAction.RandomGenerate -> randomGenerate(action.width, action.height, action.seed)
            is GameAction.ChangeSpeed -> changeSpeed(action.speed)
            is GameAction.Import -> import(action.no, action.context)
        }
    }

    private fun import(no: Int, context: Context) {
        val sourceString = context.resources.openRawResource(R.raw.bomber).bufferedReader().use { it.readText() }
        val lifeList: MutableList<MutableList<Block>> = mutableListOf()

        sourceString.lines().forEach { string ->
            val line = mutableListOf<Block>()
            string.forEach { char ->
                if (char == '.') line.add(Block(BlockState.DEAD))
                if (char == '*') line.add(Block(BlockState.ALIVE))
            }
            lifeList.add(line)
        }

        viewStates = viewStates.copy(gameState = GameState.Wait,
            playGroundState = PlayGroundState(
                lifeList,
                Size(100f, 100f),
                -1,
                0
            ))
    }

    private fun changeSpeed(speed: RunningSpeed) {
        viewStates = viewStates.copy(
            playGroundState = viewStates.playGroundState.copy(speed = speed)
        )
    }

    private fun runStep() {
        val newList = viewStates.playGroundState.stepUpdate()
        viewStates = viewStates.copy(
            playGroundState = viewStates.playGroundState.copy(
                lifeList = newList,
                step = viewStates.playGroundState.step+1
            )
        )
    }

    private fun toggleGameState() {
        Log.i(TAG, "toggleGameState: call!")
        viewStates = if (viewStates.gameState == GameState.Running) {
            viewStates.copy(gameState = GameState.Pause)
        } else {
            viewStates.copy(gameState = GameState.Running)
        }
    }

    private fun randomGenerate(width: Int, height: Int, seed: Long) {
        viewStates = viewStates.copy(gameState = GameState.Wait,
            playGroundState = PlayGroundState(
                PlayGroundState.randomGenerate(width, height, seed),
                Size(width.toFloat(), height.toFloat()),
                seed,
                0
            ))
    }
}