/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.api;

import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import com.xenoterracide.brix.util.lang.ObjectUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public abstract class AbstractProcessorBase implements Processor {

  private final ConsoleWrapper console;

  private final Logger log = LogManager.getLogger( AbstractProcessorBase.class );

  protected AbstractProcessorBase( ConsoleWrapper console ) {
    this.console = console;
  }

  @Override
  public void process( ProcessedFileConfiguration fc ) {
    var source = ObjectUtils.requireNonNull( fc.getSource(), "source" );
    var dest = fc.getDestination();
    var context = fc.getContext();
    var overwrite = fc.getOverwrite();

    log.debug( "processing: '{}'", source::toAbsolutePath );

    try {
      var parent = ObjectUtils.requireNonNull( dest.getParent(), "parent directory" );

      if ( !Files.exists( parent ) ) {
        log.debug( "creating directory: {}", parent::toAbsolutePath );
        Files.createDirectories( parent );
      }
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }

    log.debug( "outputting: '{}'", dest::toAbsolutePath );
    if ( Files.exists( dest ) ) {
      log.debug( "file '{}' exists", dest::toAbsolutePath );
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
      log.debug( "skipping file '{}'", dest::toAbsolutePath );
    }
  }

  protected abstract void writeTemplate(
    Path source,
    Path dest,
    Map<String, @Nullable Object> context
  );
}
