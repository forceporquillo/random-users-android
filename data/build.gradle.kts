plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

/** Java tool chain settings for JVM **/
object JavaToolChain {
    const val JAVA_VERSION = "17"
    val JAVA_VENDOR: JvmVendorSpec = JvmVendorSpec.AMAZON
}

android {
    namespace = "dev.forcecodes.albertsons.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 27

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    implementation(project(":core"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.core.android)
    implementation(project(":domain"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

//    api(libs.dagger)
//    ksp(libs.dagger.annotation.processor)

    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)

    implementation(libs.kotlin.coroutines)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.paging)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.paging.runtime)

    // I am running on M3 Pro device
    ksp ("org.xerial:sqlite-jdbc:3.36.0")

}
