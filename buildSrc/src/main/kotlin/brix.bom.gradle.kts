plugins {
  java
}

configurations.configureEach {
  resolutionStrategy {
    componentSelection {
      all {
        val nonRelease = Regex("^[\\d.]+-(M|ea|beta).*$")
        if (candidate.version.matches(nonRelease)) reject("no pre-release")
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
