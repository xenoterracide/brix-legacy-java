/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.api;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import org.apache.tika.mime.MimeType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ConfigLoader {

  RawConfig load( Path config ) throws IOException;

  default Function<Path, Stream<Path>> extensions( CliConfiguration cli ) {
    return path -> mimeType()
      .getExtensions()
      .stream()
      .map( ext -> path.resolve( cli.configPath( ext ) ) );
  }

  MimeType mimeType();
}
