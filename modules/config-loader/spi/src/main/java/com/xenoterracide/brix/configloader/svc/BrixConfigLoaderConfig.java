/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.svc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.xenoterracide.brix.cli.api.CliConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.mime.MimeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Configuration
class BrixConfigLoaderConfig {

  private final Logger log = LogManager.getLogger( BrixConfigLoaderConfig.class );

  private final CliConfiguration config;

  private final Path home;

  private final List<MimeType> mimeTypes;

  BrixConfigLoaderConfig(
    @Value("${user.home}")
      Path home,
    CliConfiguration config,
    List<MimeType> mimeTypes
  ) {
    this.config = config;
    this.home = home;
    this.mimeTypes = mimeTypes;
  }

  @Bean
  @ConditionalOnMissingBean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  PebbleEngine stringEngine() {
    return new PebbleEngine.Builder()
      .newLineTrimming( false )
      .strictVariables( true )
      .loader( new StringLoader() )
      .build();
  }

  @Bean
  Path foundConfigDir( Path foundConfig ) {
    return foundConfig.getParent();
  }

  @Bean
  Path foundConfig() {
    return this.mimeTypes.stream()
      .map( MimeType::getExtensions )
      .flatMap( List::stream )
      .map( this::findConfig )
      .flatMap( Optional::stream )
      .findFirst()
      .orElseThrow( () -> new UncheckedIOException(
        new FileNotFoundException( "config file not found" )
      ) ).toAbsolutePath();
  }

  Optional<Path> findConfig( String extension ) {
    return config.getConfigDir()
      .map( dir -> dir.resolve( this.pathFromConfigDir( extension ) ) )
      .filter( Files::exists )
      .or( () -> this.findConfig( extension, Path.of( "" ).toAbsolutePath() ) );
  }

  @SuppressWarnings("checkstyle:ReturnCount")
  Optional<Path> findConfig( String extension, Path searchDir ) {
    var configFile = searchDir.resolve( ".config" )
      .resolve( "brix" )
      .resolve( this.pathFromConfigDir( extension ) );

    log.trace( "searching for: '{}'", configFile );
    if ( Files.exists( configFile ) ) {
      log.debug( "found: '{}'", configFile );
      return Optional.of( configFile );
    }

    if ( Objects.equals( home, searchDir ) || Objects.isNull( searchDir.getParent() ) ) {
      return Optional.empty();
    }
    return this.findConfig( extension, searchDir.getParent() );
  }

  Path pathFromConfigDir( String extension ) {
    return Path.of( config.getLanguage() ).resolve( config.getModuleType() + extension );
  }
}