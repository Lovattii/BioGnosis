plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myplant"
    compileSdk {
        version = release(36)
    }

    sourceSets.getByName("main") {
        res.srcDirs("src/main/res-imagens-of-plants")
    }

    defaultConfig {
        applicationId = "com.example.myplant"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    val room_version = "2.6.1"

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (project(":PincelGraphics"))
    implementation (project(":MeowBottomNavigation"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
    implementation("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5")
    implementation("org.eclipse.paho:org.eclipse.paho.android.service:1.1.1")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("androidx.room:room-runtime:${room_version}")
    annotationProcessor("androidx.room:room-compiler:$room_version")
}
