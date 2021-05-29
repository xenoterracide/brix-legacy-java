/*
* Copyright © 2020-2021 Caleb Cushing.
* Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
* https://choosealicense.com/licenses/apache-2.0/#
*/
package com.xenoterracide.brix.configloader.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xenoterracide.brix.configloader.api.ConfigLoader;
import com.xenoterracide.brix.configloader.api.RawConfig;
import io.vavr.control.Try;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
class YamlConfigLoader implements ConfigLoader {

  private final ObjectMapper mapper;

  private final MimeTypes mimeTypes;

  YamlConfigLoader(
    ObjectMapper yamlMapper,
    MimeTypes mimeTypes
  ) {
    this.mapper = yamlMapper;
    this.mimeTypes = mimeTypes;
  }

  @Override
  public RawConfig load( Path path ) throws IOException {
    return mapper.readValue( path.toFile(), RawConfig.class );
  }

  @Override
  public MimeType mimeType() {
    return Try.of( () -> mimeTypes.forName( "text/x-yaml" ) ).get();
  }
}
