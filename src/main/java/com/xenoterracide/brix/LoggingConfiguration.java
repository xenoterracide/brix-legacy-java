package com.xenoterracide.brix;

import org.apache.logging.log4j.Level;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

interface LoggingConfiguration {
  @NonNull
  Level getLogLevel();

  @NonNull
  Map<String, Level> getLevelMap();
}
