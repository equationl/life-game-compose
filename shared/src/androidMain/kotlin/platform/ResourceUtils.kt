package platform

import com.equationl.BaseApplication
import java.io.InputStreamReader

actual fun readResourceAsString(name: String, path: String): String {
/*    return with(BaseApplication.instance()) {
        val resourceId = resources.getIdentifier(
            name.substringBefore("."), "raw", packageName
        )
        resources.openRawResource(resourceId)
            .bufferedReader().readText()
    }*/

    return BaseApplication.instance().assets.open(name).use { stream ->
        InputStreamReader(stream).use { reader ->
            reader.readText()
        }
    }
}