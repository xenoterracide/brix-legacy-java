group = "com.xenoterracide"
version = "0.1.0-SNAPSHOT"

plugins {
  `kotlin-dsl`
}

kotlinDslPluginOptions {
  experimentalWarning.set(false)
}

repositories {
  gradlePluginPortal()
}

dependencyLocking {
  lockAllConfigurations()
}

dependencies {
  implementation("com.diffplug.spotless:spotless-plugin-gradle:5.+")
  implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.+")
  implementation("net.ltgt.gradle:gradle-errorprone-plugin:1.+")
  implementation("org.springframework.boot:spring-boot-gradle-plugin:2.+")
  implementation("org.checkerframework:checkerframework-gradle-plugin:0.+")
}
