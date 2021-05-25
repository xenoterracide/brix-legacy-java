/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.yaml;

import com.xenoterracide.brix.configloader.api.ConfigLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.io.IOException;

@SpringBootTest
class YamlConfigLoaderTest {

  @Autowired ConfigLoader loader;

  @Value("classpath:config/java/project.yaml")
  Resource configFile;

  @Test
  void load() throws IOException {
    this.loader.load( configFile.getFile().toPath() );
  }
}
