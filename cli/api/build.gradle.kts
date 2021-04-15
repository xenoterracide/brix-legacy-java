plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  testFixturesAnnotationProcessor("org.immutables:value:2.+")
  testFixturesCompileOnly("org.immutables:value-annotations:2.+")
  testFixturesImplementation("io.vavr:vavr:0.+")
  testFixturesImplementation("org.apache.commons:commons-lang3")
}
