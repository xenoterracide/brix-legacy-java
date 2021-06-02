/*
 * Copyright © 2021 Caleb Cushing.
 * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.processor.api;

import com.xenoterracide.brix.configloader.api.ProcessedFileConfiguration;
import com.xenoterracide.brix.util.lang.ConsoleWrapper;
import org.apache.commons.io.file.PathUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

class AbstractProcessorBaseTest {

  private final ConsoleWrapper console = mock( ConsoleWrapper.class );

  private final AbstractProcessorBase processor = mock(
    AbstractProcessorBase.class,
    withSettings().useConstructor( console )
  );

  private Path destDir;

  @BeforeEach
  void setup() throws IOException {
    doCallRealMethod().when( processor ).process( any() );
    doCallRealMethod().when( processor ).askWhetherToWriteTemplate( any(), any(), any() );

    destDir = Files.createTempDirectory( this.getClass().getSimpleName() );
  }

  @AfterEach
  void teardown() throws IOException {
    PathUtils.deleteDirectory( destDir );
  }

  @Test
  void sourceNonNull() {
    var config = ProcessedFileConfiguration.builder()
      .source( null )
      .destination( Path.of( "dest" ) )
      .context( Map.of() )
      .build();

    assertThatThrownBy( () -> processor.process( config ) )
      .isInstanceOf( NullPointerException.class );
  }

  @Test
  void destHasParent() {
    var config = ProcessedFileConfiguration.builder()
      .source( Path.of( "source" ) )
      .destination( Path.of( "dest" ) )
      .context( Map.of() )
      .build();

    assertThatThrownBy( () -> processor.process( config ) )
      .isInstanceOf( IllegalArgumentException.class );
  }

  @Test
  void createParentDir() {
    var dest = destDir
      .resolve( "dest" )
      .resolve( "tmp.txt" );

    var config = ProcessedFileConfiguration.builder()
      .source( Path.of( "source" ) )
      .destination( dest )
      .context( Map.of() )
      .build();

    assertThat( dest.getParent() ).doesNotExist();

    processor.process( config );

    assertThat( dest.getParent() ).exists();
  }

  @Test
  void existsOverwriteNullYes() throws IOException {
    when(
      console.readLine(
        anyString(),
        any( Path.class )
      )
    ).thenReturn( "y" );
    var dest = Files.createFile( destDir.resolve( "tmp.txt" ) );

    var config = ProcessedFileConfiguration.builder()
      .source( Path.of( "source" ) )
      .destination( dest )
      .context( Map.of() )
      .overwrite( null )
      .build();

    processor.process( config );

    verify( processor, times(1) ).askWhetherToWriteTemplate( any(), any(), any() );
    verify( processor, times(1) ).writeTemplate( any(), any(), any() );
  }

  @Test
  void existsOverwriteNullNo() throws IOException {
    when(
      console.readLine(
        anyString(),
        any( Path.class )
      )
    ).thenReturn( "n" );
    var dest = Files.createFile( destDir.resolve( "tmp.txt" ) );

    var config = ProcessedFileConfiguration.builder()
      .source( Path.of( "source" ) )
      .destination( dest )
      .context( Map.of() )
      .overwrite( null )
      .build();

    processor.process( config );

    verify( processor, times(1) ).askWhetherToWriteTemplate( any(), any(), any() );
    verify( processor, never() ).writeTemplate( any(), any(), any() );
  }

  @Test
  void existsOverwriteTrue() throws IOException {
    var dest = Files.createFile( destDir.resolve( "tmp.txt" ) );

    var config = ProcessedFileConfiguration.builder()
      .source( Path.of( "source" ) )
      .destination( dest )
      .context( Map.of() )
      .overwrite( true )
      .build();

    processor.process( config );

    verify( processor, never() ).askWhetherToWriteTemplate( any(), any(), any() );
    verify( processor, times(1) ).writeTemplate( any(), any(), any() );
  }

  @Test
  void existsOverwriteFalse() throws IOException {
    var dest = Files.createFile( destDir.resolve( "tmp.txt" ) );

    var config = ProcessedFileConfiguration.builder()
      .source( Path.of( "source" ) )
      .destination( dest )
      .context( Map.of() )
      .overwrite( false )
      .build();

    processor.process( config );

    verify( processor, never() ).askWhetherToWriteTemplate( any(), any(), any() );
    verify( processor, never() ).writeTemplate( any(), any(), any() );
  }
}
