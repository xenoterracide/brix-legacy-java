package com.xenoterracide.brix;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.logging.log4j.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Component
public class CliCommand implements CliConfiguration, LoggingConfiguration {

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

  public void setLanguage( String language ) {
    this.language = language;
  }

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
}
