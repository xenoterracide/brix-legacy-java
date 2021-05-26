plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(projects.cli.api)
  implementation(projects.dispatch)
  compileOnly(libs.errorprone.core)
  implementation(libs.spring.context)
  implementation(libs.commons.lang)
  implementation(libs.picocli.core)
}
