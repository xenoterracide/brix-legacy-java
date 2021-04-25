plugins {
  id("brix.java-convention")
}

dependencies {
  annotationProcessor("org.immutables:value")
  compileOnly("org.immutables:value-annotations")
  implementation("org.apache.commons:commons-lang3")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("org.springframework:spring-context")
  runtimeOnly("io.pebbletemplates:pebble-spring-boot-starter")
  implementation("io.pebbletemplates:pebble")
}
