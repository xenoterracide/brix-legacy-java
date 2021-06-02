/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.copy;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.processor.api.AbstractProcessorBase;
import com.xenoterracide.brix.service.console.FileService;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

@Component
class PebbleTemplateProcessor extends AbstractProcessorBase {

  private final Logger log = LogManager.getLogger( PebbleTemplateProcessor.class );

  private final PebbleEngine fileEngine;

  private final FileService fs;

  PebbleTemplateProcessor(
    ConsoleWrapper console,
    PebbleEngine fileEngine,
    FileService fs
  ) {
    super( console );
    this.fileEngine = fileEngine;
    this.fs = fs;
  }

  @Override
  public boolean shouldProcess( ProcessedFileConfiguration fc ) {
    return fc.getSource() != null && !fs.isBinary( fc.getSource() );
  }

  protected void writeTemplate( Path source, Path dest, Map<String, @Nullable Object> context ) {
    var template = fileEngine.getTemplate( source.toAbsolutePath().toString() );
    log.trace( "creating file: {}", dest.toAbsolutePath() );
    try ( var fw = new FileWriter( dest.toFile(), StandardCharsets.UTF_8 ) ) {
      template.evaluate( fw, context );
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }
  }

}
