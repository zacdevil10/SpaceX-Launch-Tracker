import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.openapi.generator)
}

//region Launch Library models, generated from the OpenAPI schema

// Upstream schema, downloaded from https://ll.thespacedevs.com/2.3.0/schema/?format=json.
// To update: replace this file and rebuild.
val launchLibrarySpec = layout.projectDirectory.file("openapi/launch-library-2.3.0.json")
val launchLibraryRelaxedSpec = layout.buildDirectory.file("openapi/launch-library-relaxed.json")
val launchLibraryOutput = layout.buildDirectory.dir("generated/openapi/launchlibrary")
val launchLibraryGeneratedSources = "build/generated/openapi/launchlibrary/src/main/kotlin"

// The schema marks many fields as required and non-null that the API does return as null
// (landing, previous_flight, turn_around_time...), which crashes kotlinx.serialization at
// runtime. Strip every `required` list down to just `id`, so ids stay non-null and every other
// generated property is nullable with a default.
val relaxLaunchLibrarySpec = tasks.register("relaxLaunchLibrarySpec") {
    description = "Makes every property in the Launch Library schema except id optional."
    val input = launchLibrarySpec
    val output = launchLibraryRelaxedSpec
    inputs.file(input)
    outputs.file(output)
    doLast {
        fun relax(node: Any?) {
            when (node) {
                is MutableMap<*, *> -> {
                    val required = node["required"]
                    if (required is List<*>) {
                        @Suppress("UNCHECKED_CAST")
                        val schema = node as MutableMap<Any?, Any?>
                        if ("id" in required) schema["required"] = listOf("id") else schema.remove("required")
                    }
                    node.values.forEach(::relax)
                }
                is List<*> -> node.forEach(::relax)
            }
        }

        val spec = groovy.json.JsonSlurper().parse(input.asFile) as Map<*, *>
        relax((spec["components"] as Map<*, *>)["schemas"])
        output.get().asFile.apply {
            parentFile.mkdirs()
            writeText(groovy.json.JsonOutput.toJson(spec))
        }
    }
}

val generateLaunchLibraryModels = tasks.register<GenerateTask>("generateLaunchLibraryModels") {
    description = "Generates Launch Library 2.3.0 models from the OpenAPI schema."
    dependsOn(relaxLaunchLibrarySpec)
    generatorName.set("kotlin")
    library.set("jvm-retrofit2")
    inputSpec.set(launchLibraryRelaxedSpec)
    outputDir.set(launchLibraryOutput)
    ignoreFileOverride.set(layout.projectDirectory.file("openapi/.openapi-generator-ignore").asFile.absolutePath)
    modelPackage.set("uk.co.zac_h.spacex.network.launchlibrary.model")
    packageName.set("uk.co.zac_h.spacex.network.launchlibrary")
    modelNameSuffix.set("Dto")
    // Only the models: the Retrofit service stays hand-written so it can ask for exactly the
    // `mode` and filters each screen needs.
    globalProperties.set(mapOf("models" to "", "modelDocs" to "false", "modelTests" to "false"))
    typeMappings.set(mapOf("URI" to "kotlin.String", "UUID" to "kotlin.String"))
    configOptions.set(
        mapOf(
            "serializationLibrary" to "kotlinx_serialization",
            "dateLibrary" to "string",
            "enumUnknownDefaultCase" to "true",
            "sourceFolder" to "src/main/kotlin"
        )
    )
    generateModelTests.set(false)
    generateModelDocumentation.set(false)
    cleanupOutput.set(true)
}

tasks.named("preBuild") {
    dependsOn(generateLaunchLibraryModels)
}

//endregion

android {
    compileSdk {
        version = release(libs.versions.compileSdk.get().toInt()) {
            minorApiLevel = libs.versions.compileSdkMinor.get().toInt()
        }
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )
            buildConfigField("String", "URL", "\"https://ll.thespacedevs.com/2.3.0/\"")
        }
        debug {
            buildConfigField("String", "URL", "\"https://lldev.thespacedevs.com/2.3.0/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
    namespace = "uk.co.zac_h.spacex.network"

    sourceSets {
        getByName("main") {
            kotlin.directories.add(launchLibraryGeneratedSources)
        }
    }
}

dependencies {
    //Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.converter)

    //OkHttp: pinned via the BOM so Retrofit's transitive 4.x resolves to 5.x
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //Room: the source of truth for Launch Library data
    api(project(":core:database"))

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //Paging
    implementation(libs.androidx.paging)

    testImplementation(libs.junit)
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}
