package view.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableButton(
    text: String,
    modifier: Modifier = Modifier,
    subItem: @Composable (onDismissRequest: () -> Unit) -> Unit
) {
    var isShowSubItem by remember { mutableStateOf(false) }
    val arrowRotateDegrees: Float by animateFloatAsState(if (isShowSubItem) -90f else 0f)

    Column(modifier = modifier) {

        DropdownMenu(
            expanded = isShowSubItem,
            onDismissRequest = {
                isShowSubItem = false
            },
            modifier = Modifier.background(MaterialTheme.colors.surface)
        ) {
            Column(
                modifier = Modifier.background(MaterialTheme.colors.surface).padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                subItem {
                    isShowSubItem = false
                }
            }
        }

        OutlinedButton(onClick = {
            isShowSubItem = !isShowSubItem
        }) {
            Text(text = text)
            Icon(imageVector = Icons.Outlined.ArrowRight, contentDescription = text, modifier = Modifier.rotate(arrowRotateDegrees))
        }

    }
}