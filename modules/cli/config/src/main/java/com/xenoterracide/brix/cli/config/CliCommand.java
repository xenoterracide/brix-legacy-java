package com.xenoterracide.brix.cli.config;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.dispatch.Dispatcher;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Component
public class CliCommand implements CliConfiguration, Runnable {

  private final Dispatcher dispatcher;

  private Path workdir = Paths.get( "" );

  private String language;

  private String moduleType;

  private String project;

  private @MonotonicNonNull String name;

  private @MonotonicNonNull Path repo;

  CliCommand( Dispatcher dispatcher ) {
    this.dispatcher = dispatcher;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }

  @Override
  public Optional<Path> getRepo() {
    return Optional.ofNullable( repo );
  }

  @CommandLine.Option(
    names = {"--repo"},
    description = "Repository path from the current working directory. " +
      "Templates and configs are looked up relative to here. If the config " +
      "isn't found here, then we will search ~/.config/brix"
  )
  public void setRepo( Path repo ) {
    this.repo = repo;
  }

  @Override
  public Path getWorkdir() {
    return workdir;
  }

  @CommandLine.Option(
    names = {"--workdir"},
    defaultValue = "",
    showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
    description = "The working directory you want your destination paths to be relative to." +
      " Defaults to current working directory"
  )
  public void setWorkdir( Path workdir ) {
    this.workdir = workdir;
  }

  @Override
  public String getProject() {
    return project;
  }

  @CommandLine.Parameters(
    index = "2",
    description = "The name of the project you're generating code for."
  )
  public void setProject( String project ) {
    this.project = project;
  }

  @Override
  public String getLanguage() {
    return language;
  }

  @Override
  public String getModuleType() {
    return moduleType;
  }

  @CommandLine.Parameters(
    index = "1",
    description = "The type of code you're generating e.g controller, also the name of the config" +
      " file without the extension."
  )
  public void setModuleType( String moduleType ) {
    this.moduleType = moduleType;
  }

  @Override
  public @MonotonicNonNull String getName() {
    return name;
  }

  @CommandLine.Parameters(
    index = "3",
    description = "The name of the module to be created within the project.",
    arity = "0"
  )
  public void setName( String name ) {
    this.name = name;
  }

  @CommandLine.Parameters(
    index = "0",
    description = "The programming language you're generating code for. Directory under --dir"
  )
  public void setLanguage( String language ) {
    this.language = language;
  }

  @Override
  public void run() {
    dispatcher.run();
  }
}
