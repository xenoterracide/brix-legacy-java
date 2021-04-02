package com.xenoterracide.brix;

import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ConfigLoaderTest {

  @Test
  void homeNotPresent() {
    var config
      = ImmutableTestCliConfiguration.builder()
      .classLoader( this.getClass().getClassLoader() )
      .configDir( Optional.empty() )
      .moduleType( "missing" )
      .build();

    var loader = new ConfigLoader( config );

    var home = SystemUtils.getUserDir().toPath();
    assertThat( loader.pathToConfigFile() )
      .isEqualTo( home.resolve( ".config/brix/java/missing.yml" ) );

    assertThatThrownBy( () -> loader.load( loader.pathToConfigFile() ) )
      .isInstanceOf( FileNotFoundException.class );
  }

  @Test
  void configPresent() {
    var config
      = ImmutableTestCliConfiguration.builder()
      .moduleType( "module" )
      .build();

    var loader = new ConfigLoader( config );

    var path = Path.of( ".config/brix/java/module.yml" ).toAbsolutePath();

    assertThat( loader.pathToConfigFile() ).isEqualTo( path );

    assertThat( loader.load( loader.pathToConfigFile() ) ).isNotEmpty();
  }
}
