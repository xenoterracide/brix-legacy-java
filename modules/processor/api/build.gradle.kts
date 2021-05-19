plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  compileOnly(immutables.annotations)
  implementation("org.apache.commons:commons-lang3")
}
