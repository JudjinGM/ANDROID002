import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.symbol.processing)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.androidx.navigation.safeargs.kotlin)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.judjingm.android002"
    compileSdk = 34

    androidResources {
        generateLocaleConfig = true
    }

    defaultConfig {
        val localProperties = gradleLocalProperties(rootDir, providers)

        applicationId = "com.judjingm.android002"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            type = "String",
            name = "TMDB_ACCESS_TOKEN",
            value = "\"${localProperties.getProperty("tmdbAccessToken")}\""
        )
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
        buildConfig = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // ViewModel + Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)

    //Fragment
    implementation(libs.androidx.fragment.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.firebase.messaging)
    ksp(libs.hilt.android.compiler)

    // Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.room.compiler)
    ksp(libs.androidx.room.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Glide
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    //Crypto
    implementation(libs.crypto)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}