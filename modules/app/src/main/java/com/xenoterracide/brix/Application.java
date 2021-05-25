/*
 * Copyright © 2020-2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix;

import com.xenoterracide.brix.cli.api.CliConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class Application
  implements CommandLineRunner, ExitCodeGenerator, CommandLine.IExecutionExceptionHandler {

  private final Logger log = LogManager.getLogger( Application.class );

  private final CommandLine.IFactory factory;

  private final CliConfiguration cliCommand;

  private int exitCode;

  Application( CommandLine.IFactory factory, CliConfiguration cliCommand ) {
    this.factory = factory;
    this.cliCommand = cliCommand;
  }

  public static void main( String[] args ) {
    // let Spring instantiate and inject dependencies
    System.exit( SpringApplication.exit( SpringApplication.run( Application.class, args ) ) );
  }

  @Override
  public void run( String... args ) {
    var cli = new CommandLine( cliCommand, factory );
    cli.setCaseInsensitiveEnumValuesAllowed( true );
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
    log.error( ex );
    log.debug( "", ex );
    return 1;
  }
}
