plugins {
  id("brix.java-convention")
}

dependencies {
  annotationProcessor(immutables.processor)
  compileOnly(immutables.annotations)
  implementation("org.apache.commons:commons-lang3")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("org.springframework:spring-context")
  implementation(libs.pebble)
}
