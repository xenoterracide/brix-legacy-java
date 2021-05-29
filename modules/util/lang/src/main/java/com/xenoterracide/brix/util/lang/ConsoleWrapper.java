/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.util.lang;

public class ConsoleWrapper {
  public String readLine( String fmt, Object... args ) {
    var console = System.console();
    if ( console == null ) {
      throw new IllegalStateException( "unable to prompt for overwrite, console is not " +
        "available" );
    }
    var line = console.readLine( fmt, args );
    if ( line == null ) {
      throw new IllegalStateException( "stream should not be terminated" );
    }
    return line;
  }
}
