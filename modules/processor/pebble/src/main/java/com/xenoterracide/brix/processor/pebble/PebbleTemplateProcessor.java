/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.pebble;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.processor.api.Processor;
import com.xenoterracide.brix.util.file.FileService;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@Component
class PebbleTemplateProcessor implements Processor {

  private final Logger log = LogManager.getLogger( PebbleTemplateProcessor.class );

  private final ConsoleWrapper console;

  private final PebbleEngine fileEngine;

  private final FileService fs;

  PebbleTemplateProcessor(
    ConsoleWrapper console,
    PebbleEngine fileEngine,
    FileService fs
  ) {
    this.console = console;
    this.fileEngine = fileEngine;
    this.fs = fs;
  }

  @Override
  public boolean shouldProcess( ProcessedFileConfiguration fc ) {
    return fc.getSource() != null && !fs.isBinary( fc.getSource() );
  }

  @Override
  public void process( ProcessedFileConfiguration fc ) {
    var template = Objects.requireNonNull( fc.getSource() );
    log.debug( "processing: {}", template.toAbsolutePath() );

    var dest = fc.getDestination();
    var context = fc.getContext();
    var sourceTemplate = fileEngine.getTemplate( template.toAbsolutePath().toString() );
    var overwrite = fc.getOverwrite();

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
    PebbleTemplate source,
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

  void writeTemplate( PebbleTemplate source, Path dest, Map<String, Object> context ) {
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
