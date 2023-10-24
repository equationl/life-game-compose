package platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalResourceApi::class, ExperimentalForeignApi::class)
actual suspend fun readResourceAsString(name: String, path: String): String {
    return resource("$path$name").readBytes().toKString()
}