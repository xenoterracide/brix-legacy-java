group = "com.xenoterracide.brix"
version = "0.1.0"

plugins {
  id("org.springframework.boot")
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.cli.config)
  implementation(spring.boot.autoconfigure)
  implementation(picocli.core)
  runtimeOnly(picocli.starter)
}
