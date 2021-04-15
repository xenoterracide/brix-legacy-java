package com.xenoterracide.brix.processor;

class ConsoleWrapper {
  String readLine( String fmt, Object... args ) {
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
