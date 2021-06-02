/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.pebble;

import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PebbleTemplateProcessorTest {

  @Autowired PebbleTemplateProcessor processor;

  Path workdir;

  @BeforeEach
  void setup() throws IOException {
    workdir = Files.createTempDirectory( this.getClass().getSimpleName() );
  }

  @AfterEach
  void teardown() throws IOException {
    FileSystemUtils.deleteRecursively( workdir );
  }

  @ParameterizedTest
  @ArgumentsSource(Source.class)
  void shouldProcess( String filename, boolean shouldProcess ) {
    var dir = Path.of( "../../../gradle/wrapper" );

    var config = ProcessedFileConfiguration.builder()
      .context( Map.of() )
      .overwrite( true )
      .source( dir.resolve( filename ) )
      .destination( workdir.resolve( filename ) )
      .build();

    assertThat( config.getSource().toAbsolutePath() ).exists();
    assertThat( processor.shouldProcess( config ) ).isEqualTo( shouldProcess );
  }

  @ParameterizedTest
  @ArgumentsSource(Source.class)
  void process( String filename ) {
    var dir = Path.of( "../../../gradle/wrapper" );

    var config = ProcessedFileConfiguration.builder()
      .context( Map.of() )
      .overwrite( true )
      .source( dir.resolve( filename ) )
      .destination( workdir.resolve( filename ) )
      .build();

    assertThat( config.getSource().toAbsolutePath() ).exists();
    assertThat( config.getDestination().toAbsolutePath() ).doesNotExist();
    processor.process( config );
    assertThat( config.getDestination().toAbsolutePath() ).exists();
  }

  static class Source implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments( ExtensionContext context ) {
      return Stream.of(
        Arguments.of( "gradle-wrapper.jar", false ),
        Arguments.of( "gradle-wrapper.properties", true )
      );
    }
  }
}
