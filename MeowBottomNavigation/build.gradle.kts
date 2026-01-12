plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android") version "1.9.24"
    //id("org.jetbrains.kotlin.kapt") version "1.9.24"
}

android {
    namespace = "com.etebarian.meowbottomnavigation"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        // REMOVIDO: create("debug")
        // MOTIVO: O Android já cria o debug automaticamente. Removendo isso, o erro de duplicidade some.
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.dynamicanimation:dynamicanimation:1.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}