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
  /*
  // add all classes from brix
  parent?.subprojects?.filterNot { p -> p == project }?.forEach { p ->
    println(p)
    if (p.plugins.hasPlugin("java-platform")) {
      from(p.sourceSets["main"].output.classesDirs) {
        into("BOOT-INFO/classes")
      }
    }
  }
  // filter jars from brix
  exclude { ft ->
    parent
      ?.subprojects
      ?.flatMap { p -> p.tasks }
      ?.filterIsInstance<Jar>()
      ?.any { j -> j.archiveFileName.get() == ft.name }
      ?: false
  }
*/
  launchScript {
    properties(
      mapOf(
        "spring.config.location" to "classpath:application.properties"
      )
    )
  }
}
