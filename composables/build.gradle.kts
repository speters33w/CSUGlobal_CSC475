plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}

// Add this block to set the Kotlin version
buildscript {
    extra.apply {
        set("kotlin_version", "1.9.0")
    }
}