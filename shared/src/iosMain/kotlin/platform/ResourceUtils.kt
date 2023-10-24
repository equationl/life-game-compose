package platform

import kotlinx.cinterop.*
import platform.Foundation.NSBundle
import platform.Foundation.NSError
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

actual fun readResourceAsString(name: String, path: String): String {
    // TODO ios 加载资源


    // from: https://luisramos.dev/how-to-share-resources-kmm

    val bundle: NSBundle = NSBundle.bundleForClass(BundleMarker)

    val (filename, type) = when (val lastPeriodIndex = name.lastIndexOf('.')) {
        0 -> {
            null to name.drop(1)
        }
        in 1..Int.MAX_VALUE -> {
            name.take(lastPeriodIndex) to name.drop(lastPeriodIndex + 1)
        }
        else -> {
            name to null
        }
    }
    val path = bundle.pathForResource(filename, type) ?: error("Couldn't get path of $name (parsed as: ${listOfNotNull(filename, type).joinToString(".")})")

    return memScoped {
        val errorPtr = alloc<ObjCObjectVar<NSError?>>()

        NSString.stringWithContentsOfFile(path, encoding = NSUTF8StringEncoding, error = errorPtr.ptr) ?: run {
            // TODO: Check the NSError and throw common exception.
            error("Couldn't load resource: $name. Error: ${errorPtr.value?.localizedDescription} - ${errorPtr.value}")
        }
    }

}