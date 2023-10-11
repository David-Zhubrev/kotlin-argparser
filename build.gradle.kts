import java.util.Properties

plugins {
    kotlin("jvm") version "1.9.10"
    `maven-publish`
}

group = "com.appdav"
version = "0.1.2-SNAPSHOT"

fun repoProperty(name: String): String {
    return with(Properties()) {
        load(file("repo.properties").inputStream())
        get(name) as String
    }
}

publishing {
    publications.create("main", MavenPublication::class.java) {
        repositories {
            maven(repoProperty("url")){
                credentials{
                    username = repoProperty("username")
                    password = repoProperty("password")
                }
            }
            artifact("$buildDir\\libs\\${project.name}-$version.jar")
            artifact("$buildDir\\libs\\${project.name}-$version-sources.jar"){
                classifier = "sources"
            }
        }


    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.compileKotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

tasks.compileTestKotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

kotlin {
    jvmToolchain(11)
}
