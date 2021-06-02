/*
 * Copyright © 2021 Caleb Cushing.
+ * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.service.console;

import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ConsoleConfiguration {
  @Bean
  ConsoleWrapper consoleWrapper() {
    return new ConsoleWrapper();
  }
}
