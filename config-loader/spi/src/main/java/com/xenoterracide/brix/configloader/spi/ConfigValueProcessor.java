package com.xenoterracide.brix.configloader.spi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.configloader.api.ImmutableProcessedConfig;
import com.xenoterracide.brix.configloader.api.ImmutableProcessedFileConfiguration;
import com.xenoterracide.brix.configloader.api.ProcessedConfig;
import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConfigValueProcessor {
  private final Logger log = LogManager.getLogger( this.getClass() );

  private final PebbleEngine engine;

  private final CliConfiguration cliConfiguration;

  private final ObjectMapper mapper;

  private final Path configDir;

  ConfigValueProcessor(
    CliConfiguration cliConfiguration,
    PebbleEngine stringEngine,
    ObjectMapper mapper,
    Path foundConfigFile
  ) {
    this.engine = stringEngine;
    this.cliConfiguration = cliConfiguration;
    this.mapper = mapper;
    this.configDir = foundConfigFile.getParent();
  }

  public ProcessedConfig from( RawConfig config ) {
    var fcs
      = config.getFileConfigurations()
      .stream()
      .map( this::from )
      .collect( Collectors.toList() );

    var processed = ImmutableProcessedConfig.builder().fileConfigurations( fcs ).build();
    log.debug( "processed: {}", processed );
    return processed;
  }

  ProcessedFileConfiguration from( RawFileConfiguration config ) {
    var context = this.getContext( config.getContext() );
    var bldr = ImmutableProcessedFileConfiguration.builder();
    bldr.overwrite( config.getOverwrite() );
    bldr.context( context );

    config.getSource().ifPresent( src -> {
      bldr.source( Path.of( this.processTemplate( src, context ) ) );
    } );
    return bldr.build();
  }

  @SuppressWarnings("unchecked")
  Map<String, Object> getContext( Map<String, String> context ) {
    var map = new HashMap<String, Object>();
    map.putAll( context );
    map.putAll( mapper.convertValue( cliConfiguration, Map.class ) );
    map.put( "configDir", configDir.getParent() );
    return Collections.unmodifiableMap( map );
  }

  String processTemplate( String template, Map<String, Object> context ) {
    var writer = new StringWriter();
    Try.run( () -> engine.getTemplate( template ).evaluate( writer, context ) ).get();
    return writer.toString();
  }
}