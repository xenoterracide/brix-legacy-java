/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.cli.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.vavr.control.Try;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("checkstyle:HiddenField")
public class TestCliConfiguration implements CliConfiguration {
  private final @MonotonicNonNull String name;

  @JsonIgnore
  private final @MonotonicNonNull ClassLoader classLoader;

  private final String moduleType;

  private final String language;

  private final String project;

  TestCliConfiguration(
    @MonotonicNonNull String name,
    @MonotonicNonNull ClassLoader classLoader,
    String moduleType,
    String language,
    String project
  ) {
    this.name = name;
    this.classLoader = classLoader;
    this.moduleType = moduleType;
    this.language = language;
    this.project = project;
  }

  public static Builder builder() {
    return new Builder();
  }

  @Override
  public Optional<Path> getRepo() {
    return Try.ofSupplier( this::getClassLoader )
      .filter( Objects::nonNull )
      .mapTry( cl -> cl.getResource( "config" ) )
      .filter( Objects::nonNull, t -> new IOException( "unable to find resource 'config'" ) )
      .mapTry( URL::toURI )
      .map( Path::of )
      .map( Optional::of )
      .getOrElse( Optional.empty() );
  }

  @Override
  public Path getWorkdir() {
    return Path.of( "." );
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
  @Nullable
  public String getName() {
    return name;
  }

  @Nullable
  public ClassLoader getClassLoader() {
    return classLoader;
  }

  public static class Builder {
    private @MonotonicNonNull String name;

    private @MonotonicNonNull ClassLoader classLoader;

    private @MonotonicNonNull String moduleType;

    private @MonotonicNonNull String language;

    private @MonotonicNonNull String project;

    @SuppressWarnings("checkstyle:MagicNumber")
    public CliConfiguration build() {
      return new TestCliConfiguration(
        this.name,
        this.classLoader,
        Objects.requireNonNullElse( this.moduleType, "project" ),
        Objects.requireNonNullElse( this.language, "java" ),
        Objects.requireNonNullElseGet( this.project, () -> {
          var numberOfChars = 8;
          return RandomStringUtils.randomAlphabetic( numberOfChars );
        } )
      );
    }


    public Builder name( String name ) {
      this.name = name;
      return this;
    }

    public Builder classLoader( ClassLoader classLoader ) {
      this.classLoader = classLoader;
      return this;
    }

    public Builder moduleType( String moduleType ) {
      this.moduleType = moduleType;
      return this;
    }

    public Builder language( String language ) {
      this.language = language;
      return this;
    }

    public Builder project( String project ) {
      this.project = project;
      return this;
    }
  }
}
