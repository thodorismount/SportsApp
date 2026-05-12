plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.kaizen.sportsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kaizen.sportsapp"
        minSdk = 23
        targetSdk = 36
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
    buildFeatures {
        compose = true
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":domain:api"))
    implementation(project(":domain:impl"))
    implementation(project(":remote:api"))
    implementation(project(":remote:impl"))
    implementation(project(":local:api"))
    implementation(project(":local:impl"))
    implementation(project(":data"))
    implementation(project(":presentation"))

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Koin
    implementation(libs.koin.android)

    // Ktor (for HttpClient construction in DI)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    // Room (for Room.databaseBuilder in DI)
    implementation(libs.room.runtime)

    // Serialization (for Json { } in DI)
    implementation(libs.kotlinx.serialization.json)

    debugImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
