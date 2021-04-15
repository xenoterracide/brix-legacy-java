package com.xenoterracide.brix;

import com.xenoterracide.brix.processor.Processor;
import com.xenoterracide.brix.processor.ProcessorInstructionAssembler;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
class Dispatcher implements Runnable {
  private final List<Processor> processors;

  private final CliConfiguration cliConfig;

  private final ProcessorInstructionAssembler assembler;

  private final ConfigLoader configLoader;

  Dispatcher(
    CliConfiguration cliConfig,
    ConfigLoader configLoader,
    List<Processor> processors,
    ProcessorInstructionAssembler assembler
  ) {
    this.cliConfig = cliConfig;
    this.configLoader = configLoader;
    this.processors = processors;
    this.assembler = assembler;
  }

  @Override
  public void run() {
    var log = LogManager.getLogger( this.getClass() );
    log.debug( "processed args: {}", cliConfig );

    var configFile = configLoader.pathToConfigFile();
    log.debug( "config file: {}", configFile );

    configLoader.load( configFile ).forEach(
      fileConfig -> {
        var argMap = new HashMap<String, Object>();
        argMap.put( "language", cliConfig.getLanguage() );
        argMap.put( "moduleType", cliConfig.getModuleType() );
        argMap.put( "project", cliConfig.getProject() );
        argMap.put( "name", cliConfig.getName() );

        var context = new HashMap<>( argMap );
        context.putAll( fileConfig.getContext() );

        var instruction = assembler.from( fileConfig );

        processors.forEach( p -> p.process( configFile, instruction, context ) );
      }
    );
  }
}
