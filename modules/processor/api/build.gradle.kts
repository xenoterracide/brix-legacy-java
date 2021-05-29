plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  api(projects.configLoader.api)
}
