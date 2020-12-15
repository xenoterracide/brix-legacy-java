package com.xenoterracide.brix;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import org.apache.commons.io.file.PathUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class PebbleTemplateProcessorTest {

  /*
  private final PebbleEngine stringEngine = new PebbleEngine.Builder()
    .newLineTrimming( false )
    .strictVariables( true )
    .loader( new StringLoader() )
    .build();
   */

  private final PebbleEngine fileEngine = new PebbleEngine.Builder()
    .newLineTrimming( false )
    .strictVariables( true )
    .loader( new FileLoader() )
    .build();

  @Test
  void testAsk() throws IOException, URISyntaxException {
    var workdir = Files.createTempDirectory( "brix" );
    var classpathRoot = this.getClass().getClassLoader().getResource( "templates" ).toURI();
    var configDir = Path.of( classpathRoot );
    var templatePath = configDir.resolve( "testTemplate.peb" );
    var pathToOutput = workdir.resolve( "testTemplate" );

    var console = Mockito.mock( PebbleTemplateProcessor.ConsoleWrapper.class );
    Mockito.when( console.readLine( anyString(), any( Path.class ) ) ).thenReturn( "y" );
    var processor = new PebbleTemplateProcessor( configDir, pathToOutput, console );

    var template = fileEngine.getTemplate( templatePath.toString() );

    Map<String, Object> ctx1 = Map.of( "test", "foo" );
    processor.writeTemplate( template, pathToOutput, ctx1 );
    Assertions.assertThat( Files.readString( pathToOutput ) ).isEqualTo( "foo\n" );

    Map<String, Object> ctx2 = Map.of( "test", "bar" );
    processor.askWhetherToWriteTemplate( template, pathToOutput, ctx2 );
    Assertions.assertThat( Files.readString( pathToOutput ) ).isEqualTo( "bar\n" );

    PathUtils.delete( workdir );
  }
}
