package uk.co.zac_h.spacex.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

/** Settings every Android module shares: SDK levels, Java 17 and the Kotlin JVM target. */
internal fun Project.configureKotlinAndroid(extension: CommonExtension) {
    val compileSdkVersion = libs.intVersion("compileSdk")
    val compileSdkMinorVersion = libs.intVersion("compileSdkMinor")

    extension.apply {
        compileSdk {
            version = release(compileSdkVersion) {
                minorApiLevel = compileSdkMinorVersion
            }
        }

        defaultConfig.minSdk = libs.intVersion("minSdk")
        defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        compileOptions.sourceCompatibility = JavaVersion.VERSION_17
        compileOptions.targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
}
