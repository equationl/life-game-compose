package view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Clock
import model.GameAction
import model.GameState
import model.RunningSpeed
import view.widgets.ExpandableButton
import view.widgets.ExpandableButtonOri

@Composable
fun ControlBar(
    gameChannel: Channel<GameAction>,
    gameState: GameState
) {

    Column(
        Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            ExpandableButton(text = "Random", modifier = Modifier.padding(start = 4.dp), orientation = ExpandableButtonOri.UP) {
                RandomGenerateItem { width, height, seed ->
                    gameChannel.trySend(GameAction.RandomGenerate(width, height, seed))
                }
            }
            ExpandableButton(text = "Speed", modifier = Modifier.padding(start = 4.dp), orientation = ExpandableButtonOri.UP) {
                SpeedItem {
                    gameChannel.trySend(GameAction.ChangeSpeed(it))
                }
            }

            ExpandableButton(text = "Load", modifier = Modifier.padding(start = 4.dp), orientation = ExpandableButtonOri.UP) {
                ImportItem {
                    gameChannel.trySend(GameAction.Import(it))
                }
            }
        }
        Row {
            OutlinedButton(
                onClick = {
                    gameChannel.trySend(GameAction.ToggleGameState)
                }
            ) {
                Text(text = gameState.msg)
            }
            OutlinedButton(modifier = Modifier.padding(start = 4.dp), enabled = gameState != GameState.Running, onClick = {
                gameChannel.trySend(GameAction.RunStep)
            }) {
                Text(text = "Step")
            }
            OutlinedButton(modifier = Modifier.padding(start = 4.dp), enabled = gameState != GameState.Running, onClick = {
                gameChannel.trySend(GameAction.Reset)
            }) {
                Text(text = "Reset")
            }
        }
    }
}

@Composable
fun ImportItem(onClick: (no: Int) -> Unit) {
    Column(modifier = Modifier.background(Color.White)) {
        Text(text = "1", Modifier.clickable {
            onClick(1)
        })
    }
}

@Composable
fun SpeedItem(onClick: (speed: RunningSpeed) -> Unit) {
    Column(Modifier.background(Color.White)) {
        RunningSpeed.entries.forEach {
            Text(text = it.title, modifier = Modifier.clickable {
                onClick(it)
            })
        }
    }
}

@Composable
fun RandomGenerateItem(onClick: (width: Int, height: Int, seed: Long) -> Unit) {
    var width by remember { mutableStateOf("50") }
    var height by remember { mutableStateOf("50") }
    var seed by remember { mutableStateOf(Clock.System.now().epochSeconds.toString()) }

    Column(Modifier.background(Color.White)) {
        OutlinedTextField(
            value = width,
            label = {
                Text(text = "Width")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value: String ->
                if (value.toIntOrNull() != null) {
                    width = value
                }
            }
        )

        OutlinedTextField(
            value = height,
            label = {
                Text(text = "Height")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value: String ->
                if (value.toIntOrNull() != null) {
                    height = value
                }
            }
        )

        OutlinedTextField(
            value = seed,
            label = {
                Text(text = "Seed")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value: String ->
                if (value.toIntOrNull() != null) {
                    seed = value
                }
            }
        )

        OutlinedButton(onClick = {
            onClick(width.toIntOrNull() ?: 50, height.toIntOrNull() ?: 50, seed.toLongOrNull() ?: 0L)
        }) {
            Text(text = "Generate now!")
        }
    }
}