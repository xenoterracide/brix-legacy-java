plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(projects.cli.api)
  implementation(projects.dispatch)
  implementation(spring.context)
  implementation(commons.lang)
  implementation(picocli.core)
}
