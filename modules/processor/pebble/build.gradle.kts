plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.util.lang)
  api(projects.processor.api)
  implementation(projects.configLoader.api)
  implementation(projects.cli.api)
  implementation(projects.service.file)
  runtimeOnly(projects.service.console)

  implementation(libs.commons.lang)
  implementation(libs.pebble)
  implementation(libs.spring.context)
  implementation(libs.vavr)

  testImplementation(projects.util.test)
  testImplementation(libs.bundles.spring.test)
  testImplementation(libs.mockito)
  testImplementation(libs.commons.io)
}
