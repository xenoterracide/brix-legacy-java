package com.xenoterracide.brix;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

interface TemplateProcessor {
  void process(
    Map.@NonNull Entry<String, SkeletonConfiguration> entry,
    @NonNull Map<String, Object> context
  );
}
