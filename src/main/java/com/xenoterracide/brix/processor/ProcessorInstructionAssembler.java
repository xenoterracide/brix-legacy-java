package com.xenoterracide.brix.processor;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.xenoterracide.brix.CliConfiguration;
import com.xenoterracide.brix.FileConfiguration;
import io.vavr.control.Try;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class ProcessorInstructionAssembler {

  private final CliConfiguration cliConfig;

  private final PebbleEngine engine;

  ProcessorInstructionAssembler( CliConfiguration cliConfig, PebbleEngine stringEngine ) {
    this.cliConfig = cliConfig;
    this.engine = stringEngine;
  }

  public ProcessorInstruction from( FileConfiguration config ) {
    Map<String, Object> context = Collections.unmodifiableMap( config.getContext() );
    var bldr = ImmutableProcessorInstruction.builder();

    this.getPath( this.cliConfig.getWorkdir(), config.getSource(), context )
      .ifPresent( bldr::source );
    this.getPath( this.cliConfig.getWorkdir(), config.getDestination(), context )
      .ifPresent( bldr::destination );

    return bldr.build();
  }

  Optional<Path> getPath(
    Path relativeTo,
    @Nullable Path pathTemplate,
    Map<String, Object> context
  ) {
    if ( pathTemplate == null ) {
      return Optional.empty();
    }
    var template = engine.getTemplate(
      relativeTo.resolve( pathTemplate ).toAbsolutePath().toString()
    );

    return Try.of( () -> {
      var writer = new StringWriter();
      template.evaluate( writer, context );
      return Path.of( writer.toString() ).toAbsolutePath();
    } )
      .map( Optional::of )
      .getOrElseThrow( e -> new RuntimeException( e ) );
  }

}
