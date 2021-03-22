package com.xenoterracide.brix;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfigLoaderTest {

  @Test
  void home() {
    var config = ImmutableTestCliConfiguration.builder().build();

    var loader = new ConfigLoader( config );

    assertThat( loader.pathToConfigFile() )
      .isEqualTo( SystemUtils.getUserDir().toPath().resolve( ".config/brix/project.yml" ) );

    assertThatThrownBy( () -> loader.load( loader.pathToConfigFile() ) )
      .hasMessage( "" );
  }
}
