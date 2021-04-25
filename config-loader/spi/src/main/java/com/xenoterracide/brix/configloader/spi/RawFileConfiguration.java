/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.spi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.util.Map;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableRawFileConfiguration.class)
public abstract class RawFileConfiguration {

  @Value.Default
  public Map<String, String> getContext() {
    return Map.of();
  }

  public abstract @Nullable Boolean getOverwrite();

  public abstract Optional<String> getSource();

  public abstract String getDestination();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
