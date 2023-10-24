package platform

import androidx.compose.ui.res.useResource

actual suspend fun readResourceAsString(name: String, path: String): String {
    return useResource("$path$name") {
        it.bufferedReader().readText()
    }
}