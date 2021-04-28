package com.xenoterracide.brix.configloader.svc;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.cli.api.ImmutableTestCliConfiguration;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BrixConfigLoaderConfigTest {

  static final Path CONFIG_DIR = Path.of( ".config", "brix" );

  static final Path HOME_CONFIG_DIR = SystemUtils.getUserHome().toPath().resolve( CONFIG_DIR );

  @Autowired Path foundConfig;

  @Test
  void homeTest() {
    assertThat( foundConfig ).exists();
  }

  @TestConfiguration
  static class Config {

    Config(
      @Value("classpath:home.yaml")
        Resource config
    ) {
      try {
        var langDir = Files.createDirectories( HOME_CONFIG_DIR.resolve( "brixTestDir" ) );
        Files.copy( config.getFile().toPath(), langDir.resolve( "home.yaml" ) );
      }
      catch ( IOException e ) {
        LogManager.getLogger( this.getClass() ).warn( e );
      }
    }

    @Bean
    MimeType yaml( MimeTypes mimeTypes ) throws MimeTypeException {
      return mimeTypes.forName( "text/x-yaml" );
    }

    @Bean
    CliConfiguration cliConfiguration() {
      return ImmutableTestCliConfiguration.builder()
        .language( "brixTestDir" )
        .moduleType( "home" )
        .build();
    }
  }
}
