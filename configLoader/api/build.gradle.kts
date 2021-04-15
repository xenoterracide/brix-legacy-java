plugins {
  id("brix.java-convention")
}

dependencies {
  annotationProcessor("org.immutables:value")
  compileOnly("org.immutables:value-annotations")
}
