plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.cpit490project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.cpit490project"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // AndroidX essentials
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Networking: Volley
    implementation("com.android.volley:volley:1.2.1")

    // Image loading: Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // RecyclerView for the horizontal offers list & grid of movies
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // CardView for wrapping each offer thumbnail
    implementation("androidx.cardview:cardview:1.0.0")
}
