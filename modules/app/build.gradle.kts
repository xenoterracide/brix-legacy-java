import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "com.xenoterracide.brix"
version = "0.1.0"

plugins {
  id("org.springframework.boot")
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.cli.config)
  runtimeOnly(projects.configLoader.yaml)
  runtimeOnly(projects.processor.pebble)
  implementation(libs.spring.boot.autoconfigure)
  runtimeOnly(libs.spring.boot.starter.core)
  implementation(libs.picocli.core)
  runtimeOnly(libs.picocli.starter)
  testImplementation(libs.spring.core)
}

tasks.withType<BootJar> {
  mainClass.set("com.xenoterracide.brix.Application")
  launchScript {
    properties(
      mapOf(
        "spring.config.location" to "classpath:application.properties"
      )
    )
  }
  layered {
    isEnabled = true
  }
}
