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
  implementation(libs.bundles.jackson.config)
  implementation(libs.jackson.dataformat.yaml)
  runtimeOnly(libs.jackson.databind)

  implementation(libs.spring.context)
  testImplementation(libs.bundles.spring.test)
  testRuntimeOnly("com.jayway.jsonpath:json-path")
}
