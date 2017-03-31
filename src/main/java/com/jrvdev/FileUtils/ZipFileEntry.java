package com.jrvdev.FileUtils;

import java.io.InputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileEntry implements IFileEntry {

    private final String _name;
    private final ZipFile _zipFile;

    ZipFileEntry( String name, ZipFile zipFile ) {
        _name = name;
        _zipFile = zipFile;
    }

    public String getName() {
System.out.println( "viewing " + _name );
        return _name;
    }

    public InputStream getFileContents() throws IOException {
        final Enumeration<? extends ZipEntry> entries = _zipFile.entries();
        while (entries.hasMoreElements()){

            final ZipEntry entry = entries.nextElement();

            // if found return an InputStream
            if(entry.getName().equals(_name)){

                return _zipFile.getInputStream(entry);

            }
        }

        // file not found
        throw new IOException("Could not open the file '" + _name + "' in archive." );
    }


}