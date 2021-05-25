/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.pebble;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProcessorConfiguration {

  @Bean
  ConsoleWrapper consoleWrapper() {
    return new ConsoleWrapper();
  }

  @Bean
  PebbleEngine stringEngine() {
    return new PebbleEngine.Builder()
      .newLineTrimming( false )
      .strictVariables( true )
      .loader( new StringLoader() )
      .build();
  }

  @Bean
  PebbleEngine fileEngine() {
    return new PebbleEngine.Builder()
      .newLineTrimming( false )
      .strictVariables( true )
      .loader( new FileLoader() )
      .build();
  }
}
