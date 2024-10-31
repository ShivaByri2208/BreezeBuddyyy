plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.breezebuddyyy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.breezebuddyyy"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {
    // Using version catalog for dependencies managed by libs.versions.toml
    implementation(libs.appcompat)
    implementation(libs.material) // Ensure version matches the catalog, or use an explicit version below
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Explicitly declared dependencies
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.location)
    implementation(libs.recyclerview)
    implementation (libs.cardview)
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)
    implementation(libs.okhttp)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.json)
    implementation(libs.okhttp3.logging.interceptor)
}
