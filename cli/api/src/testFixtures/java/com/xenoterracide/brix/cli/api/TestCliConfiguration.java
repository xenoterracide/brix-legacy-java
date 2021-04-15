/*
* Copyright © 2021 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix.cli.api;

import io.vavr.control.Try;
import org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@Value.Immutable
interface TestCliConfiguration extends CliConfiguration {

  @Override
  @Value.Default
  @SuppressWarnings("immutables:untype")
  default Optional<Path> getConfigDir() {
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
  @Value.Default
  default Path getWorkdir() {
    return Path.of( "." );
  }

  @Override
  @Value.Default
  default String getProject() {
    var numberOfChars = 8;
    return RandomStringUtils.randomAlphabetic( numberOfChars );
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

  @Nullable
  ClassLoader getClassLoader();

  @Override
  @Nullable
  String getName();
}
