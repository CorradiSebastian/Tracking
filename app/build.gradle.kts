import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    //id ("kotlin-android-extensions")
}

android {
    namespace = "com.sebastiancorradi.track"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sebastiancorradi.track"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.01"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val mapsKey: String = gradleLocalProperties(rootDir).getProperty("MAPS_API_KEY")
        manifestPlaceholders["MAPS_API_KEY"] =  mapsKey
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("androidx.lifecycle:lifecycle-service:2.6.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //androidX service extension
    implementation("androidx.core:core:1.12.0")
    //navigation
    val navVersion = "2.7.5"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-android-compiler:2.47")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")

    implementation("androidx.hilt:hilt-work:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    //ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    //lifecycleScope for the service to run corroutines
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))

    //TODO probably I will not need this
    // Google Sign In SDK
   // implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.gms:play-services-auth")
    implementation("com.google.firebase:firebase-analytics")

    // Firebase SDK
    implementation(platform("com.google.firebase:firebase-bom:32.0.0"))
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Firebase UI Library
    //TODO probably I will not need this
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")


    //Firebase RTDM
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database-ktx")


    implementation("com.firebaseui:firebase-ui-database:8.0.2")

    //Location Permissions
    implementation ("com.google.accompanist:accompanist-permissions:0.28.0")

    // Preferences DataStore (SharedPreferences like APIs)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Navigation
    implementation("androidx.navigation:navigation-compose:2.7.6")

    //Bottom navigation baR?
    implementation("androidx.compose.material:material:1.5.4")

    //constraint layout
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //maps
    implementation ("com.google.maps.android:maps-compose:2.11.4")
    implementation ("com.google.android.gms:play-services-maps:18.1.0")

    implementation ("androidx.navigation:navigation-compose:2.7.7")
}