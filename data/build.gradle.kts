plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.mountouris.sportsapp.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":domain:api"))
    implementation(project(":remote:api"))
    implementation(project(":local:api"))

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(kotlin("test"))
}
