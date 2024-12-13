/** Java tool chain settings for JVM **/
object JavaToolChain {
    const val JAVA_VERSION = "17"
    val JAVA_VENDOR: JvmVendorSpec = JvmVendorSpec.AMAZON
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.compose.compiler)
    kotlin("kapt")
}

android {
    namespace = "dev.forcecodes.albertsons.randomuser"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.forcecodes.albertsons.randomuser"
        minSdk = 27
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
                "proguard-rules.pro",
            )
        }
    }
//    kotlin.jvmToolchain {
//        languageVersion = JavaLanguageVersion.of(JavaToolChain.JAVA_VERSION)
//        vendor = JavaToolChain.JAVA_VENDOR
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    buildFeatures {
        viewBinding = true
        compose = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
    kapt {
        correctErrorTypes = true
    }
}

dependencies {

    implementation(project(":data"))
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.compose.material)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // Add Compose Runtime dependency
    implementation(libs.androidx.runtime)

    implementation(libs.coil.compose)
    implementation(libs.coil.okhttp)

    implementation(libs.toggle)
}
