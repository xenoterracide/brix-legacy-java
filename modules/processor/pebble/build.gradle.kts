plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.processor.spi)
  api(projects.processor.api)

  implementation("io.pebbletemplates:pebble:3.+")
  implementation("org.springframework:spring-context")

  testImplementation("org.springframework:spring-test")
  testImplementation("org.springframework.boot:spring-boot-test")
  testImplementation("org.springframework.boot:spring-boot-autoconfigure")
  testImplementation("org.springframework.boot:spring-boot-test-autoconfigure")
}
