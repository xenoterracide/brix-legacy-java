/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.configloader.api;

import java.nio.file.Path;

public interface ConfigLoader {

  boolean canLoad( Path path );

  ProcessedConfig load( Path path );
}
