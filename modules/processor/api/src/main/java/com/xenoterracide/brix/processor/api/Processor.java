/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.api;

import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;

public interface Processor {

  boolean shouldProcess( ProcessedFileConfiguration fc );

  void process( ProcessedFileConfiguration fc );
}
