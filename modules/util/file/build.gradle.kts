plugins {
  id("brix.bom")
  id("brix.java-convention")
}

dependencies {
  compileOnly("biz.aQute.bnd:bndlib:2.+") // missing transient dependency of tika
  implementation(libs.vavr)
  api(libs.tika)
  implementation(spring.context)
}
