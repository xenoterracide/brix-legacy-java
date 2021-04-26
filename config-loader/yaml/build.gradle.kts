plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(project(":config-loader:api"))
  implementation(project(":config-loader:spi"))
  implementation(project(":cli:api"))
  testImplementation(testFixtures(project(":cli:api")))
  testRuntimeOnly(testFixtures(project(":util")))

  implementation("org.apache.commons:commons-lang3")
  implementation(libs.commons.io)
  implementation(libs.tika)
  implementation(libs.vavr)

  implementation("com.fasterxml.jackson.core:jackson-core")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
  runtimeOnly("com.fasterxml.jackson.core:jackson-databind")
  runtimeOnly("com.fasterxml.jackson.module:jackson-module-parameter-names")

  implementation("org.springframework:spring-context")

  testImplementation("org.springframework.boot:spring-boot-test")
  testImplementation("org.springframework.boot:spring-boot-autoconfigure")
}
