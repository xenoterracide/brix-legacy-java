plugins {
  id("brix.java-convention")
}

dependencies {
  implementation(project(":config-loader:api"))
  implementation(project(":cli:api"))
  annotationProcessor("org.immutables:value")
  compileOnly("org.immutables:value-annotations")
  implementation("io.vavr:vavr")
  implementation("org.apache.commons:commons-lang3")
  implementation("org.apache.tika:tika-core")
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation("org.springframework:spring-context")
  runtimeOnly("io.pebbletemplates:pebble-spring-boot-starter")
  implementation("io.pebbletemplates:pebble")
}
