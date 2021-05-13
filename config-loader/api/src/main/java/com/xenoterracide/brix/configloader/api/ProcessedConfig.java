package com.xenoterracide.brix.configloader.api;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
public abstract class ProcessedConfig {

  @Value.Default
  public List<ProcessedFileConfiguration> getFileConfigurations() {
    return List.of();
  }

}
