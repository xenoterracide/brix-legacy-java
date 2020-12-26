/*
 * Copyright © 2020 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.vavr.control.Try;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

class ConfigLoader {

  private final Path configDir;

  private final String language;

  private final String moduleType;

  private final ObjectMapper mapper = new ObjectMapper( new YAMLFactory() )
    .enable( DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS )
    .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
    .enable( DeserializationFeature.WRAP_EXCEPTIONS )
    .enable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES )
    .findAndRegisterModules();

  ConfigLoader( @NonNull CliConfiguration cliConfig ) {
    this.configDir = cliConfig.getConfigDir();
    this.language = cliConfig.getLanguage();
    this.moduleType = cliConfig.getModuleType();
  }

  @NonNull
  Path pathToConfigFile() {
    var filename = moduleType + ".yml";
    var relPathToConfigFIle = Path.of( language ).resolve( filename );
    var cwdConfigFile = configDir.resolve( relPathToConfigFIle );

    var home = SystemUtils.getUserHome().toPath();
    var confFile = Files.exists( cwdConfigFile ) ? cwdConfigFile : home.resolve( relPathToConfigFIle );
    return confFile.toAbsolutePath();
  }

  @NonNull
  Map<String, SkeletonConfiguration> load( @NonNull Path path ) throws RuntimeException {
    var log = LogManager.getLogger( this.getClass() );

    var config = Try
      .of( () -> mapper.readValue( path.toFile(), Config.class ) )
      .getOrElseThrow( e -> {
        if ( e instanceof IOException ) {
          throw new UncheckedIOException( (IOException) e );
        }
        throw new RuntimeException( e );
      } );

    log.debug( "config: {}", config );

    return config.getTemplates();
  }
}
