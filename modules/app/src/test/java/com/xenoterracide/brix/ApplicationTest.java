/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

  @Test
  void main() throws URISyntaxException, IOException {
    var resourceRoot = this.getClass().getClassLoader().getResource( "brix" ).toURI();
    var proj = "tmp-" + UUID.randomUUID().toString();

    Application.exec( "--repo", Path.of( resourceRoot ).toString(), "java", "module", proj );

    var projPath = Path.of( proj );
    assertThat( projPath.resolve( "test.txt" ).toAbsolutePath() )
      .exists()
      .hasContent( "java" );

    FileSystemUtils.deleteRecursively( projPath );
  }
}
