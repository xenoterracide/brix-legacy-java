plugins {
  id("brix.java-convention")
}

dependencies {
  runtimeOnly(projects.util.file)
  api(projects.configLoader.api)
  implementation(projects.cli.api)
  implementation(projects.util.lang)
  testImplementation(testFixtures(projects.cli.api))
  testRuntimeOnly(projects.util.test)
  testRuntimeOnly(projects.configLoader.yaml)

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
