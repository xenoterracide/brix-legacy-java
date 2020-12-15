/*
* Copyright © 2020 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

@Value.Immutable
@JsonDeserialize(as = ImmutableSkeletonConfiguration.class)
abstract class SkeletonConfiguration {

  @Value.Default
  @NotNull Map<String, String> getContext() {
    return Map.of();
  }

  abstract @Nullable Boolean getOverwrite();

  abstract @Nullable Pattern getAfter();

  abstract @NotNull Path getSource();

  abstract @NotNull Path getDestination();

  @Override
  public @NotNull String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
