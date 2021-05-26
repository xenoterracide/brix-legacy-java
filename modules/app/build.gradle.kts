group = "com.xenoterracide.brix"
version = "0.1.0"

plugins {
  id("org.springframework.boot")
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.cli.config)
  implementation(libs.spring.boot.autoconfigure)
  implementation(libs.picocli.core)
  runtimeOnly(libs.picocli.starter)
}
