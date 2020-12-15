/*
* Copyright © 2020 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;

@Value.Immutable
@JsonDeserialize(as = ImmutableConfig.class)
abstract class Config {

  @Nullable Path getWorkdirPath() {
    var workdir = this.getWorkdir();
    return workdir != null ? Path.of( workdir ) : null;
  }

  abstract @Nullable String getWorkdir();

  abstract @NotNull Map<String, SkeletonConfiguration> getTemplates();
}
