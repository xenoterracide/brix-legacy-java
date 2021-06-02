plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  implementation(projects.util.lang)
  implementation(libs.commons.io)
  implementation(libs.spring.context)
}
