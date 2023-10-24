package platform

import com.equationl.nativelib.NativeLib

actual object PlayGroundUtils {
    actual fun stepUpdate(sourceData: Array<IntArray>): Array<IntArray> {
        return NativeLib().stepUpdate(sourceData)
    }
}