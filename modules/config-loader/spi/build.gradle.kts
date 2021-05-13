plugins {
  id("brix.java-convention")
}

dependencies {
  implementation(project(":config-loader:api"))
  implementation(project(":cli:api"))
  testImplementation(testFixtures(project(":cli:api")))
  annotationProcessor(immutables.processor)
  compileOnly(immutables.annotations)
  implementation(libs.vavr)
  implementation("org.apache.commons:commons-lang3")
  implementation(libs.tika)
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("org.springframework:spring-context")
  testImplementation("org.springframework:spring-test")
  implementation("org.springframework.boot:spring-boot-autoconfigure")
  implementation(libs.pebble)
}
