plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.avv.superheroesmarvel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.avv.superheroesmarvel"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx.v190)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Para retrofit y Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Adicional para el interceptor
    implementation(libs.logging.interceptor)

    // Glide y Picasso
    implementation(libs.glide)
    implementation(libs.picasso)

    // Para las corrutinas
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Imágenes con bordes redondeados
    implementation(libs.roundedimageview)

    // Para usar Youtube Player API
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation(libs.firebase.auth.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Aplica el plugin de Google Services al final del archivo
apply(plugin = "com.google.gms.google-services")

