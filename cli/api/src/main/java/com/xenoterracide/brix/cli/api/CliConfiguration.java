/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.cli.api;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.Optional;

public interface CliConfiguration {
  Optional<Path> getConfigDir();

  Path getWorkdir();

  String getProject();

  String getLanguage();

  String getModuleType();

  @Nullable String getName();
}
