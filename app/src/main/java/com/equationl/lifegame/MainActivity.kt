package com.equationl.lifegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equationl.lifegame.model.GameAction
import com.equationl.lifegame.model.GameState
import com.equationl.lifegame.ui.theme.LifeGameTheme
import com.equationl.lifegame.view.GameScreen
import com.equationl.lifegame.viewModel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class MainActivity : ComponentActivity() {
    companion object {
        const val AutoRunningDuration = 50L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LifeGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: GameViewModel = viewModel()
                    
                    LaunchedEffect(key1 = "game looper") {
                        while (isActive) {
                            if (viewModel.viewStates.gameState == GameState.Running) {
                                viewModel.dispatch(GameAction.RunStep)
                            }
                            delay(AutoRunningDuration)
                        }
                    }

                    GameScreen(viewModel = viewModel)
                }
            }
        }
    }
}
