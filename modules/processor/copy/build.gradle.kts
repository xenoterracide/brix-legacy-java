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
  implementation(projects.service.console)

  implementation(libs.spring.context)

  testImplementation(projects.util.test)
  testImplementation(libs.bundles.spring.test)
}
