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

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      alias("vavr").to("io.vavr:vavr:0.+")
      alias("pebble").to("io.pebbletemplates:pebble:3.+")
      alias("tika").to("org.apache.tika:tika-core:1.+")
      alias("mockito").to("org.mockito", "mockito-core").withoutVersion()
    }
    create("picocli") {
      version("picocli", "4.+")
      alias("core").to("info.picocli", "picocli").versionRef("picocli")
      alias("starter").to("info.picocli", "picocli-spring-boot-starter").versionRef("picocli")
    }
    create("commons") {
      alias("io").to("commons-io:commons-io:2.+")
      alias("lang").to("org.apache.commons", "commons-lang3").withoutVersion()
    }
    create("spring") {
      alias("platform").to("org.springframework.boot:spring-boot-starter-parent:2.+")
      alias("context").to("org.springframework", "spring-context").withoutVersion()
      alias("test").to("org.springframework", "spring-test").withoutVersion()
      alias("boot-test-lib").to("org.springframework.boot", "spring-boot-test").withoutVersion()
      alias("boot-autoconfigure").to("org.springframework.boot", "spring-boot-autoconfigure").withoutVersion()
      alias("boot-test-autoconfigure").to("org.springframework.boot", "spring-boot-test-autoconfigure").withoutVersion()
      bundle("test", listOf("test", "boot-test-lib", "boot-test-autoconfigure", "boot-autoconfigure"))
    }
    create("checker") {
      version("checker", "3.+")
      alias("annotations").to("org.checkerframework", "checker-qual").versionRef("checker")
      alias("processor").to("org.checkerframework", "checker").versionRef("checker")
    }
    create("immutables") {
      version("immutables", "2.+")
      alias("processor").to("org.immutables", "value").versionRef("immutables")
      alias("annotations").to("org.immutables", "value-annotations").versionRef("immutables")
    }
    create("jackson") {
      alias("databind").to("com.fasterxml.jackson.core", "jackson-databind").withoutVersion()
    }
    create("ep") {
      version("ep", "2.+")
      alias("core").to("com.google.errorprone", "error_prone_core").versionRef("ep")
      alias("annotations").to("com.google.errorprone", "error_prone_annotations").versionRef("ep")
    }
  }
}
