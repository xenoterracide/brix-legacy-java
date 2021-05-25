/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.service;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.cli.api.TestCliConfiguration;
import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
  properties = {
    "logging.level.com.xenoterracide.brix.configloader.service.ConfigLoaderService=trace"
  }
)
class ConfigLoaderServiceTest {

  static final Path CONFIG_DIR = Path.of( ".config", "brix" );

  static final Path HOME_CONFIG_DIR = SystemUtils.getUserHome().toPath().resolve( CONFIG_DIR );

  static final Path PROJECT_CONFIG_DIR
    = HOME_CONFIG_DIR.resolve( ConfigLoaderServiceTest.class.getSimpleName() );

  @Value("classpath:home.yaml")
  Resource config;

  @Autowired ConfigLoaderService loaderService;

  @Autowired CliConfiguration cliConfiguration;

  @AfterAll
  static void teardown() throws IOException {
    FileSystemUtils.deleteRecursively( PROJECT_CONFIG_DIR );
  }

  @BeforeEach
  void setup() throws IOException {
    var langDir = Files.createDirectories( PROJECT_CONFIG_DIR );
    Files.copy( config.getFile().toPath(), langDir.resolve( "home.yaml" ) );
  }

  @Test
  void homeTest() {
    var processedConfig = loaderService.findAndLoad( cliConfiguration );
    assertThat( processedConfig.getFileConfigurations() )
      .isNotEmpty()
      .map( ProcessedFileConfiguration::getContext )
      .map( m -> m.get( "configDir" ) )
      .contains( PROJECT_CONFIG_DIR );
  }

  @TestConfiguration
  static class Config {

    @Bean
    CliConfiguration cliConfiguration() {
      return TestCliConfiguration.builder()
        .language( ConfigLoaderServiceTest.class.getSimpleName() )
        .moduleType( "home" )
        .build();
    }
  }
}
