/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@Value.Immutable
public abstract class ProcessorInstruction {

  public abstract Map<String, String> getContext();

  public abstract @Nullable Boolean getOverwrite();

  public abstract Path getConfigDir();

  public abstract Optional<Path> getSource();

  public abstract Path getDestination();

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
