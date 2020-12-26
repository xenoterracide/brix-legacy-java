/*
* Copyright © 2020 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;

@Value.Immutable
@JsonDeserialize(as = ImmutableSkeletonConfiguration.class)
abstract class SkeletonConfiguration {

  @Value.Default
  @NonNull Map<String, String> getContext() {
    return Map.of();
  }

  abstract @Nullable Boolean getOverwrite();

  abstract @Nullable Pattern getAfter();

  abstract @NonNull Path getSource();

  abstract @NonNull Path getDestination();

  @Override
  public @NonNull String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}