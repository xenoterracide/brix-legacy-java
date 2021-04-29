/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class ProcessedFileConfiguration {

  private final Map<String, @Nullable Object> context;

  private final @Nullable Boolean overwrite;

  private final @Nullable Path source;

  private final Path destination;

  ProcessedFileConfiguration(
    Map<String, @Nullable Object> context,
    @Nullable Boolean overwrite,
    @Nullable Path source,
    Path destination
  ) {
    this.context = context;
    this.overwrite = overwrite;
    this.source = source;
    this.destination = destination;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Map<String, @Nullable Object> getContext() {
    return context;
  }

  public @Nullable Boolean getOverwrite() {
    return overwrite;
  }

  public @Nullable Path getSource() {
    return source;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }

  public Path getDestination() {
    return destination;
  }

  @SuppressWarnings("checkstyle:HiddenField")
  public static final class Builder {
    private Map<String, @Nullable Object> context = Map.of();

    private @Nullable Boolean overwrite;

    private @Nullable Path source;

    private @MonotonicNonNull Path destination;

    private Builder() {
    }

    public Builder context( Map<String, @Nullable Object> context ) {
      this.context = context;
      return this;
    }

    public Builder overwrite( @Nullable Boolean overwrite ) {
      this.overwrite = overwrite;
      return this;
    }

    public Builder source( @Nullable Path source ) {
      this.source = source;
      return this;
    }

    public Builder destination( Path destination ) {
      this.destination = destination;
      return this;
    }

    public ProcessedFileConfiguration build() {
      Objects.requireNonNull( destination, "destination" );
      return new ProcessedFileConfiguration( context, overwrite, source, destination );
    }
  }
}
