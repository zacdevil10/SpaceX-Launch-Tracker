package uk.co.zac_h.spacex.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/** Turns on Compose for an Android module. Apply it after the application or library plugin. */
class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.getByType<CommonExtension>().buildFeatures.compose = true
    }
}
