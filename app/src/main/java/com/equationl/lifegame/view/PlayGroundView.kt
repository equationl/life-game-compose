package com.equationl.lifegame.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.dataModel.Block.getColor
import com.equationl.lifegame.dataModel.Block.isAlive
import com.equationl.lifegame.model.PlayGroundState

@Composable
fun PlayGround(
    playGroundState: PlayGroundState,
    onGroundChange: (scaleChange: Float, offsetChange: Offset) -> Unit,
) {
    val blockList = playGroundState.lifeList

    val scale = playGroundState.scale
    val offset = playGroundState.offset

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        onGroundChange(zoomChange, offsetChange)
    }

    Canvas(modifier = Modifier
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale,
        )
        .transformable(state = state, lockRotationOnZoomPan = true)
        .size((blockList[0].size * Block.SIZE * scale).dp, (blockList.size * Block.SIZE * scale).dp)
        .background(Color.Black)
    ) {
        blockList.forEachIndexed { column, lineList ->
            lineList.forEachIndexed { row, block ->
                if (block.isAlive()) {
                    drawRect(color = block.getColor(),
                        topLeft = Offset(scale*row*Block.SIZE.dp.toPx()+offset.x, scale*column*Block.SIZE.dp.toPx()+offset.y),
                        size = Size(scale*Block.SIZE.dp.toPx(), scale*Block.SIZE.dp.toPx()))
                }
            }
        }
    }
}

@Composable
fun PlayInfo(playGroundState: PlayGroundState) {
    Column(Modifier.background(Color.White)) {
        Text(text = "Step: ${playGroundState.step}")
        Text(text = "Info: ${playGroundState.size.width}x${playGroundState.size.height};@${playGroundState.seed};X${playGroundState.speed.title}")
    }
}

@Preview
@Composable
fun PLayGroundPreview() {
    val playGroundState = PlayGroundState(
        PlayGroundState.randomGenerate(50, 50, 1),
        Size(50f, 50f),
        1,
        0
    )
    PlayGround(playGroundState) { _, _ -> }
}