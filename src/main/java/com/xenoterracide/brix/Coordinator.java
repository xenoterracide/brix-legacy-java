package com.xenoterracide.brix;

import com.xenoterracide.brix.processor.PebbleTemplateProcessor;
import com.xenoterracide.brix.processor.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.BiFunction;

final class Coordinator implements Runnable {
  private final BiFunction<Path, Path, Processor> processorFactory;

  private final CliConfiguration cliConfig;

  private final ConfigLoader configLoader;

  private Coordinator(
    CliConfiguration cliConfig,
    ConfigLoader configLoader,
    BiFunction<Path, Path, Processor> processorFactory
  ) {
    this.cliConfig = cliConfig;
    this.configLoader = configLoader;
    this.processorFactory = processorFactory;
    configureLog4j( cliConfig );
  }

  private static void configureLog4j( LoggingConfiguration config ) {
    var console = "console";
    var builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    var defaultAppender = builder.newAppender( console, "CONSOLE" )
      .addAttribute( "target", ConsoleAppender.Target.SYSTEM_OUT );
    defaultAppender.add( builder.newLayout( "PatternLayout" )
      .addAttribute( "pattern", "%highlight{%-5level} - %msg%n%throwable" ) );

    builder.add( defaultAppender );
    builder.add( builder.newRootLogger( config.getLogLevel() )
      .add( builder.newAppenderRef( console ) ) );
    config.getLevelMap().forEach( ( s, level ) -> builder.add( builder.newLogger( s, level ) ) );
    Configurator.initialize( builder.build() );
  }

  static Coordinator with( @NonNull CliConfiguration cliConfig ) {
    return with( cliConfig, new ConfigLoader( cliConfig ), PebbleTemplateProcessor::new );
  }

  static Coordinator with(
    @NonNull CliConfiguration cliConfig,
    @NonNull ConfigLoader configLoader,
    @NonNull BiFunction<Path, Path, Processor> templateProcessorFactory
  ) {
    return new Coordinator( cliConfig, configLoader, templateProcessorFactory );
  }

  @Override
  public void run() {
    var log = LogManager.getLogger( this.getClass() );
    log.debug( "processed args: {}", cliConfig );

    var configFile = configLoader.pathToConfigFile();
    log.debug( "config file: {}", configFile );

    var config = configLoader.load( configFile );

    var templateProcessor = processorFactory.apply(
      configFile.getParent(),
      cliConfig.getWorkdir()
    );
    config
      .values()
      .forEach(
        fileConfig -> {
          var argMap = new HashMap<String, Object>();
          argMap.put( "language", cliConfig.getLanguage() );
          argMap.put( "moduleType", cliConfig.getModuleType() );
          argMap.put( "project", cliConfig.getProject() );
          argMap.put( "name", cliConfig.getName() );

          var context = new HashMap<String, Object>();
          context.putAll( argMap );
          context.putAll( fileConfig.getContext() );

          templateProcessor.process( fileConfig, Collections.unmodifiableMap( context ) );
        }
      );
  }
}
