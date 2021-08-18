plugins {
    kotlin("js")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":commons"))
    implementation(project(":graphicsgeo"))
    implementation(project(":htmlcanvas"))
    implementation(project(":htmltoolbar"))
    implementation(project(":keycommand"))
    implementation(project(":lifecycle"))
    implementation(project(":livedata"))
    implementation(project(":monoboard"))
    implementation(project(":monobitmap"))
    implementation(project(":monobitmap-manager"))
    implementation(project(":shape"))
    implementation(project(":shape-clipboard"))
    implementation(project(":shape-selection"))
    implementation(project(":shape-serialization"))
    implementation(project(":statemanager"))
    implementation(project(":store-manager"))

    testImplementation(kotlin("test-js"))
}
val compilerType: org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType by ext
kotlin {
    js(compilerType) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport.enabled = true
                }
            }
        }
        binaries.executable()
    }
}
