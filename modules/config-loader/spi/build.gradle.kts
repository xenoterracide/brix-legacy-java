plugins {
  id("brix.java-convention")
}

dependencies {
  runtimeOnly(projects.util)
  implementation(projects.configLoader.api)
  implementation(projects.cli.api)
  testImplementation(testFixtures(projects.cli.api))
  annotationProcessor(immutables.processor)
  compileOnly(immutables.annotations)
  implementation(libs.vavr)
  implementation(commons.lang)
  implementation(libs.tika)
  implementation(jackson.databind)
  implementation(spring.context)
  testImplementation(spring.test)
  implementation(spring.boot.autoconfigure)
  implementation(libs.pebble)
}
