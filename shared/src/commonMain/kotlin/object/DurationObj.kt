package `object`

import kotlin.native.concurrent.ThreadLocal
import kotlin.time.Duration

@ThreadLocal
object DurationObj {
    var lastStepDuration = Duration.ZERO
}