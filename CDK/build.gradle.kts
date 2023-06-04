plugins {
    application
    kotlin("jvm") version "1.8.0"
}

val cdkVersion = "2.79.1"
val structVersion = "10.2.28"


application {
    mainClass.set("dev.murmurs.flashframe.cdk.CdkAppKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.amazon.awscdk:aws-cdk-lib:$cdkVersion")
    implementation("software.constructs:constructs:$structVersion")
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("com.google.guava:guava:31.1-jre")
}

tasks.register("synth", Exec::class) {
    commandLine("cdk", "synth")
}

tasks.build {
    dependsOn("synth")
}

