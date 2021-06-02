plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(projects.configLoader.api)
  implementation(projects.util.lang)
  runtimeOnly(projects.service.console)
  api(projects.service.file)
  api(projects.util.lang)
  implementation(libs.commons.lang)
  testImplementation(libs.mockito)
  testImplementation(libs.commons.io)
}
