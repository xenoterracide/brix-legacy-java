package com.xenoterracide.brix;

import org.apache.logging.log4j.Level;

import java.util.Map;

interface LoggingConfiguration {
  Level getLogLevel();

  Map<String, Level> getLevelMap();
}
