/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.yaml;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.cli.api.TestCliConfiguration;
import com.xenoterracide.brix.configloader.api.ConfigLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;

@SpringBootTest
class YamlConfigLoaderTest {

  @Autowired ConfigLoader loader;

  @Autowired Path foundConfig;

  @Test
  void load() {
    this.loader.load( foundConfig );
  }

  @TestConfiguration
  static class Config {

    @Bean
    CliConfiguration cliConfiguration() {
      return TestCliConfiguration.builder()
        .classLoader( this.getClass().getClassLoader() )
        .build();
    }
  }
}
