package com.xenoterracide.brix.processor;

import java.nio.file.Path;
import java.util.Map;

public interface Processor {

  boolean shouldProcess( ProcessorInstruction instruction );

  void process(
    Path configDir,
    ProcessorInstruction instruction,
    Map<String, Object> context
  );
}
