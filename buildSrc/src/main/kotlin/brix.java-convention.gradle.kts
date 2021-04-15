import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  id("brix.java-library")
  checkstyle
  id("com.github.spotbugs")
  id("com.diffplug.spotless")
}

repositories {
  mavenCentral()
}

dependencyLocking {
  lockAllConfigurations()
}

dependencies {
  implementation(platform("org.springframework.boot:spring-boot-starter-parent"))
  testFixturesImplementation(platform("org.springframework.boot:spring-boot-starter-parent"))

  runtimeOnly("org.springframework.boot:spring-boot-starter-log4j2")

  implementation("org.apache.logging.log4j:log4j-api")
  testFixturesImplementation("org.apache.logging.log4j:log4j-api")

  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testImplementation("org.assertj:assertj-core")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testRuntimeOnly("org.springframework:spring-test")
  testImplementation("org.springframework.boot:spring-boot-test")
}

tasks.test {
  useJUnitPlatform()

  testLogging {
    lifecycle {
      showStandardStreams = true
      displayGranularity = 2
      events.addAll(listOf(TestLogEvent.STARTED, TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED))
    }
  }
  reports {
    html.isEnabled = false
    junitXml.isEnabled = false
  }
}

tasks.withType<Checkstyle>().configureEach {
  isShowViolations = true
  reports {
    html.isEnabled = false
    xml.isEnabled = false
  }
}

fun checkstyleConfig(filename: String): File {
  val path = "config/checkstyle/${filename}"
  val f = file(path)
  return if (f.exists()) f else rootProject.file(path)
}

tasks.named<Checkstyle>("checkstyleMain") {
  configFile = checkstyleConfig("main.xml")
}

tasks.named<Checkstyle>("checkstyleTestFixtures") {
  configFile = checkstyleConfig("test.xml")
}

tasks.named<Checkstyle>("checkstyleTest") {
  configFile = checkstyleConfig("test.xml")
}

spotbugs {
  val config = file("config/spotbugs/exclude.xml")
  if (config.exists()) {
    excludeFilter.set(config)
  }
  effort.set(Effort.MAX)
  reportLevel.set(Confidence.LOW)
}

tasks.withType<SpotBugsTask>().configureEach {
  reports.register("html") {
    enabled = true
  }
}

val copyright = "Copyright © \$YEAR Caleb Cushing."
val license = "Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE"
val licenseSimple = "https://choosealicense.com/licenses/apache-2.0/#"
spotless {
  ratchetFrom = "HEAD"
  java {
    licenseHeader(
      "/*\n" +
        "* $copyright\n" +
        "* $license\n" +
        "* $licenseSimple\n" +
        "*/"
    )
  }
}
