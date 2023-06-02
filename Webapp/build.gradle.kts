plugins {
  base
}
tasks.register("npmInstall", Exec::class) {
  commandLine("npm", "install")
}

tasks.register("npmRunBuild", Exec::class) {
  commandLine("npm", "run", "build")
}
tasks.build {
  dependsOn("npmInstall", "npmRunBuild")
  tasks.getByName("npmRunBuild").mustRunAfter("npmInstall")
}
