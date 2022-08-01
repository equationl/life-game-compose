package com.equationl.lifegame.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.equationl.lifegame.viewModel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    Column(Modifier.fillMaxSize()) {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            PlayGround(playGroundState = viewModel.viewStates.playGroundState)
        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            ControlBar(viewModel, gameState = viewModel.viewStates.gameState)
        }
    }
}

@Preview
@Composable
fun PreviewGameScreen() {
    GameScreen(GameViewModel())
}