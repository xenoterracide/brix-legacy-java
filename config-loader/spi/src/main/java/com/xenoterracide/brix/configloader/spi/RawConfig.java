/*
 * Copyright © 2020 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.spi;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(deepImmutablesDetection = true)
@JsonDeserialize(as = ImmutableRawConfig.class)
public abstract class RawConfig {

  @Value.Default
  public List<RawFileConfiguration> getFileConfigurations() {
    return List.of();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
