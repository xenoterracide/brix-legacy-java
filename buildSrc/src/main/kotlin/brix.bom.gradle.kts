plugins {
  `java-base`
}

configurations.all {
  resolutionStrategy {
    componentSelection {
      all {
        val nonRelease = Regex("^[\\d.]+-(M|ea).*$")
        if (candidate.version.matches(nonRelease)) reject("no milestone version")
      }
    }
    eachDependency {
      if (requested.group === "org.immutables") useVersion("2.+")
      if (requested.group === "org.checkerframework") useVersion("3.+")
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
