/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class RawFileConfiguration {

  private final Map<String, String> context;

  private final @Nullable Boolean overwrite;

  private final @Nullable String source;

  private final String destination;

  @JsonCreator
  RawFileConfiguration(
    Map<String, String> context,
    @Nullable Boolean overwrite,
    @Nullable String source,
    String destination
  ) {
    this.context = Objects.requireNonNullElse( context, Map.of() );
    this.overwrite = overwrite;
    this.source = source;
    this.destination = destination;
  }

  public Map<String, String> getContext() {
    return context;
  }

  public @Nullable Boolean getOverwrite() {
    return overwrite;
  }

  public Optional<String> getSource() {
    return Optional.ofNullable( source );
  }

  public String getDestination() {
    return destination;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
