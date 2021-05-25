/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.service;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.configloader.api.ConfigLoader;
import com.xenoterracide.brix.configloader.api.ProcessedConfig;
import com.xenoterracide.brix.configloader.api.RawConfig;
import com.xenoterracide.brix.util.lang.CollectionUtils;
import io.vavr.control.Try;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class ConfigLoaderService {

  private final Logger log = LogManager.getLogger( ConfigLoaderService.class );

  private final List<ConfigLoader> loaders;

  private final ConfigValueProcessor processor;

  ConfigLoaderService(
    List<ConfigLoader> loaders,
    ConfigValueProcessor processor
  ) {
    this.loaders = CollectionUtils.requireNonEmpty( loaders );
    this.processor = processor;
  }

  public ProcessedConfig findAndLoad( CliConfiguration cli ) {
    return loaders.stream()
      .flatMap( loader -> configSearchPath( cli )
        .flatMap( loader.extensions( cli ) )
        .peek( path -> log.trace( "for: '{}'", path.toAbsolutePath() ) )
        .filter( Files::exists )
        .flatMap( tryLoad( loader ) )
      )
      .map( p -> processor.from( p.getLeft(), p.getRight() ) )
      .findFirst()
      .orElseThrow( () -> {
        var msg = "configuration file not found";
        return new UncheckedIOException( new FileNotFoundException( msg ) );
      } );
  }

  private Stream<Path> configSearchPath( CliConfiguration cli ) {
    var localDir = Path.of( ".config", "brix" );
    var homeDir = SystemUtils.getUserHome().toPath().resolve( localDir );
    return Stream.concat( cli.getConfigDir().stream(), Stream.of( localDir, homeDir ) )
      .peek( p -> log.debug( "searching: {}", p.toAbsolutePath() ) )
      .filter( Files::exists );
  }

  private Function<Path, Stream<Pair<Path, RawConfig>>> tryLoad( ConfigLoader loader ) {
    return path -> {
      log.trace( "loading: '{}'", path.toAbsolutePath() );
      return Try.of( () -> Pair.of( path, loader.load( path ) ) )
        .onFailure( e -> log.error( "failed to load: '{}'", path.toAbsolutePath(), e ) )
        .toJavaStream();
    };
  }
}
