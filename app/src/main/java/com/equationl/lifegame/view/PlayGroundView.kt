package com.equationl.lifegame.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.dataModel.Block.getColor
import com.equationl.lifegame.dataModel.Block.isAlive
import com.equationl.lifegame.model.PlayGroundState

@Composable
fun PlayGround(playGroundState: PlayGroundState) {
    val blockList: MutableList<MutableList<Int>> = playGroundState.lifeList
    Canvas(modifier = Modifier
        .size((blockList[0].size * Block.SIZE).dp, (blockList.size * Block.SIZE).dp)
        .background(Color.Black)
    ) {
        blockList.forEachIndexed { Column, lineList ->
            lineList.forEachIndexed { row, block ->
                if (block.isAlive()) {
                    drawRect(color = block.getColor(),
                        topLeft = Offset(row*Block.SIZE.dp.toPx(), Column*Block.SIZE.dp.toPx()),
                        size = Size(Block.SIZE.dp.toPx(), Block.SIZE.dp.toPx()))
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
    PlayGround(playGroundState)
}