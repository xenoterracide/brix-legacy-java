/*
 * Copyright © 2020 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@CommandLine.Command( name = "brix" )
public final class Application implements Runnable {

  @SuppressWarnings( "NullAway.Init" )
  @CommandLine.Option(
    names = { "--root-log-level" },
    defaultValue = "error",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "change root logging level"
  )
  private final Level logLevel = Level.ERROR;

  @SuppressWarnings( { "NullAway.Init", "initialization.fields.uninitialized" } )
  @CommandLine.Option( names = { "--log-level" }, description = "log level of specific package" )
  private final Map<String, Level> levelMap = Map.of();

  @SuppressWarnings( { "NullAway.Init", "initialization.fields.uninitialized" } )
  @CommandLine.Option(
    names = { "--workdir" },
    defaultValue = "",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "The working directory you want your destination paths to be relative to." +
      " Defaults to current working directory"
  )
  private final Path workdir = Paths.get( "" );

  @SuppressWarnings( { "NullAway.Init", "initialization.fields.uninitialized" } )
  @CommandLine.Parameters( index = "0", description = "The programming language you're generating code for." +
    " Directory under --dir" )
  private String language;

  @SuppressWarnings( { "NullAway.Init", "initialization.fields.uninitialized" } )
  @CommandLine.Parameters( index = "1", description = "The type of code you're generating e.g controller," +
    " also the name of the config file without the extension." )
  private String moduleType;

  @SuppressWarnings( { "NullAway.Init", "initialization.fields.uninitialized" } )
  @CommandLine.Parameters( index = "2", description = "The name of the project you're generating code for." )
  private String project;

  @CommandLine.Parameters( index = "3", description = "The name of the module to be created within the project.",
    arity = "0" )
  private @Nullable String name;

  @SuppressWarnings( { "NullAway.Init", "initialization.fields.uninitialized" } )
  @CommandLine.Option(
    names = { "-d", "--dir" },
    defaultValue = ".config/brix",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "Directory path from the current working directory. " +
      "Templates and configs are looked up relative to here. If the config " +
      "isn't found here, then we will search ~/.config/brix"
  )
  private Path configDir;

  private final ConfigLoader configLoader = new ConfigLoader();

  public static void main( @NonNull String... args ) {
    var cli = new CommandLine( new Application() );
    cli.setCaseInsensitiveEnumValuesAllowed( true );
    cli.registerConverter( Level.class, Level::valueOf );
    cli.setExecutionExceptionHandler( new ExceptionHandler() );
    System.exit( cli.execute( args ) );
  }

  @Override
  public void run() {
    configureLog4j( logLevel, levelMap );
    var log = LogManager.getLogger( this.getClass() );
    log.debug( "processed args: {}", this );

    var configFile = getAbsoluteConfigFile();
    log.debug( "config file: {}", configFile );

    var config = configLoader.load( configFile );

    var templateProcessor = new PebbleTemplateProcessor( configFile.getParent(), workdir );
    config
      .entrySet()
      .forEach(
        e -> {
          var argMap = new HashMap<String, Object>();
          argMap.put( "language", language );
          argMap.put( "moduleType", moduleType );
          argMap.put( "project", project );
          argMap.put( "name", name );

          var context = new HashMap<String, Object>();
          context.putAll( argMap );
          context.putAll( e.getValue().getContext() );

          templateProcessor.process( e, Collections.unmodifiableMap( context ) );
        }
      );
  }

  private static void configureLog4j( Level rootLevel, Map<String, Level> levelMap ) {
    ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();

    AppenderComponentBuilder defaultAppender = builder.newAppender( "console", "CONSOLE" )
      .addAttribute( "target", ConsoleAppender.Target.SYSTEM_OUT );
    defaultAppender.add( builder.newLayout( "PatternLayout" )
      .addAttribute( "pattern", "%highlight{%-5level} - %msg%n%throwable" ) );

    builder.add( defaultAppender );
    builder.add( builder.newRootLogger( rootLevel ).add( builder.newAppenderRef( "console" ) ) );
    levelMap.forEach( ( s, level ) -> builder.add( builder.newLogger( s, level ) ) );
    Configurator.initialize( builder.build() );
  }

  @NonNull
  Path getAbsoluteConfigFile() {
    var filename = moduleType + ".yml";
    var relPathToConfigFIle = Path.of( language ).resolve( filename );
    var cwdConfigFile = configDir.resolve( relPathToConfigFIle );

    var home = SystemUtils.getUserHome().toPath();
    var confFile = Files.exists( cwdConfigFile ) ? cwdConfigFile : home.resolve( relPathToConfigFIle );
    return confFile.toAbsolutePath();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }

  static class ExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(
      Exception ex,
      CommandLine commandLine,
      CommandLine.ParseResult parseResult
    ) {
      var log = LogManager.getLogger( this.getClass() );
      log.debug( "", ex );
      log.error( ex );
      return 1;
    }
  }
}