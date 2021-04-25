package com.xenoterracide.brix.configloader.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.Map;

@Value.Immutable
public abstract class ProcessedFileConfiguration {

  public abstract Map<String, Object> getContext();

  public abstract @Nullable Boolean getOverwrite();

  public abstract @Nullable Path getSource();

  public abstract Path getDestination();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
