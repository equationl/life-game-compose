package com.equationl.lifegame.utils

import com.equationl.lifegamenative.LifeGameNativeLib

object PlayGroundUtils {
    private var lifeGameNativeLib: LifeGameNativeLib = LifeGameNativeLib()

    fun stepUpdate(sourceData: Array<IntArray>): Array<IntArray>  {
        return lifeGameNativeLib.stepUpdate(sourceData)
    }
}