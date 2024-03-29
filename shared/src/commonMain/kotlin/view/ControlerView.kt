package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import constant.DefaultGame
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Clock
import model.Algorithm
import model.GameAction
import model.GameState
import model.RunningSpeed
import view.widgets.ExpandableButton

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

        var isShowMoreOption by remember { mutableStateOf(false) }

        AnimatedVisibility(
            visible = isShowMoreOption,
        ) {
            Column {
                Row {
                    ExpandableButton(text = "Speed", modifier = Modifier.padding(start = 4.dp)) { onDismissRequest ->
                        SpeedItem {
                            onDismissRequest()
                            gameChannel.trySend(GameAction.ChangeSpeed(it))
                        }
                    }

                    ExpandableButton(text = "Algorithm", modifier = Modifier.padding(start = 4.dp)) { onDismissRequest ->
                        AlgorithmItem {
                            onDismissRequest()
                            gameChannel.trySend(GameAction.ChangeAlgorithm(it))
                        }
                    }
                }

                Row {
                    ExpandableButton(text = "Random", modifier = Modifier.padding(start = 4.dp)) { onDismissRequest ->
                        RandomGenerateItem { width, height, seed ->
                            onDismissRequest()
                            gameChannel.trySend(GameAction.RandomGenerate(width, height, seed))
                        }
                    }
                    ExpandableButton(text = "Import", modifier = Modifier.padding(start = 4.dp)) { onDismissRequest ->
                        ImportItem {
                            onDismissRequest()
                            gameChannel.trySend(GameAction.Import(it))
                        }
                    }
                    ExpandableButton(text = "Load", modifier = Modifier.padding(start = 4.dp)) { onDismissRequest ->
                        LoadItem {
                            onDismissRequest()
                            gameChannel.trySend(GameAction.Load(it))
                        }
                    }
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
            OutlinedButton(modifier = Modifier.padding(start = 4.dp), onClick = {
                isShowMoreOption = !isShowMoreOption
            }) {
                Text(text = "More")
            }
        }
    }
}

@Composable
fun ImportItem(onClick: (select: DefaultGame) -> Unit) {
    Column(Modifier.background(MaterialTheme.colors.surface)) {
        DefaultGame.entries.forEach {
            Text(text = it.showName, modifier = Modifier.clickable {
                onClick(it)
            })
        }
    }
}

@Composable
fun LoadItem(
    onClick: (data: String) -> Unit
) {
    var data by remember { mutableStateOf("") }

    Column(Modifier.background(MaterialTheme.colors.surface)) {
        Text("Load a game board by text, \".\" mean dead, \"*\" mean alive")

        OutlinedTextField(
            value = data,
            label = {
                Text(text = "board data")
            },
            maxLines = 50,
            singleLine = false,
            onValueChange = { value: String ->
                data = value
            }
        )

        OutlinedButton(onClick = {
            onClick(data)
        }) {
            Text(text = "Load")
        }
    }
}

@Composable
fun AlgorithmItem(onClick: (algorithm: Algorithm) -> Unit) {
    Column(Modifier.background(MaterialTheme.colors.surface)) {
        Algorithm.entries.forEach {
            Text(text = it.title, modifier = Modifier.clickable {
                onClick(it)
            })
        }
    }
}

@Composable
fun SpeedItem(onClick: (speed: RunningSpeed) -> Unit) {
    Column(Modifier.background(MaterialTheme.colors.surface)) {
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

    Column(Modifier.background(MaterialTheme.colors.surface)) {
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