/*
* Copyright © 2020-2021 Caleb Cushing.
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
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

class ConfigLoader {

  private final Logger log = LogManager.getLogger( this.getClass() );

  private final Path configDir;

  private final String language;

  private final String moduleType;

  private final ObjectMapper mapper = new ObjectMapper( new YAMLFactory() )
    .enable( DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS )
    .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
    .enable( DeserializationFeature.WRAP_EXCEPTIONS )
    .enable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES )
    .findAndRegisterModules();

  ConfigLoader( CliConfiguration cliConfig ) {
    this.configDir = cliConfig.getConfigDir().orElse( Path.of( ".config", "brix" ) );
    this.language = cliConfig.getLanguage();
    this.moduleType = cliConfig.getModuleType();
  }

  Path pathToConfigFile() {
    var filename = moduleType + ".yml";
    var relPathToConfigFile = Path.of( language ).resolve( filename );
    var cwdConfigFile = configDir.resolve( relPathToConfigFile );

    var home = SystemUtils.getUserDir().toPath();
    log.debug( "searching at: {}", cwdConfigFile );
    var confFile = Files.exists( cwdConfigFile )
                   ? cwdConfigFile
                   : home.resolve( cwdConfigFile );

    log.debug( "using location: {}", confFile );
    return confFile.toAbsolutePath();
  }

  Map<String, FileConfiguration> load( Path path ) {
    var config = Try.of( () -> mapper.readValue( path.toFile(), Config.class ) ).get();

    log.debug( "config: {}", config );

    return config.getTemplates();
  }
}
