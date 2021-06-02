/*
 * Copyright © 2021 Caleb Cushing.
+ * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.service.console;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
class TikaConfiguration {

  @Bean
  @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
  TikaConfig tikaConfig() {
    return TikaConfig.getDefaultConfig();
  }

  @Bean
  @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
  Tika tika( TikaConfig config ) {
    return new Tika( config );
  }

  @Bean
  MimeTypes mimeTypes( TikaConfig config ) {
    return config.getMimeRepository();
  }
}
