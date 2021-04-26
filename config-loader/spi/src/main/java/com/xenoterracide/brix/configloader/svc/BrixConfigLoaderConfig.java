package com.xenoterracide.brix.configloader.svc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.xenoterracide.brix.cli.api.CliConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileNotFoundException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Configuration
@PropertySource("classpath:jackson.properties")
class BrixConfigLoaderConfig {

  private final Logger log = LogManager.getLogger( BrixConfigLoaderConfig.class );

  private final CliConfiguration config;

  private final Path home;

  private final List<String> extensions;

  BrixConfigLoaderConfig(
    @Value("${user.home}")
      Path home,
    CliConfiguration config,
    List<String> extensions
  ) {
    this.config = config;
    this.home = home;
    this.extensions = extensions;
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
  Tika tika() {
    return new Tika();
  }

  @Bean
  Path foundConfig() {
    return this.extensions.stream()
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

    if ( home.equals( searchDir ) ) {
      return Optional.empty();
    }
    return findConfig( extension, searchDir.getParent() );
  }

  Path pathFromConfigDir( String extension ) {
    return Path.of( config.getLanguage() ).resolve( config.getModuleType() + extension );
  }
}
