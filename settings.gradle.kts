rootProject.name = "brix"
enableFeaturePreview("VERSION_CATALOGS")
include(
  "util",
  "cli:api",
  "config-loader:spi",
  "config-loader:api",
  "config-loader:yaml",
  "app"
)

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      alias("vavr").to("io.vavr:vavr:0.+")
      alias("pebble").to("io.pebbletemplates:pebble:3.+")
      alias("commons-io").to("commons-io:commons-io:2.+")
      alias("tika").to("org.apache.tika:tika-core:1.+")
    }
    create("spring") {
      alias("platform").to("org.springframework.boot:spring-boot-starter-parent:2.+")
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
  }
}
