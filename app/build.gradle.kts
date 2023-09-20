plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.falaai"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.falaai"
        minSdk = 23
        targetSdk = 33
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

    viewBinding {
        enable = true
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.0-alpha02")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.2")
    implementation ("io.coil-kt:coil:2.2.2")
    implementation ("com.aallam.openai:openai-client-bom:3.4.0")
    implementation ("com.aallam.openai:openai-client")
    runtimeOnly ("io.ktor:ktor-client-okhttp")
}