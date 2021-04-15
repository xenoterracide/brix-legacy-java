plugins {
  `java-base`
}

configurations.configureEach {
  resolutionStrategy {
    componentSelection {
      all {
        val nonRelease = Regex("^[\\d.]+-(M|ea).*$")
        if (candidate.version.matches(nonRelease)) reject("no milestone version")
      }
    }
    eachDependency {
      when (requested.group) {
        "org.immutables" -> useVersion("2.+")
        "org.checkerframework" -> useVersion("3.+")
        "io.vavr" -> useVersion("0.+")
      }
      if (requested.module.toString() == "org.springframework.boot:spring-boot-starter-parent") {
        useVersion("2.+")
      }
    }
  }
}

dependencies {
  modules {
    module("org.springframework.boot:spring-boot-starter-logging") {
      replacedBy("org.springframework.boot:spring-boot-starter-log4j2")
    }
  }
}
