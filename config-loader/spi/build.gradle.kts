plugins {
  id("brix.java-convention")
}

dependencies {
  implementation(project(":config-loader:api"))
  implementation(project(":cli:api"))
  testImplementation(testFixtures(project(":cli:api")))
  annotationProcessor("org.immutables:value")
  compileOnly("org.immutables:value-annotations")
  implementation("io.vavr:vavr")
  implementation("org.apache.commons:commons-lang3")
  implementation("org.apache.tika:tika-core")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("org.springframework:spring-context")
  testImplementation("org.springframework:spring-test")
  implementation("org.springframework.boot:spring-boot-autoconfigure")
  implementation("io.pebbletemplates:pebble")
}
