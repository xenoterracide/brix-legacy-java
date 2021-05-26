import java.nio.file.Files

rootProject.name = "brix"
enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

file("modules").walkTopDown().maxDepth(3).onEnter { f ->
  f.isDirectory
}.asIterable()
  .filter { f -> Files.exists(f.toPath().resolve("build.gradle.kts")) }
  .map { f -> f.toPath().toString() }
  .map { path -> path.substringAfter("/modules") }
  .forEach { relPath ->
    val proj = relPath.replace("/", ":")
    include(proj)
    project(proj).projectDir = file("modules$relPath")
  }
