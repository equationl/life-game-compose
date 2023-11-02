package model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import dataModel.Block
import kotlinx.datetime.Clock
import platform.PlayGroundUtils
import utils.CommonGameUtils
import kotlin.random.Random
import kotlin.time.Duration

private const val TAG = "PlayGroundState"

data class PlayGroundState(
    val lifeList: Array<IntArray>,
    val size: Size,
    val seed: Long,
    val step: Int,
    val speed: RunningSpeed = RunningSpeed.Normal,
    val scale: Float = 1f,
    val offset: Offset = Offset.Zero,
    val nowStepDuration: Duration = Duration.ZERO,
    val totalDuration: Duration = Duration.ZERO
) {

    /**
     * 更新一步状态
     * */
    fun stepUpdate(algorithm: Algorithm): Array<IntArray> {
        return if (algorithm == Algorithm.Cpp) {
            PlayGroundUtils.stepUpdate(lifeList)
        } else {
            CommonGameUtils.stepUpdate(lifeList)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PlayGroundState

        if (!lifeList.contentDeepEquals(other.lifeList)) return false
        if (size != other.size) return false
        if (seed != other.seed) return false
        if (step != other.step) return false
        if (speed != other.speed) return false
        if (scale != other.scale) return false
        if (offset != other.offset) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lifeList.contentDeepHashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + seed.hashCode()
        result = 31 * result + step
        result = 31 * result + speed.hashCode()
        result = 31 * result + scale.hashCode()
        result = 31 * result + offset.hashCode()
        return result
    }

    companion object {
        /**
         * 随机生成一个初始图
         * */
        fun randomGenerate(width: Int, height: Int, seed: Long = Clock.System.now().epochSeconds): Array<IntArray> {
            val list: Array<IntArray> = Array(height) {
                IntArray(1)
            }
            val random = Random(seed)

            for (h in 0 until height) {
                val lineList = IntArray(width)
                for (w in 0 until width) {
                    lineList[w] = if (random.nextBoolean()) Block.ALIVE else Block.DEAD
                }
                list[h] = lineList
            }

            return list
        }
    }
}

enum class RunningSpeed(val title: String, val delayTime: Long) {
    Slow1("0.25", 164L),
    Slow2("0.5", 82L),
    Normal("1", 41L),
    Fast1("1.5", 26L),
    Fast2("2", 13L),
    Fast4("6", 6L),
    NOT_SPECIFY("unlimited", 1L)
}

enum class Algorithm(val title: String) {
    Kotlin("Kotlin"),
    Cpp("Cpp")
}