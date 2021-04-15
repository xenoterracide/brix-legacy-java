plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  testFixturesAnnotationProcessor("org.immutables:value")
  testFixturesCompileOnly("org.immutables:value-annotations")
  testFixturesImplementation("io.vavr:vavr")
  testFixturesImplementation("org.apache.commons:commons-lang3")
}
