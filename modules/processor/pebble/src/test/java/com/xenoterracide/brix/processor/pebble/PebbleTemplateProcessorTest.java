/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.pebble;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.xenoterracide.brix.processor.spi.ConsoleWrapper;
import com.xenoterracide.brix.util.FileService;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
class PebbleTemplateProcessorTest {

  @Autowired PebbleEngine fileEngine;

  @Autowired FileService fileService;

  @Test
  void testAsk() throws IOException, URISyntaxException {
    var workdir = Files.createTempDirectory( "brix" );
    var classpathRoot = this.getClass()
      .getClassLoader()
      .getResource( "templates" )
      .toURI();

    var configDir = Path.of( classpathRoot );
    var templatePath = configDir.resolve( "testTemplate.peb" );
    var pathToOutput = workdir.resolve( "testTemplate" );

    var console = Mockito.mock( ConsoleWrapper.class );
    Mockito.when( console.readLine( anyString(), any( Path.class ) ) ).thenReturn( "y" );
    var processor = new PebbleTemplateProcessor( console, fileEngine, fileService );

    var template = fileEngine.getTemplate( templatePath.toString() );

    Map<String, Object> ctx1 = Map.of( "test", "foo" );
    processor.writeTemplate( template, pathToOutput, ctx1 );
    assertThat( Files.readString( pathToOutput ) ).isEqualTo( "%s%n", "foo" );

    Map<String, Object> ctx2 = Map.of( "test", "bar" );
    processor.askWhetherToWriteTemplate( template, pathToOutput, ctx2 );
    assertThat( Files.readString( pathToOutput ) ).isEqualTo( "%s%n", "bar" );

    PathUtils.delete( workdir );
  }
}
