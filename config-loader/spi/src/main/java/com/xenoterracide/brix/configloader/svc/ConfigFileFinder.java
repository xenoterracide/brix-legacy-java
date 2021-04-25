package com.xenoterracide.brix.configloader.svc;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.xenoterracide.brix.cli.api.CliConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
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
class ConfigFileFinder {

  private final Logger log = LogManager.getLogger( this.getClass() );

  private final CliConfiguration config;

  private final Path home;

  private final List<String> extensions;

  ConfigFileFinder(
    @Value("user.home")
      Path home,
    CliConfiguration config,
    List<String> extensions
  ) {
    this.config = config;
    this.home = home;
    this.extensions = extensions;
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
      .or( () -> this.findConfig( extension, Path.of( "" ) ) );
  }

  Optional<Path> findConfig( String extension, Path searchDir ) {
    var configFile = searchDir.resolve( ".config" )
      .resolve( "brix" )
      .resolve( this.pathFromConfigDir( extension ) );

    log.trace( "searching for: '{}'", configFile::toAbsolutePath );
    if ( Files.exists( configFile ) ) {
      log.debug( "found: '{}'", configFile::toAbsolutePath );
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
