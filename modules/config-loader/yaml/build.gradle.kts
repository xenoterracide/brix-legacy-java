plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(projects.configLoader.api)
  implementation(projects.cli.api)
  testImplementation(testFixtures(projects.cli.api))
  testRuntimeOnly(projects.util.test)
  testRuntimeOnly(projects.util.file)

  implementation(libs.vavr)

  implementation("com.fasterxml.jackson.core:jackson-core")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  runtimeOnly("com.fasterxml.jackson.core:jackson-databind")
  implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")

  implementation(spring.context)
  testImplementation(spring.bundles.test)
  testRuntimeOnly("com.jayway.jsonpath:json-path")
}
