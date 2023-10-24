import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    sourceSets {
        val jvmMain by getting  {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.equationl.lifegame"
            packageVersion = "1.0.0"

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
        }
    }
}

tasks.register("compileJni") {
    doLast {
        val resourcePath = File(rootProject.projectDir, "desktopApp/resources/common/lib/")
        val binFilePath = File(resourcePath, "nativelib.bin")
        val cppFilePath = File(rootProject.projectDir, "nativelib/src/main/cpp/nativelib.cpp")


        println("compile jni for desktop running……")

        val jdkFile = org.gradle.internal.jvm.Jvm.current().javaHome
        val systemPrefix: String

        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            systemPrefix = "win32"
        }
        else if (Os.isFamily(Os.FAMILY_MAC)) {
            systemPrefix = "darwin"
        }
        else {
            println("UnSupport System for compiler cpp, please compiler manual")
            return@doLast
        }

        val includePath1 = jdkFile.resolve("include")
        val includePath2 = includePath1.resolve(systemPrefix)

        println("Check Desktop Resources Path……")

        if (!resourcePath.exists()) {
            println("${resourcePath.absolutePath} not exists, create……")
            mkdir(resourcePath)
        }

        val runTestResult = runCommand("g++ --version")
        if (!runTestResult.first) {
            println("Error: Not find command g++, Please install it and add to your system environment path")
            println(runTestResult.second)
            return@doLast
        }

        val command = "g++ ${cppFilePath.absolutePath} -o ${binFilePath.absolutePath} -shared -fPIC -I ${includePath1.absolutePath} -I ${includePath2.absolutePath}"

        println("running command $command……")

        val compilerResult = runCommand(command)

        if (!compilerResult.first) {
            println("Command run fail: ${compilerResult.second}")
            return@doLast
        }

        println(compilerResult.second)

        println("compile jni for desktop all done")

    }
}

tasks.named("compileKotlinJvm") {
    dependsOn("compileJni")
}

fun runCommand(command: String, timeout: Long = 120): Pair<Boolean, String> {
    val process = ProcessBuilder()
        .command(command.split(" "))
        .directory(rootProject.projectDir)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
    process.waitFor(timeout, TimeUnit.SECONDS)
    val result = process.inputStream.bufferedReader().readText()
    val error = process.errorStream.bufferedReader().readText()
    return if (error.isBlank()) {
        Pair(true, result)
    }
    else {
        Pair(false, error)
    }
}
