package com.equationl.lifegame.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.equationl.lifegame.model.GameAction
import com.equationl.lifegame.model.GameState
import com.equationl.lifegame.model.PlayGroundState
import com.equationl.lifegame.model.ViewState

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
            is GameAction.Clear -> clear()
            is GameAction.RandomGenerate -> randomGenerate(action.width, action.height, action.seed)
        }
    }

    private fun clear() {
        viewStates = viewStates.copy(gameState = GameState.Wait,
            playGroundState = PlayGroundState(PlayGroundState.randomGenerate(1, 1)))
    }

    private fun runStep() {
        val newList = viewStates.playGroundState.stepUpdate()
        viewStates = viewStates.copy(playGroundState = PlayGroundState(newList))
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
            playGroundState = PlayGroundState(PlayGroundState.randomGenerate(width, height, seed)))
    }
}