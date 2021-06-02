/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.copy;

import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.processor.api.AbstractProcessorBase;
import com.xenoterracide.brix.service.console.FileService;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Component
class CopyProcessor extends AbstractProcessorBase {

  private final Logger log = LogManager.getLogger( CopyProcessor.class );

  private final FileService fs;

  CopyProcessor(
    ConsoleWrapper console,
    FileService fs
  ) {
    super( console );
    this.fs = fs;
  }

  @Override
  public boolean shouldProcess( ProcessedFileConfiguration fc ) {
    return fc.getSource() != null
      && Files.exists( fc.getSource().toAbsolutePath() )
      && fs.isBinary( fc.getSource().toAbsolutePath() );
  }

  @Override
  protected void writeTemplate( Path source, Path dest, Map<String, @Nullable Object> context ) {
    log.debug( "copying: '{}' to '{}'", source.toAbsolutePath(), dest.toAbsolutePath() );
    try {
      FileCopyUtils.copy( source.toFile(), dest.toFile() );
    }
    catch ( IOException e ) {
      throw new UncheckedIOException( e );
    }
  }

}
