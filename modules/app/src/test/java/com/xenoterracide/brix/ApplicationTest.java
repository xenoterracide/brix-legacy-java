/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

  @Test
  void main() throws URISyntaxException {
    var resourceRoot = this.getClass().getClassLoader().getResource( "brix" ).toURI();
    var proj = RandomStringUtils.randomAlphanumeric( 10 );

    Application.exec( "--repo", Path.of( resourceRoot ).toString(), "java", "module", proj );

    assertThat( Path.of( proj ).resolve( "test.txt" ).toAbsolutePath() )
      .exists()
      .hasContent( "java" );
  }
}
