// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
//    id("com.android.application") version "8.1.2" apply false
//    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
//    id("com.google.dagger.hilt.android") version "2.50" apply false
//    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.google.dagger.hilt.android) apply false
//    id("com.google.dagger.hilt.android") version "2.50" apply false
    alias(libs.plugins.ksp) apply false
//    id("com.google.devtools.ksp") version libs.versions.ksp apply false
}

//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:4.0.2")
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
//        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50")
//    }
//}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
//task clean(type: Delete) {
//    delete(rootProject.buildDir)
//}