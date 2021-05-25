plugins {
  id("brix.java-convention")
}

dependencies {
  implementation(projects.cli.api)
  api(libs.tika)
  implementation(commons.lang)
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation(spring.context)
  implementation(libs.pebble)
  annotationProcessor(immutables.processor)
  compileOnly(immutables.annotations)
}
