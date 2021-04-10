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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexCaptureGroupTest {

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
    try ( BufferedWriter bufWriter = Files.newBufferedWriter( templatePath,
      Charset.defaultCharset() ) ) {
      bufWriter.write( writer.toString() );
    }

    var config
      = ImmutableTestCliConfiguration.builder()
      .moduleType( "module" )
      .build();

    var loader = new ConfigLoader( config );
    var map = loader.load( templatePath );
    var skeleton = map.get( "settings.txt" );
    if ( skeleton == null ) {
      throw new FileNotFoundException( "settings.txt not present" );
    }
    if ( skeleton.getAfter() == null ) {
      throw new MissingFormatArgumentException( "after pattern not provided" );
    }

    String fileContent = Files.readString(
      Path.of( configDir.toString(), skeleton.getDestination().toString() ),
      StandardCharsets.US_ASCII );

    Pattern pattern = Pattern.compile( skeleton.getAfter().pattern() );
    Matcher matcher = pattern.matcher( fileContent );
    if ( matcher.find() ) {
      String start = fileContent.substring( 0, matcher.end() );
      String end = fileContent.substring( matcher.end() );
      String updatedContent = start + "\n\t\":" + ctx.get( "moduleType" ) + "\"," + end;

      Assertions.assertThat( updatedContent ).contains( "\":" + ctx.get( "moduleType" ) + "\"" );
    }
  }
}
