/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.svc;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TikaConfiguration {

  @Bean
  TikaConfig tikaConfig() {
    return TikaConfig.getDefaultConfig();
  }

  @Bean
  Tika tika( TikaConfig config ) {
    return new Tika( config );
  }

  @Bean
  MimeTypes mimeRepository( TikaConfig config ) {
    return config.getMimeRepository();
  }
}
