plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(projects.configLoader.api)
  implementation(projects.configLoader.spi)
  implementation(projects.cli.api)
  testImplementation(testFixtures(projects.cli.api))
  testRuntimeOnly(projects.testUtil)

  implementation(commons.lang)
  implementation(commons.io)
  implementation(projects.util)
  implementation(libs.vavr)

  implementation("com.fasterxml.jackson.core:jackson-core")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  runtimeOnly("com.fasterxml.jackson.core:jackson-databind")
  implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")

  implementation(spring.context)
  testImplementation(spring.bundles.test)
  testRuntimeOnly("com.jayway.jsonpath:json-path")
}
