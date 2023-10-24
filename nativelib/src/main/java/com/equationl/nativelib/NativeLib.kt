package com.equationl.nativelib

class NativeLib {

    external fun stepUpdate(lifeList: Array<IntArray>): Array<IntArray>

    companion object {
        init {
            System.loadLibrary("nativelib")
        }
    }
}