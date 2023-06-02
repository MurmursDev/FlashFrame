plugins {
    idea
    base
}

tasks.register("buildOpenAPI",Exec::class) {
    workingDir("OpenAPI")
    commandLine("gradle", "build")
}

tasks.register("buildWebapp", Exec::class) {
    dependsOn("buildOpenAPI")
    workingDir("Webapp")
    commandLine("gradle", "build")
}

tasks.register("buildServer", Exec::class) {
    dependsOn("buildOpenAPI")
    commandLine("gradle", "Server:build", "-Dprod=true")
}

tasks.register("buildCDK", Exec::class) {
    dependsOn("buildWebapp", "buildServer")
    workingDir("CDK")
    commandLine("gradle", "build")
}


tasks.build {
    dependsOn("buildCDK")
    doLast {
        copy {
            from("$projectDir/OpenAPI/build/generated/openapi/client/dist")
            into("$projectDir/Webapp/generated/flashframe-client")
        }
    }
}
