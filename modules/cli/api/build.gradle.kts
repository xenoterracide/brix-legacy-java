plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  testFixturesAnnotationProcessor(libs.immutables.core)
  testFixturesCompileOnly(libs.immutables.annotations)
  testFixturesImplementation(libs.vavr)
  testFixturesImplementation(libs.jackson.databind)
  testFixturesImplementation(libs.commons.lang)
}
