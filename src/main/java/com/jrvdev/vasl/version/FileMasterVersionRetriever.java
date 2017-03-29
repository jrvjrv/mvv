package com.jrvdev.vasl.version;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class FileMasterVersionRetriever implements IMasterVersionRetriever {

    private final String _sourceFilename;
 
        public FileMasterVersionRetriever( String sourceFilename ) {
        _sourceFilename = sourceFilename;
    }

    public InputStream GetVersions() {
        try {
            File initialFile = new File(_sourceFilename);
            InputStream targetStream = new FileInputStream(initialFile);
            return targetStream;
        }
        catch ( FileNotFoundException ex ) {
            return new ByteArrayInputStream("".getBytes());
        }
/*       try {
            URL base = new URL( netUtils.encodeUrl( _sourceUri ) );
            URLConnection conn = base.openConnection();
            conn.setUseCaches(false);

            InputStream input = null;
            input = conn.getInputStream();
            return input;

        }
        catch (IOException e) {
//            System.out.println("mvv IOException");
//            
            // Fail silently if we can't contact the server
            return new ByteArrayInputStream("".getBytes());
        }
        finally {

        }
// */
    }
}