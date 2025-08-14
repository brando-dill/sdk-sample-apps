/*
 * Copyright (c) 2024 - 2025 Ping Identity Corporation. All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.pingidentity.samples.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.pingidentity.samples.app"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["appRedirectUriScheme"] = "myapp" // TODO update this with redirect Uri schema.

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.jks")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
}

dependencies {

    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)

    // DaVinci SDK
    implementation(libs.davinci)
    implementation(libs.foundation.android)
    implementation(libs.foundation.oidc)
    implementation(libs.foundation.oidc.browser)

    // Social Login
    implementation(libs.external.idp)

    //To enable Native Google Sign-In, fall back to browser if Google SDK is not available.
    implementation(libs.googleid)
    implementation(libs.androidx.credentials.play.services.auth)
    // Google native sign-on SDK for Android
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

// Needed by Android 13 and earlier
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.facebook.android:facebook-login:18.0.3")

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)

    // Android Studio Preview support
    implementation(libs.androidx.core.splashscreen)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
}
