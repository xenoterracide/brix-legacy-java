package com.xenoterracide.brix;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
class CliRunner implements CommandLineRunner, ExitCodeGenerator, CommandLine.IExecutionExceptionHandler {

  private int exitCode = 0;
  private final CliCommand cliArgs;
  private final CommandLine.IFactory iFactory;

  CliRunner( CliCommand cliArgs, CommandLine.IFactory iFactory ) {
    this.cliArgs = cliArgs;
    this.iFactory = iFactory;
  }

  @Override
  public void run( String... args ) {
    var cli = new CommandLine( cliArgs, iFactory );
    cli.setCaseInsensitiveEnumValuesAllowed( true );
    cli.registerConverter( Level.class, Level::valueOf );
    cli.setExecutionExceptionHandler( this );
    this.exitCode = cli.execute( args );
  }

  @Override
  public int getExitCode() {
    return exitCode;
  }

  @Override
  public int handleExecutionException(
    Exception ex,
    CommandLine commandLine,
    CommandLine.ParseResult parseResult
  ) {
    var log = LogManager.getLogger( this.getClass() );
    log.debug( "", ex );
    log.error( ex );
    return 1;
  }
}
