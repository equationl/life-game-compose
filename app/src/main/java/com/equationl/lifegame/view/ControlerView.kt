package com.equationl.lifegame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.equationl.lifegame.model.GameAction
import com.equationl.lifegame.model.GameState
import com.equationl.lifegame.model.RunningSpeed
import com.equationl.lifegame.view.widgets.ExpandableButton
import com.equationl.lifegame.view.widgets.ExpandableButtonOri
import com.equationl.lifegame.viewModel.GameViewModel

@Composable
fun ControlBar(viewModel: GameViewModel, gameState: GameState) {
    Row {
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxHeight()) {
            OutlinedButton(
                onClick = {
                    viewModel.dispatch(GameAction.ToggleGameState)
                }
            ) {
                Text(text = gameState.msg)
            }
            OutlinedButton(modifier = Modifier.padding(start = 4.dp), enabled = gameState != GameState.Running, onClick = {
                viewModel.dispatch(GameAction.RunStep)
            }) {
                Text(text = "Step")
            }
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxHeight()) {
            ExpandableButton(text = "Random", modifier = Modifier.padding(start = 4.dp), orientation = ExpandableButtonOri.UP) {
                RandomGenerateItem { width, height, seed ->
                    viewModel.dispatch(GameAction.RandomGenerate(width, height, seed))
                }
            }
        }
        Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxHeight()) {
            ExpandableButton(text = "Speed", modifier = Modifier.padding(start = 4.dp), orientation = ExpandableButtonOri.UP) {
                SpeedItem {
                    viewModel.dispatch(GameAction.ChangeSpeed(it))
                }
            }
        }
    }
}

@Composable
fun SpeedItem(onClick: (speed: RunningSpeed) -> Unit) {
    Column(Modifier.background(Color.White)) {
        RunningSpeed.values().forEach {
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
    var seed by remember { mutableStateOf(System.currentTimeMillis().toString()) }

    Column(Modifier.background(Color.White)) {
        OutlinedTextField(
            value = width,
            label = {
                Text(text = "Width")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { value: String ->
                if (value.isDigitsOnly()) {
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
                if (value.isDigitsOnly()) {
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
                if (value.isDigitsOnly()) {
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

@Preview(showSystemUi = true)
@Composable
fun ControlPreview() {
    ControlBar(GameViewModel(), GameState.Wait)
}