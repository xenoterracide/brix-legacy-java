package com.xenoterracide.brix.processor;

import com.xenoterracide.brix.FileConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public interface Processor {
  void process(
    Map.@NonNull Entry<String, FileConfiguration> entry,
    @NonNull Map<String, Object> context
  );
}
