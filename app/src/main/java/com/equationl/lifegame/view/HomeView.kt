package com.equationl.lifegame.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.equationl.lifegame.model.GameAction
import com.equationl.lifegame.viewModel.GameViewModel

@Composable
fun GameScreen(viewModel: GameViewModel) {
    Box(Modifier.fillMaxSize()) {

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            PlayGround(
                playGroundState = viewModel.viewStates.playGroundState,
                onGroundChange = {
                        scaleChange, offsetChange ->  viewModel.dispatch(GameAction.ChangeGround(scaleChange, offsetChange))
                }
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            PlayInfo(viewModel.viewStates.playGroundState)
        }

        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            ControlBar(viewModel,
                gameState = viewModel.viewStates.gameState,
            )
        }
    }
}

@Preview
@Composable
fun PreviewGameScreen() {
    GameScreen(GameViewModel())
}