plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.processor.spi)
  api(projects.processor.api)
  implementation(projects.configLoader.api)
  implementation(projects.cli.api)
  implementation(projects.util)

  implementation(commons.lang)
  implementation(libs.pebble)
  implementation(spring.context)
  implementation(libs.vavr)

  testImplementation(projects.testUtil)
  testImplementation(spring.bundles.test)
  testImplementation(libs.mockito)
  testImplementation(commons.io)
}
