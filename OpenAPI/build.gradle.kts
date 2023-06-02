import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.openapi.generator") version "6.6.0"
    kotlin("jvm") version "1.8.0"
}

repositories {
    mavenCentral()
}

dependencies {
}

tasks.register("generateServerApi", GenerateTask::class) {
    val outputDirPath = "$buildDir/generated/openapi/api"
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/src/main/resources/api.yaml")
    outputDir.set(outputDirPath)
    templateDir.set("$projectDir/src/main/resources/template/kotlin-spring")
    apiPackage.set("dev.murmurs.server.web.api")
    modelPackage.set("dev.murmurs.server.web.model")
    configOptions.set(
        mapOf(
            "serializationLibrary" to "jackson",
            "interfaceOnly" to "true",
            "documentationProvider" to "none",
            "useBeanValidation" to "false",
            "annotationLibrary" to "none",
            "skipDefaultInterface" to "true",
            "useSpringBoot3" to "true",
            "groupId" to "dev.murmurs.flashframe",
            "artifactId" to "api",
            "artifactVersion" to "0.0.1"
        )
    )
    doLast {
        delete("$buildDir/generated/openapi/server/pom.xml")
    }
}

tasks.register("generateAngularClient", GenerateTask::class) {
    val outputDirPath = "$buildDir/generated/openapi/client"
    generatorName.set("typescript-angular")
    inputSpec.set("$projectDir/src/main/resources/api.yaml")
    outputDir.set(outputDirPath)
    configOptions.set(
        mapOf(
            "npmName" to "flashframe-client",
        )
    )
}

tasks.withType<KotlinCompile> {
    dependsOn("generateServerApi", "generateAngularClient")
    kotlinOptions.jvmTarget = JvmTarget.JVM_17.target
}

tasks.register("buildApi", Exec::class) {
    workingDir("$buildDir/generated/openapi/api")
    commandLine("gradle", "build")
}

tasks.register("npmInstall", Exec::class){
    workingDir("$buildDir/generated/openapi/client")
    commandLine("npm", "install")
}

tasks.register("npmRunBuild", Exec::class){
    workingDir("$buildDir/generated/openapi/client")
    commandLine("npm", "run", "build")
}

tasks.register("buildClient") {
    dependsOn("npmInstall", "npmRunBuild")
    tasks.getByName("npmRunBuild").mustRunAfter("npmInstall")
}

tasks.build {
    dependsOn("buildApi", "buildClient")
}
