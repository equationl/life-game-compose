package platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.toCValues
import nativelib.update

actual object PlayGroundUtils {
    actual fun stepUpdate(sourceData: Array<IntArray>): Array<IntArray> {
        return stepUpdateNative(sourceData)
    }
}
@OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)
fun stepUpdateNative(sourceData: Array<IntArray>): Array<IntArray> {

    val row = sourceData.size
    val col = sourceData[0].size

    val list1 = sourceData.map { it.pin() }
    val passList = list1.map { it.addressOf(0) }
    val result = mutableListOf<IntArray>()

    memScoped {
        val arg = allocArray<IntVar>(row * col)
        val resultNative = update(passList.toCValues(), row, col, arg)
        for (i in 0 until row) {
            val line = IntArray(col)
            for (j in 0 until col) {
                val index = i * col + j
                line[j] = resultNative!![index]
                // println("current value from kotlin: result[$index] = ${resultNative?.get(index)}")
            }
            result.add(line)
        }
    }

//    list1.forEach {
//        it.unpin()
//    }

//    result.forEachIndexed { index, ints ->
//        println("line[$index] = ${ints.contentToString()}")
//    }

    return result.toTypedArray()
}