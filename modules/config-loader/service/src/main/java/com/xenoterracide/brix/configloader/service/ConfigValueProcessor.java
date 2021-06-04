/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.service;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.configloader.api.ImmutableProcessedConfig;
import com.xenoterracide.brix.configloader.api.ProcessedConfig;
import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.configloader.api.RawConfig;
import com.xenoterracide.brix.configloader.api.RawFileConfiguration;
import com.xenoterracide.brix.util.lang.ObjectUtils;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class ConfigValueProcessor {
  private final Logger log = LogManager.getLogger( ConfigValueProcessor.class );

  private final PebbleEngine engine;

  private final CliConfiguration cliConfiguration;

  ConfigValueProcessor(
    CliConfiguration cliConfiguration,
    PebbleEngine stringEngine
  ) {
    this.engine = stringEngine;
    this.cliConfiguration = cliConfiguration;
  }

  public ProcessedConfig from( Path toConfig, RawConfig config ) {
    var fcs
      = config.getFileConfigurations()
      .stream()
      .map( c -> this.from( toConfig, c ) )
      .collect( Collectors.toList() );

    var processed = ImmutableProcessedConfig.builder()
      .fileConfigurations( fcs )
      .build();
    log.debug( "processed: {}", processed );
    return processed;
  }

  ProcessedFileConfiguration from( Path toConfig, RawFileConfiguration config ) {
    var cwd = Path.of( "" ).toAbsolutePath();
    log.trace( "CWD: '{}'", cwd );
    var context = this.getContext( toConfig, config.getContext() );

    var bldr = ProcessedFileConfiguration.builder();
    bldr.overwrite( config.getOverwrite() );
    bldr.context( context );
    bldr.destination( cwd.resolve( this.processTemplate( config.getDestination(), context ) ) );

    config.getSource().ifPresent( src -> {
      var parent = ObjectUtils.requireNonNull( toConfig.getParent(), "config parent" );
      bldr.source( parent.resolve( this.processTemplate( src, context ) ) );
    } );
    return bldr.build();
  }

  @SuppressWarnings("unchecked")
  Map<String, @Nullable Object> getContext( Path toConfig, Map<String, String> context ) {
    var map = new HashMap<String, @Nullable Object>( context );
    map.put( "name", cliConfiguration.getName() );
    map.put( "moduleType", cliConfiguration.getModuleType() );
    map.put( "language", cliConfiguration.getLanguage() );
    map.put( "project", cliConfiguration.getProject() );
    map.put( "configDir", toConfig.getParent() );
    return Collections.unmodifiableMap( map );
  }

  String processTemplate( String template, Map<String, @Nullable Object> context ) {
    var writer = new StringWriter();
    Try.run( () -> engine.getTemplate( template ).evaluate( writer, context ) ).get();
    return writer.toString();
  }
}
