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

  compileOnly(libs.immutables.annotations)
  implementation(libs.vavr)
  implementation(libs.commons.lang)
  implementation(libs.tika)
  implementation(libs.jackson.databind)
  implementation(libs.spring.context)
  implementation(libs.spring.boot.autoconfigure)
  implementation(libs.pebble)

  testImplementation(libs.bundles.spring.test)
}
