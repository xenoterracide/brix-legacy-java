package com.xenoterracide.brix;

import io.vavr.control.Try;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

@Value.Immutable
interface TestCliConfiguration extends CliConfiguration {

  @Override
  default Path getConfigDir() {
    return Try.ofSupplier( this::getClass )
      .map( Class::getClassLoader )
      .mapTry( cl -> cl.getResource( "config" ) )
      .filter( Objects::nonNull, t -> new IOException( "unable to find resource 'config'" ) )
      .mapTry( URL::toURI )
      .peek( uri -> LogManager.getLogger().error( "{}", uri ) )
      .mapTry( uri -> FileSystems.newFileSystem(uri, Map.of()).getPath("." ) )
      .get();
  }

  @Override
  default Path getWorkdir() {
    return Path.of( "." );
  }

  @Override
  default String getProject() {
    return RandomStringUtils.randomAlphabetic( 8 );
  }

  @Override
  default String getLanguage() {
    return "java";
  }

  @Override
  default String getModuleType() {
    return "project";
  }

  @Override
  default Level getLogLevel() {
    return Level.DEBUG;
  }

  @Override
  @Nullable String getName();
}
