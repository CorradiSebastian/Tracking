// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    // The google-services plugin is required to parse the google-services.json file
    id("com.google.gms.google-services") version "4.4.0" apply false
}
