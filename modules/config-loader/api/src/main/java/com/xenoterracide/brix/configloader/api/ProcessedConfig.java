/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
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
