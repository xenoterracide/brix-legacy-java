package com.xenoterracide.brix;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;

interface CliConfiguration extends LoggingConfiguration {
  @NonNull Path getConfigDir();

  @NonNull Path getWorkdir();

  @NonNull String getProject();

  @NonNull String getLanguage();

  @NonNull String getModuleType();

  @Nullable String getName();
}
