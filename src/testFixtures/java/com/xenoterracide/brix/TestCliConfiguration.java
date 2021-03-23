package com.xenoterracide.brix;

import io.vavr.control.Try;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Level;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Value.Immutable
@Value.Style
interface TestCliConfiguration extends CliConfiguration {

  @Override
  @Value.Default
  default Optional<Path> getConfigDir() {
    return Try.ofSupplier( this::getClassLoader )
      .filter( Objects::nonNull )
      .mapTry( cl -> cl.getResource( "config" ) )
      .filter( Objects::nonNull, t -> new IOException( "unable to find resource 'config'" ) )
      .mapTry( URL::toURI )
      .map( Path::of )
      .map( Optional::of )
      .getOrElse( Optional.empty());
  }

  @Override
  @Value.Default
  default Path getWorkdir() {
    return Path.of( "." );
  }

  @Override
  @Value.Default
  default String getProject() {
    return RandomStringUtils.randomAlphabetic( 8 );
  }

  @Override
  @Value.Default
  default String getLanguage() {
    return "java";
  }

  @Override
  @Value.Default
  default String getModuleType() {
    return "project";
  }

  @Override
  @Value.Default
  default Level getLogLevel() {
    return Level.DEBUG;
  }

  @Nullable ClassLoader getClassLoader();

  @Override
  @Nullable String getName();
}
