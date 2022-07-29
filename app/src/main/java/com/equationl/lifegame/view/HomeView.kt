package com.equationl.lifegame.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.equationl.lifegame.viewModel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    Column(Modifier.fillMaxSize()) {
        PlayGround(playGroundState = viewModel.viewStates.playGroundState)
        ControlBar(viewModel, gameState = viewModel.viewStates.gameState)
    }
}

@Preview
@Composable
fun PreviewGameScreen() {
    GameScreen(GameViewModel())
}