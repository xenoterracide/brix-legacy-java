/*
 * Copyright © 2020 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
class PebbleTemplateProcessor implements Processor {

  private final Logger log = LogManager.getLogger( this.getClass() );

  private final ConsoleWrapper console;
  private final PebbleEngine fileEngine;

  PebbleTemplateProcessor( ConsoleWrapper console, PebbleEngine fileEngine ) {
    this.console = console;
    this.fileEngine = fileEngine;
  }

  @Override
  public boolean shouldProcess( ProcessorInstruction instruction ) {
    return instruction.getSource().isPresent();
  }

  @Override
  public void process(
    Path configDir,
    ProcessorInstruction instruction,
    Map<String, Object> context
  ) {
    log.debug( "context: {}", context );
    var template = instruction.getSource().orElseThrow();
    log.debug( "processing: {}", template.toAbsolutePath() );

    writeFile( template, instruction.getDestination(), instruction, context );
  }


  void writeFile(
    Path template,
    Path dest,
    ProcessorInstruction skel,
    Map<String, Object> context
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

  void askWhetherToWriteTemplate( PebbleTemplate source, Path dest, Map<String, Object> context ) {
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
