package com.xenoterracide.brix;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.Optional;

interface CliConfiguration extends LoggingConfiguration {
  Optional<Path> getConfigDir();

  Path getWorkdir();

  String getProject();

  String getLanguage();

  String getModuleType();

  @Nullable String getName();
}
