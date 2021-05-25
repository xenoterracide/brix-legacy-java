/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.dispatch;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import com.xenoterracide.brix.configloader.service.ConfigLoaderService;
import com.xenoterracide.brix.processor.api.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Dispatcher implements Runnable {
  private final Logger log = LogManager.getLogger( Dispatcher.class );

  private final List<Processor> processors;

  private final CliConfiguration cliConfig;

  private final ConfigLoaderService loaderService;

  Dispatcher(
    List<Processor> processors,
    CliConfiguration cliConfig,
    ConfigLoaderService loaderService
  ) {
    this.processors = processors;
    this.cliConfig = cliConfig;
    this.loaderService = loaderService;
  }

  @Override
  public void run() {
    log.debug( "processed args: {}", cliConfig );

    var config = loaderService.findAndLoad( cliConfig );

    config.getFileConfigurations().forEach( fileConfig -> {
      processors.forEach( p -> p.process( fileConfig ) );
    } );
  }

}
