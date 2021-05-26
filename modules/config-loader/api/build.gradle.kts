plugins {
  id("brix.java-convention")
}

dependencies {
  implementation(projects.cli.api)
  api(libs.tika)
  implementation(libs.commons.lang)
  implementation(libs.jackson.databind)
  implementation(libs.spring.context)
  implementation(libs.pebble)
  annotationProcessor(libs.immutables.core)
  compileOnly(libs.immutables.annotations)
}
