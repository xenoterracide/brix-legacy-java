plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  testFixturesAnnotationProcessor(immutables.processor)
  testFixturesCompileOnly(immutables.annotations)
  testFixturesImplementation(libs.vavr)
  testFixturesImplementation("com.fasterxml.jackson.core:jackson-databind")
  testFixturesImplementation("org.apache.commons:commons-lang3")
  testFixturesImplementation("org.springframework:spring-test")
  testFixturesImplementation("org.springframework.boot:spring-boot-autoconfigure")
  testFixturesImplementation("org.springframework.boot:spring-boot-test")
}
