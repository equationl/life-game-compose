import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.isActive
import model.GameAction
import model.GameState
import theme.LifeGameTheme
import view.GameScreen
import viewModel.GamePresenter

@Composable
fun App() {
    val gameChannel: Channel<GameAction> = remember { Channel() }
    val gameFlow = remember(gameChannel) { gameChannel.consumeAsFlow() }
    val viewStates = GamePresenter(gameFlow)


    LifeGameTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            LaunchedEffect(viewStates.gameState) {
                while (isActive) {
                    if (viewStates.gameState == GameState.Running) {
                        gameChannel.trySend(GameAction.RunStep)
                    }
                    delay(viewStates.playGroundState.speed.delayTime)
                }
            }

            GameScreen(gameChannel, viewStates)
        }
    }
}