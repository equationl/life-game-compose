package com.equationl.lifegame.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equationl.lifegame.model.GameAction
import com.equationl.lifegame.model.GameState
import com.equationl.lifegame.viewModel.GameViewModel

@Composable
fun ControlBar(viewModel: GameViewModel, gameState: GameState) {
    Row {
        Button(onClick = {
            viewModel.dispatch(GameAction.ToggleGameState)
        }) {
            Text(text = gameState.msg)
        }
        Button(modifier = Modifier.padding(start = 4.dp), enabled = gameState != GameState.Running, onClick = {
            viewModel.dispatch(GameAction.RunStep)
        }) {
            Text(text = "Step")
        }
        Button(modifier = Modifier.padding(start = 4.dp), onClick = {
            viewModel.dispatch(GameAction.Clear)
        }) {
            Text(text = "Clear")
        }
        Button(modifier = Modifier.padding(start = 4.dp), onClick = {
            // TODO 暂时写死
            viewModel.dispatch(GameAction.RandomGenerate(70, 100, System.currentTimeMillis()))
        }) {
            Text(text = "Random")
        }
    }
}

@Preview
@Composable
fun ControlPreview() {
    ControlBar(GameViewModel(), GameState.Running)
}