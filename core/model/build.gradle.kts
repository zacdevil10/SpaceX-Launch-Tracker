import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// App-owned models shared by every form factor. Pure Kotlin: no Android or Compose, so phone, Wear,
// TV and XR code can all depend on it.
plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    testImplementation(libs.junit)
}
