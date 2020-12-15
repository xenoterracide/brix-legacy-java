/*
* Copyright © 2020 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.vavr.control.Try;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class PebbleTemplateProcessor {

  private final Logger log = LogManager.getLogger( this.getClass() );
  private final Path configDir;
  private final Path workdir;

  private final PebbleEngine fileEngine = new PebbleEngine.Builder()
    .newLineTrimming( false )
    .strictVariables( true )
    .loader( new FileLoader() )
    .build();

  private final PebbleEngine stringEngine = new PebbleEngine.Builder()
    .newLineTrimming( false )
    .strictVariables( true )
    .loader( new StringLoader() )
    .build();

  PebbleTemplateProcessor( @NotNull Path configDir, @NotNull Path workdir ) {
    this.configDir = Objects.requireNonNull( configDir );
    this.workdir = Objects.requireNonNull( workdir );
  }

  void process(
    Map.@NotNull Entry<String, SkeletonConfiguration> entry,
    @NotNull Map<String, Object> context
  ) {
    log.debug( "context: {}", context );
    var templatePath = getPath( configDir, entry.getValue().getSource(), context );
    var destPath = getPath( workdir, entry.getValue().getDestination(), context );
    log.debug( "processing: {}", templatePath.toAbsolutePath() );

    writeFile( templatePath, destPath, entry.getValue(), context );
  }

  @NotNull
  Path getPath(
    @NotNull Path relativeTo,
    @NotNull String pathTemplate,
    @NotNull Map<String, Object> context
  )
    throws UncheckedIOException {
    var template = stringEngine.getTemplate(
      relativeTo.resolve( pathTemplate ).toAbsolutePath().toString()
    );

    return Try
      .of(
        () -> {
          var writer = new StringWriter();
          template.evaluate( writer, context );
          return Path.of( writer.toString() ).toAbsolutePath();
        }
      )
      .getOrElseThrow( e -> new RuntimeException( e ) );
  }

  void writeFile(
    @NotNull Path template,
    @NotNull Path dest,
    @NotNull SkeletonConfiguration skel,
    @NotNull Map<String, Object> context
  )
    throws UncheckedIOException {
    var sourceTemplate = fileEngine.getTemplate( template.toAbsolutePath().toString() );
    var overwrite = skel.getOverwrite();
    if ( Files.exists( dest.toAbsolutePath() ) ) {
      log.debug( "file {} exists", dest.toAbsolutePath() );
      if ( overwrite == null ) {
        askWhetherToWriteTemplate( sourceTemplate, dest, context );
      }
      else if ( overwrite ) {
        writeTemplate( sourceTemplate, dest, context );
      }
    }
    else {
      writeTemplate( sourceTemplate, dest, context );
    }
  }

  void askWhetherToWriteTemplate(
    PebbleTemplate source, Path dest, Map<String, Object> context
  ) {
    var console = Objects.requireNonNull( System.console() );
    var line = console.readLine( "Overwrite [yN] %s ", dest );
    if ( BooleanUtils.toBoolean( line ) ) {
      writeTemplate( source, dest, context );
    }
    else {
      log.debug( "skipping file {}", dest.toAbsolutePath() );
    }
  }

  void writeTemplate(
    @NotNull PebbleTemplate source,
    @NotNull Path dest,
    @NotNull Map<String, Object> context
  ) {
    try {
      var parent = dest.getParent();

      // this shouldn't happen because dest, should be a file and so it should always have a parent
      if ( parent == null ) {
        throw new RuntimeException( "destination should have a parent directory" );
      }

      if ( !Files.exists( parent ) ) {
        log.debug( "creating directory: {}", parent );
        Files.createDirectories( parent );
      }
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }
    log.debug( "creating file: {}", dest );
    try ( var fw = new FileWriter( dest.toFile(), StandardCharsets.UTF_8 ) ) {
      source.evaluate( fw, context );
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }
  }
}
