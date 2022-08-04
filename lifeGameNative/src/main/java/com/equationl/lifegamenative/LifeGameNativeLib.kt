package com.equationl.lifegamenative

class LifeGameNativeLib {

    external fun stepUpdate(lifeList: Array<IntArray>): Array<IntArray>

    companion object {
        init {
            System.loadLibrary("lifegamenative")
        }
    }
}