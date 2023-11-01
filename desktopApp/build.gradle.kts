import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
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
    description = "compile jni binary file for desktop"

    val resourcePath = File(rootProject.projectDir, "desktopApp/resources/common/lib/")
    val binFilePath = File(resourcePath, "nativelib.bin")
    val cppFileDirectory = File(rootProject.projectDir, "nativelib/src/main/cpp")
    val cppFilePath = File(cppFileDirectory, "nativelib.cpp")

    // 指定输入、输出文件，用于增量编译
    inputs.dir(cppFileDirectory)
    outputs.file(binFilePath)

    doLast {
        project.logger.info("compile jni for desktop running……")

        val jdkFile = org.gradle.internal.jvm.Jvm.current().javaHome
        val systemPrefix: String

        val os: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()

        if (os.isWindows) {
            systemPrefix = "win32"
        }
        else if (os.isMacOsX) {
            systemPrefix = "darwin"
        }
        else if (os.isLinux) {
            systemPrefix = "linux"
        }
        else {
            project.logger.error("UnSupport System for compiler cpp, please compiler manual")
            return@doLast
        }

        val includePath1 = jdkFile.resolve("include")
        val includePath2 = includePath1.resolve(systemPrefix)

        if (!includePath1.exists() || !includePath2.exists()) {
            val msg = "ERROR: $includePath2 not found!\nMaybe it's because you are using JetBrain Runtime (Jbr)\nTry change Gradle JDK to another jdk which provide jni support"
            throw GradleException(msg)
        }

        project.logger.info("Check Desktop Resources Path……")

        if (!resourcePath.exists()) {
            project.logger.info("${resourcePath.absolutePath} not exists, create……")
            mkdir(resourcePath)
        }

        val runTestResult = runCommand("g++ --version")
        if (!runTestResult.first) {
            throw GradleException("Error: Not find command g++, Please install it and add to your system environment path\n${runTestResult.second}")
        }

        val command = "g++ ${cppFilePath.absolutePath} -o ${binFilePath.absolutePath} -shared -fPIC -I ${includePath1.absolutePath} -I ${includePath2.absolutePath}"

        project.logger.info("running command $command……")

        val compilerResult = runCommand(command)

        if (!compilerResult.first) {
            throw GradleException("Command run fail: ${compilerResult.second}")
        }

        project.logger.info(compilerResult.second)

        project.logger.lifecycle("compile jni for desktop all done")
    }
}

gradle.projectsEvaluated {
    tasks.named("prepareAppResources") {
        dependsOn("compileJni")
    }
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
