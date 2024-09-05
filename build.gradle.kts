// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript{
    dependencies {
        //classpath(libs.hilt.android.gradle.plugin)
        //classpath("com.google.dagger:hilt-android-gradle-plugin:2.44")
    }
}

plugins {
    val room_version = "2.6.1"

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("androidx.room") version "$room_version" apply false
}