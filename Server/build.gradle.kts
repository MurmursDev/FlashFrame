import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

var log4j2Version = "2.19.0"
var kotlinVersion = "1.8.0"
var junitVersion = "5.9.2"


plugins {
    java
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.8.21"
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

val isDev = System.getProperty("prod") != "true"
val isProd = System.getProperty("prod") == "true"
configurations {
    implementation {
        exclude(module = "spring-boot-starter-logging")
    }
    runtimeClasspath {
        if (isProd) {
            exclude(module = "spring-boot-starter-tomcat")
        }
    }
}

if (isProd) {
    sourceSets {
        main {
            kotlin {
                exclude { it.name == "WebSecurityConfig.kt" }
            }
        }
    }
}

dependencies {
    implementation("dev.murmurs.flashframe:api:0.0.1")

    //AWS Lambda
    implementation("com.amazonaws:aws-lambda-java-core:1.2.2")
    implementation("com.amazonaws:aws-lambda-java-events:3.11.1")
    runtimeOnly("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    implementation("com.amazonaws.serverless:aws-serverless-java-container-springboot3:2.0.0-M1")

    //AWS DDB
    implementation("software.amazon.awssdk:dynamodb:2.20.26")
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.20.26")

    //AWS Cognito Verify
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("com.auth0:jwks-rsa:0.22.0")


    //Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    if (isDev) {
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
        implementation("org.springframework.boot:spring-boot-starter-security")
    }

    //JSON
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //Kotlin
    implementation(kotlin("stdlib:$kotlinVersion"))
    implementation(kotlin("reflect:$kotlinVersion"))

    //Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-core:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-api:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-jul:$log4j2Version")
    implementation("org.apache.logging.log4j:log4j-jcl:$log4j2Version")

    //Common Utils
    implementation("com.google.guava:guava:31.1-jre")

    //Testing
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    //Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    if(isDev) {
        testImplementation("org.springframework.security:spring-security-test")
    }
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
}

tasks.getByName<Test>("test") {
    systemProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager")
    useJUnitPlatform()
}


java.sourceCompatibility = JavaVersion.VERSION_17

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JvmTarget.JVM_17.target
}

tasks.create<Zip>("buildZip") {
    from(tasks.compileJava)
    from(tasks.compileKotlin)
    from(tasks.processResources)
    into("lib") {
        from(configurations.runtimeClasspath)
    }
}

tasks.build {
    finalizedBy("buildZip")
}
