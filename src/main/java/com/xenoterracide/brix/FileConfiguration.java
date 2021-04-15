/*
* Copyright © 2020 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.Map;

@Value.Immutable
@JsonDeserialize(as = ImmutableFileConfiguration.class)
public abstract class FileConfiguration {

  @Value.Default
  public Map<String, String> getContext() {
    return Map.of();
  }

  public abstract @Nullable Boolean getOverwrite();

  public abstract @Nullable Path getSource();

  public abstract Path getDestination();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
