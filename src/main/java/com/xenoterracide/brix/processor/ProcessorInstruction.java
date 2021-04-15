package com.xenoterracide.brix.processor;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Value.Immutable
abstract class ProcessorInstruction {

  abstract Map<String, String> getContext();

  abstract @Nullable Boolean getOverwrite();

  abstract Path getConfigDir();

  abstract Optional<Path> getSource();

  abstract Path getDestination();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
