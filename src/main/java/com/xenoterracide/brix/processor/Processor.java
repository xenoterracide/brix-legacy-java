package com.xenoterracide.brix.processor;

import com.xenoterracide.brix.FileConfiguration;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public interface Processor {
  void process(
    @NonNull FileConfiguration fileConfig,
    @NonNull Map<String, Object> context
  );
}
