plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.mountouris.sportsapp.remote"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":remote:api"))
    implementation(project(":domain:api"))

    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}
