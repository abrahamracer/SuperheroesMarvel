buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}
