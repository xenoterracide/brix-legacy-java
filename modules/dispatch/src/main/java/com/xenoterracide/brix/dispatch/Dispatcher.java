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
import java.util.function.Consumer;

@Component
public class Dispatcher implements Consumer<CliConfiguration> {
  private final Logger log = LogManager.getLogger( Dispatcher.class );

  private final List<Processor> processors;

  private final ConfigLoaderService loaderService;

  Dispatcher(
    List<Processor> processors,
    ConfigLoaderService loaderService
  ) {
    this.processors = processors;
    this.loaderService = loaderService;
  }

  @Override
  public void accept( CliConfiguration cliConfiguration ) {
    log.debug( "processed args: {}", cliConfiguration );

    var config = loaderService.findAndLoad( cliConfiguration );

    config.getFileConfigurations().forEach( fileConfig -> {
      processors.forEach( p -> p.process( fileConfig ) );
    } );
  }
}
