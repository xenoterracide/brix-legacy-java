/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.spi;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;
import java.util.Objects;


public class RawConfig {

  private final List<RawFileConfiguration> fileConfigurations;

  @JsonCreator
  RawConfig( List<RawFileConfiguration> fileConfigurations ) {
    this.fileConfigurations = Objects.requireNonNullElseGet( fileConfigurations, List::of );
  }

  public List<RawFileConfiguration> getFileConfigurations() {
    return fileConfigurations;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString( this, ToStringStyle.MULTI_LINE_STYLE );
  }
}
