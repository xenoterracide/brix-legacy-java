/*
 * Copyright © 2020 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.xenoterracide.brix.FileConfiguration;
import io.vavr.control.Try;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class PebbleTemplateProcessor implements Processor {

  private final Logger log = LogManager.getLogger( this.getClass() );

  private final Path configDir;

  private final Path workdir;

  private final ConsoleWrapper console;

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

  public PebbleTemplateProcessor( @NonNull Path configDir, @NonNull Path workdir ) {
    this( configDir, workdir, new ConsoleWrapper() );
  }

  PebbleTemplateProcessor(
    @NonNull Path configDir,
    @NonNull Path workdir,
    @NonNull ConsoleWrapper console
  ) {
    this.configDir = Objects.requireNonNull( configDir );
    this.workdir = Objects.requireNonNull( workdir );
    this.console = Objects.requireNonNull( console );
  }

  @Override
  public void process(
    Map.@NonNull Entry<String, FileConfiguration> entry,
    @NonNull Map<String, Object> context
  ) {
    log.debug( "context: {}", context );
    var templatePath = getPath( configDir, entry.getValue().getSource(), context );
    var destPath = getPath( workdir, entry.getValue().getDestination(), context );
    log.debug( "processing: {}", templatePath.toAbsolutePath() );

    writeFile( templatePath, destPath, entry.getValue(), context );
  }

  @NonNull
  Path getPath(
    @NonNull Path relativeTo,
    @NonNull Path pathTemplate,
    @NonNull Map<String, Object> context
  )
    throws UncheckedIOException {
    var template = stringEngine.getTemplate(
      relativeTo.resolve( pathTemplate ).toAbsolutePath().toString()
    );

    return Try.of(
      () -> {
        var writer = new StringWriter();
        template.evaluate( writer, context );
        return Path.of( writer.toString() ).toAbsolutePath();
      }
    )
      .getOrElseThrow( e -> new RuntimeException( e ) );
  }

  void writeFile(
    @NonNull Path template,
    @NonNull Path dest,
    @NonNull FileConfiguration skel,
    @NonNull Map<String, Object> context
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
    @NonNull PebbleTemplate source, @NonNull Path dest, @NonNull Map<String, Object> context
  ) {
    var line = console.readLine( "Overwrite [yN] %s ", dest );
    if ( BooleanUtils.toBoolean( line ) ) {
      writeTemplate( source, dest, context );
    }
    else {
      log.debug( "skipping file {}", dest.toAbsolutePath() );
    }
  }

  void writeTemplate(
    @NonNull PebbleTemplate source,
    @NonNull Path dest,
    @NonNull Map<String, Object> context
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

  static class ConsoleWrapper {
    String readLine( @NonNull String fmt, @NonNull Object... args ) {
      var console = System.console();
      if ( console == null ) {
        throw new IllegalStateException( "unable to prompt for overwrite, console is not " +
          "available" );
      }
      var line = console.readLine( fmt, args );
      if ( line == null ) {
        throw new IllegalStateException( "stream should not be terminated" );
      }
      return line;
    }
  }
}
