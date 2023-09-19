plugins {
    kotlin("jvm") version "1.9.10"
}

group = "com.appdav"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin{
    compilerOptions{
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks.compileTestKotlin{
    compilerOptions{
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

kotlin {
    jvmToolchain(11)
}
