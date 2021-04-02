/*
* Copyright © 2020-2021 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@CommandLine.Command(name = "brix")
public final class Application implements Runnable, CliConfiguration, LoggingConfiguration {

  @SuppressWarnings("NullAway.Init")
  @CommandLine.Option(
    names = { "--root-log-level" },
    defaultValue = "error",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "change root logging level"
  )
  private final Level logLevel = Level.ERROR;

  @SuppressWarnings({ "NullAway.Init", "initialization.field.uninitialized" })
  @CommandLine.Option(names = { "--log-level" }, description = "log level of specific package")
  private final Map<String, Level> levelMap = Map.of();

  @SuppressWarnings({ "NullAway.Init", "initialization.field.uninitialized" })
  @CommandLine.Option(
    names = { "--workdir" },
    defaultValue = "",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "The working directory you want your destination paths to be relative to." +
      " Defaults to current working directory"
  )
  private final Path workdir = Paths.get( "" );

  @SuppressWarnings({ "NullAway.Init", "initialization.field.uninitialized" })
  @CommandLine.Parameters(
    index = "0",
    description = "The programming language you're generating code for. Directory under --dir"
  )
  private String language;

  @SuppressWarnings({ "NullAway.Init", "initialization.field.uninitialized" })
  @CommandLine.Parameters(
    index = "1",
    description = "The type of code you're generating e.g controller, also the name of the config" +
      " file without the extension."
  )
  private String moduleType;

  @SuppressWarnings({ "NullAway.Init", "initialization.field.uninitialized" })
  @CommandLine.Parameters(
    index = "2",
    description = "The name of the project you're generating code for."
  )
  private String project;

  @CommandLine.Parameters(
    index = "3",
    description = "The name of the module to be created within the project.",
    arity = "0"
  )
  private @Nullable String name;

  @SuppressWarnings({ "NullAway.Init", "initialization.field.uninitialized" })
  @CommandLine.Option(
    names = { "-d", "--config-dir" },
    description = "Directory path from the current working directory. " +
      "Templates and configs are looked up relative to here. If the config " +
      "isn't found here, then we will search ~/.config/brix"
  )
  private @Nullable Path configDir;

  public static void main( @NonNull String... args ) {
    LogManager.getLogger().error( "IN APP ARGS: {}", (Object) args );
    var cli = new CommandLine( new Application() );
    cli.setCaseInsensitiveEnumValuesAllowed( true );
    cli.registerConverter( Level.class, Level::valueOf );
    cli.setExecutionExceptionHandler( new ExceptionHandler() );
    System.exit( cli.execute( args ) );
  }

  @Override
  public void run() {
    Coordinator.with( this ).run();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }

  @Override
  public Level getLogLevel() {
    return logLevel;
  }

  @Override
  public Map<String, Level> getLevelMap() {
    return levelMap;
  }

  @Override
  public Optional<Path> getConfigDir() {
    return Optional.ofNullable( configDir );
  }

  @Override
  public Path getWorkdir() {
    return workdir;
  }

  @Override
  public String getProject() {
    return project;
  }

  @Override
  public String getLanguage() {
    return language;
  }

  @Override
  public String getModuleType() {
    return moduleType;
  }

  @Override
  public @Nullable String getName() {
    return name;
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
