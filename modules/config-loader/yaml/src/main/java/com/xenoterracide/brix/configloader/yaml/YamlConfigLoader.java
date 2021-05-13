/*
* Copyright © 2020-2021 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix.configloader.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenoterracide.brix.configloader.api.ConfigLoader;
import com.xenoterracide.brix.configloader.api.ProcessedConfig;
import com.xenoterracide.brix.configloader.spi.ConfigValueProcessor;
import com.xenoterracide.brix.configloader.spi.RawConfig;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
class YamlConfigLoader implements ConfigLoader {

  private final Logger log = LogManager.getLogger( this.getClass() );

  private final ObjectMapper mapper;

  private final Tika tika;

  private final ConfigValueProcessor processor;

  YamlConfigLoader(
    ObjectMapper mapper,
    Tika tika,
    ConfigValueProcessor processor
  ) {
    this.mapper = mapper;
    this.tika = tika;
    this.processor = processor;
  }

  @Override
  public boolean canLoad( Path path ) {
    return Try.of( () -> tika.detect( path ) )
      .map( MediaType::parse )
      .map( mt -> mt.equals( MediaType.text( "x-yaml" ) ) )
      .getOrElse( false );
  }

  @Override
  public ProcessedConfig load( Path path ) {
    var config = Try.of( () -> mapper.readValue( path.toFile(), RawConfig.class ) ).get();

    log.debug( "raw config: {}", config );

    return processor.from( config );
  }
}
