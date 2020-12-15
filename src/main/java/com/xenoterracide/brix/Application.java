/*
* Copyright © 2020 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.jetbrains.annotations.NotNull;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommandLine.Command( name = "skaf" )
public final class Application implements Runnable {

  @SuppressWarnings( {"NullAway.Init"} )
  @CommandLine.Parameters( index = "0", description = "first configuration directory" )
  private String arg;

  @CommandLine.Parameters(
    index = "1..*",
    description = "path to configuration directories separated by space"
  )
  @SuppressWarnings( {"NullAway.Init"} )
  private List<String> args;

  @SuppressWarnings( {"NullAway.Init"} )
  @CommandLine.Option(
    names = {"-d", "--dir"},
    defaultValue = ".config/scaf",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "Directory path from the current working directory. " +
      "Templates and configs are looked up relative to here"
  )
  private Path dir;

  @SuppressWarnings( "NullAway.Init" )
  @CommandLine.Option(
    names = {"--root-log-level"},
    defaultValue = "error",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "change root logging level"
  )
  private Level logLevel = Level.ERROR;

  @SuppressWarnings( {"NullAway.Init"} )
  @CommandLine.Option( names = {"--log-level"}, description = "log level of specific package" )
  private Map<String, Level> levelMap = Map.of();

  @SuppressWarnings( "NullAway.Init" )
  @CommandLine.Option(
    names = {"--workdir"},
    defaultValue = "",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "The working directory you want your destination paths to be relative to." +
      " Defaults to current working directory"
  )
  private Path workdir = Paths.get( "" );

  private final ConfigLoader configLoader = new ConfigLoader();

  public static void main( @NotNull String... args ) {
    var cli = new CommandLine( new Application() );
    cli.setCaseInsensitiveEnumValuesAllowed( true );
    cli.registerConverter( Level.class, Level::valueOf );
    System.exit( cli.execute( args ) );
  }

  @Override
  public void run() {
    configureLog4j( logLevel, levelMap );
    var log = LogManager.getLogger( this.getClass() );
    log.debug( "processed args: {}", this );

    var splitArgs = new ArrayList<>( args );

    var name = splitArgs.remove( splitArgs.size() - 1 );
    var fn = splitArgs.remove( splitArgs.size() - 1 ) + ".yml";

    var configDir = dir.resolve( Path.of( arg, splitArgs.toArray( new String[ 0 ] ) ) );
    var pathToConfig = configDir.resolve( fn ).toAbsolutePath();
    var config = configLoader.load( pathToConfig );

    var templateProcessor = new PebbleTemplateProcessor( configDir, workdir );
    config
      .entrySet()
      .forEach(
        e -> {
          Map<String, Object> context = new HashMap<>();
          context.putAll( Map.of( "args", args, "name", name ) );
          context.putAll( e.getValue().getContext() );
          templateProcessor.process( e, Collections.unmodifiableMap( context ) );
        }
      );
  }

  private static void configureLog4j( Level rootLevel, Map<String, Level> levelMap ) {
    var pattern = PatternLayout.newBuilder()
      .withPattern( "%highlight{[%t] %-5level: %msg%n%throwable}\n" )
      .build();
    var console = ConsoleAppender.createDefaultAppenderForLayout( pattern );
    console.start();

    var config = LoggerContext.getContext( false ).getConfiguration();

    var root = config.getRootLogger();
    var appenders = root.getAppenders();
    appenders.keySet().forEach( root::removeAppender );
    root.addAppender( console, rootLevel, null );
    Configurator.reconfigure( config );
    Configurator.setRootLevel( rootLevel );
    Configurator.setLevel( levelMap );
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
