plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    // Apply kapt plugin
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.autogestion"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.autogestion"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation("libs.compressor")

    implementation(libs.coil.compose)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)


    implementation(libs.retrofit)
    // GSON
    implementation(libs.converter.gson)
    implementation(libs.okhttp)

    implementation(libs.androidx.material.icons.extended)

// AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.runtime.livedata)

    // AndroidX Testing dependencies
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v350)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material)
    implementation(libs.material3)
    // Additional test dependencies
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.room.testing)

    // Android Room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    kapt(libs.androidx.room.compiler)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    /* // Kotlin standard library
     implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")*/

    // Core ktx - pour des fonctionnalités Kotlin dans Android
    implementation(libs.androidx.core.ktx.v1120)

    // Room runtime
    implementation(libs.androidx.room.runtime.v252)

    // Room KTX - pour utiliser les coroutines et d'autres fonctionnalités Kotlin avec Room
    implementation(libs.androidx.room.ktx)

    // Room Compiler - nécessaire pour la génération de code
    kapt(libs.androidx.room.compiler.v252)

    // Lifecycle KTX - pour des fonctionnalités avancées de gestion du cycle de vie
    implementation(libs.androidx.lifecycle.runtime.ktx.v262)
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)

    // Coroutine - pour utiliser les coroutines Kotlin
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

}

