/*
 * Copyright © 2021 Caleb Cushing.
+ * Apache 2.0. See https://github.com/xenoterracide/brix/LICENSE
 * https://choosealicense.com/licenses/apache-2.0/#
 */
package com.xenoterracide.brix.util.file;

import io.vavr.control.Try;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class FileService {
  private final Tika tika;

  private final ObjectFactory<MimeTypes> mimeTypes;

  FileService( Tika tika, ObjectFactory<MimeTypes> mimeTypes ) {
    this.tika = tika;
    this.mimeTypes = mimeTypes;
  }

  public boolean isBinary( Path path ) {
    return Try.of( () -> {
      var mimeType = this.detect( path );
      return mimeType.getType().getType().equals( "text" );
    } ).getOrElse( false );
  }

  public MimeType detect( Path path ) throws IOException, MimeTypeException {
    return mimeTypes.getObject().forName( tika.detect( path ) );
  }
}
