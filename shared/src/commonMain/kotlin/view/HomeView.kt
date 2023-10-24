package view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.channels.Channel
import model.GameAction
import model.ViewState

@Composable
fun GameScreen(
    gameChannel: Channel<GameAction>,
    viewStates: ViewState
) {
    Box(Modifier.fillMaxSize()) {

        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            PlayGround(
                playGroundState = viewStates.playGroundState,
                onGroundChange = { scaleChange, offsetChange ->
                    gameChannel.trySend(GameAction.ChangeGround(scaleChange, offsetChange))
                }
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            PlayInfo(viewStates.playGroundState)
        }

        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            ControlBar(
                gameChannel,
                gameState = viewStates.gameState,
            )
        }
    }
}