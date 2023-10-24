package view.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

@Composable
fun ExpandableButton(
    text: String,
    modifier: Modifier = Modifier,
    orientation: ExpandableButtonOri = ExpandableButtonOri.DOWN,
    subItem: @Composable () -> Unit
) {
    val rotateAngle = if (orientation == ExpandableButtonOri.DOWN) 90f else -90f
    var isShowSubItem by remember { mutableStateOf(false) }
    val arrowRotateDegrees: Float by animateFloatAsState(if (isShowSubItem) rotateAngle else 0f)

    Column(modifier = modifier) {
        if (orientation == ExpandableButtonOri.UP) {
            AnimatedVisibility(visible = isShowSubItem) {
                Column {
                    subItem()
                }
            }
        }

        OutlinedButton(onClick = {
            isShowSubItem = !isShowSubItem
        }) {
            Text(text = text)
            Icon(imageVector = Icons.Outlined.ArrowRight, contentDescription = text, modifier = Modifier.rotate(arrowRotateDegrees))
        }

        if (orientation == ExpandableButtonOri.DOWN) {
            AnimatedVisibility(visible = isShowSubItem) {
                Column {
                    subItem()
                }
            }
        }
    }
}

enum class ExpandableButtonOri {
    UP,
    DOWN
}