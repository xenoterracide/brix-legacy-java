package com.xenoterracide.brix;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
// import org.apache.commons.io.file.PathUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Map;

class RegexCaptureGroupTest {
  private final PebbleEngine pebbleEngine = new PebbleEngine.Builder()
    .newLineTrimming( false )
    .strictVariables( true )
    .loader( new FileLoader() )
    .build();

  @Test
  void test () throws IOException, URISyntaxException {
    var configDir = Path.of( this.getClass().getClassLoader().getResource( "templates" ).toURI() );
    var templatePath = configDir.resolve( "testTemplate.yml" );

    var template = pebbleEngine.getTemplate( templatePath.toString() );

    Writer writer = new StringWriter();
    Map<String, Object> ctx = Map.of( "name", "test", "moduleType", "foo" );
    template.evaluate( writer, ctx );
    var output = writer.toString();

    Assertions.assertThat(output).contains("destination: \'test/settings.gradle.kts\'");
    Assertions.assertThat(output).contains("replace: \'$1  :\"foo\"\'");
  }
}
