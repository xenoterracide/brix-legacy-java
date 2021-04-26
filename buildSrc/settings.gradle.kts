rootProject.name = "buildSrc"
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
  versionCatalogs {
    create("spring") {
      alias("platform").to("org.springframework.boot:spring-boot-starter-parent:2.+")
    }
    create("checker") {
      version("checker", "3.+")
      alias("annotations").to("org.checkerframework", "checker-qual").versionRef("checker")
      alias("processor").to("org.checkerframework", "checker").versionRef("checker")
    }
  }
}
