package com.equationl.nativelib

import java.io.File

class NativeLib {

    external fun stepUpdate(lifeList: Array<IntArray>): Array<IntArray>

    @Suppress("UnsafeDynamicallyLoadedCode")
    companion object {
        init {
            val libFile = File(System.getProperty("compose.application.resources.dir")).resolve("lib").resolve("nativelib.bin")
            System.load(libFile.absolutePath)
        }
    }
}