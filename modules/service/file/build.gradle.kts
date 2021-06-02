plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  compileOnlyApi(libs.bndlib)
  implementation(libs.vavr)
  api(libs.tika)
  implementation(libs.spring.context)
}
