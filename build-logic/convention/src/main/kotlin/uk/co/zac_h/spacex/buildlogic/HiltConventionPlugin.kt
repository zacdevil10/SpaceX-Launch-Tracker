package uk.co.zac_h.spacex.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/** Hilt through KSP: applies both plugins and adds the runtime and the compiler. */
class HiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("com.google.dagger.hilt.android")

        dependencies {
            add("implementation", libs.findLibrary("hilt.android").get())
            add("ksp", libs.findLibrary("hilt.compiler").get())
        }
    }
}
