package com.equationl.lifegame.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equationl.lifegame.dataModel.Block
import com.equationl.lifegame.model.PlayGroundState


@Composable
fun PlayGround(playGroundState: PlayGroundState) {
    val blockList: List<List<Block>> = playGroundState.lifeList
    Column(
        Modifier
            .size((blockList[0].size * Block.SIZE).dp, (blockList.size * Block.SIZE).dp)
            .background(Color.Black)) {
        blockList.forEach { lineList ->
            Row {
                lineList.forEach { block ->
                    Row(modifier = Modifier
                        .size(Block.SIZE.dp)
                        .background(block.getColor())) {}
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PLayGroundPreview() {
    //PlayGround(blockList = PlayGroundState.randomGenerate(70, 90, 1))
}