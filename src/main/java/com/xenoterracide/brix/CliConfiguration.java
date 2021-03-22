package com.xenoterracide.brix;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;

interface CliConfiguration extends LoggingConfiguration {
  Path getConfigDir();

  Path getWorkdir();

  String getProject();

  String getLanguage();

  String getModuleType();

  @Nullable String getName();
}
