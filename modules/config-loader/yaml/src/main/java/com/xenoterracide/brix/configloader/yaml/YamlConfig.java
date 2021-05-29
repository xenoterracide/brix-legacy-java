/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.yaml;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration()
class YamlConfig {

  @Bean
  ObjectMapper yamlMapper() {
    return new ObjectMapper( new YAMLFactory() )
      .registerModule( new ParameterNamesModule() )
      .enable( DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS )
      .enable( DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES )
      .enable( DeserializationFeature.WRAP_EXCEPTIONS )
      .enable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
  }
}
