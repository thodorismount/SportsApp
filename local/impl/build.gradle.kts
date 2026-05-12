plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.kaizen.sportsapp.local"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":local:api"))

    api(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
