package com.xenoterracide.brix.processor.api;

import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractProcessorBase implements Processor {

  protected final ConsoleWrapper console;

  private final Logger log = LogManager.getLogger( AbstractProcessorBase.class );

  protected AbstractProcessorBase( ConsoleWrapper console ) {
    this.console = console;
  }

  @Override
  public void process( ProcessedFileConfiguration fc ) {
    var source = Objects.requireNonNull( fc.getSource() );
    var dest = fc.getDestination();
    var context = fc.getContext();
    var overwrite = fc.getOverwrite();

    log.debug( "processing: '{}'", source.toAbsolutePath() );

    try {
      var parent = dest.getParent();

      // this shouldn't happen because dest, should be a file and so it should always have a parent
      if ( parent == null ) {
        throw new IllegalArgumentException( "destination should have a parent directory" );
      }

      if ( !Files.exists( parent ) ) {
        log.trace( "creating directory: {}", parent.toAbsolutePath() );
        Files.createDirectories( parent );
      }
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }

    log.debug( "outputting: '{}'", dest.toAbsolutePath() );
    if ( Files.exists( dest ) ) {
      log.debug( "file '{}' exists", dest.toAbsolutePath() );
      if ( overwrite == null ) {
        askWhetherToWriteTemplate( source, dest, context );
      }
      else if ( overwrite ) {
        writeTemplate( source, dest, context );
      }
    }
    else {
      writeTemplate( source, dest, context );
    }
  }

  protected void askWhetherToWriteTemplate(
    Path source,
    Path dest,
    Map<String, @Nullable Object> context
  ) {
    var line = console.readLine( "Overwrite [yN] %s ", dest );
    if ( BooleanUtils.toBoolean( line ) ) {
      writeTemplate( source, dest, context );
    }
    else {
      log.debug( "skipping file {}", dest.toAbsolutePath() );
    }
  }

  abstract protected void writeTemplate(
    Path source,
    Path dest,
    Map<String, @Nullable Object> context
  );
}
