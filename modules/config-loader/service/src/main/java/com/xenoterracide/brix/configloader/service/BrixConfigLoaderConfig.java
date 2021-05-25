/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BrixConfigLoaderConfig {

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  PebbleEngine stringEngine() {
    return new PebbleEngine.Builder()
      .newLineTrimming( false )
      .strictVariables( true )
      .loader( new StringLoader() )
      .build();
  }
}
