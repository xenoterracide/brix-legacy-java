/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;

// CHECKSTYLE:OFF HideUtilityClassConstructor FinalClass
@ActiveProfiles("dev")
@SpringBootApplication
public class TestApplication {

  // CHECKSTYLE:ON

  public static void main( String[] args ) {
    SpringApplication.run( TestApplication.class, args );
  }
}
