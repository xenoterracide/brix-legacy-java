package com.xenoterracide.brix.processor;

import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.Map;

@Component
class CopyProcessor implements Processor {

  @Override
  public boolean shouldProcess( ProcessorInstruction instruction ) {
    return false;
  }

  @Override
  public void process(
    Path configDir,
    ProcessorInstruction instruction,
    Map<String, Object> context
  ) {

  }
}
