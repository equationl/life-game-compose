package com.equationl.nativelib

import java.io.File

class NativeLib {

    external fun stepUpdate(lifeList: Array<IntArray>): Array<IntArray>

    @Suppress("UnsafeDynamicallyLoadedCode")
    companion object {
        init {
            //TODO 尚未实现自动编译，目前需要手动编译
            // 编译命令 g++ nativelib.cpp -o nativelib.dll -shared -fPIC -I C:\Users\DELL\.jdks\corretto-19.0.2\include -I C:\Users\DELL\.jdks\corretto-19.0.2\include\win32
            // C:\Users\DELL\.jdks\corretto-19.0.2 为 JDK 路径
            // 编译完成后需要将编译生成的二进制库移至 desktopApp/resources/lib 目录下
            val libFile = File(System.getProperty("compose.application.resources.dir")).resolve("lib").resolve("nativelib.dll")
            System.load(libFile.absolutePath)
        }
    }
}