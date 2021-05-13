/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.yaml;

import com.xenoterracide.brix.configloader.spi.RawFileConfiguration;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@ContextConfiguration(classes = {RawFileConfigurationTest.Config.class, YamlConfig.class})
class RawFileConfigurationTest {

  @Autowired
  JacksonTester<RawFileConfiguration> tester;

  @Test
  void deserialize() throws IOException {
    assertThat( tester.read( "/raw-file-configuration.yaml" ) )
      .extracting(
        RawFileConfiguration::getSource,
        RawFileConfiguration::getDestination
      )
      .contains( Optional.of( "foo" ), "bar" );
  }

  @TestConfiguration
  static class Config {
    @Bean
    MimeTypes mimeRepository() {
      return TikaConfig.getDefaultConfig().getMimeRepository();
    }
  }
}
