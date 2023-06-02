rootProject.name = "FlashFrame"

mkdir("OpenAPI/build/generated/openapi/api")
includeBuild("OpenAPI")
includeBuild("OpenAPI/build/generated/openapi/api") {
    if (file("OpenAPI/build/generated/openapi/api/build.gradle.kts").exists()) {
        dependencySubstitution {
            substitute(module("dev.murmurs.flashframe:api")).using(project(":"))
        }
    }
}
includeBuild("Server")
includeBuild("Webapp")
includeBuild("CDK")
