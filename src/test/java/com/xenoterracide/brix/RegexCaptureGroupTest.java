/*
* Copyright © 2020-2021 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

class RegexCaptureGroupTest {
  private static final String SETTINGS_FILE = "settings.txt";

  private final PebbleEngine pebbleEngine = new PebbleEngine.Builder()
    .newLineTrimming( false )
    .strictVariables( true )
    .loader( new FileLoader() )
    .build();

  @Test
  void test() throws IOException, URISyntaxException {
    var configDir = Path.of( this.getClass().getClassLoader().getResource( "templates" ).toURI() );
    var templatePath = configDir.resolve( "testTemplate.yml" );

    var template = pebbleEngine.getTemplate( templatePath.toString() );

    Writer writer = new StringWriter();
    Map<String, Object> ctx = Map.of( "name", "test", "moduleType", "foo" );
    template.evaluate( writer, ctx );
    var output = writer.toString();

    Assertions.assertThat( output ).contains( "destination: \'test/settings.gradle.kts\'" );
    Assertions.assertThat( output ).contains( "replace: \'$1  :\"foo\"\'" );

    File settings = new File( configDir.toString(), SETTINGS_FILE );
    BufferedWriter bufferedWriter = Files.newBufferedWriter( settings.toPath(),
      Charset.defaultCharset() );
    try {
      bufferedWriter.write( String.format( "rootProject.name =" +
        "\"test-template\"%n%n include(%n\t\":%s\"%n)", "foo" ) );
    }
    finally {
      bufferedWriter.close();
    }

    String fileContent = Files.readString(
      Path.of( configDir.toString(), SETTINGS_FILE ),
      StandardCharsets.US_ASCII );
    Assertions.assertThat( fileContent ).contains( "\":foo\"" );
  }
}
