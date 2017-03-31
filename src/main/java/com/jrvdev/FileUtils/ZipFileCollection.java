package com.jrvdev.FileUtils;

import java.io.IOException;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;


public class ZipFileCollection implements IFileCollection {
    private final String _zipFilePath;

    private ZipFile _zipFile = null;

    public ZipFileCollection( String zipFilePath ) {
        _zipFilePath = zipFilePath;
    }

    private void loadZipFileIfNecessary() throws IOException {
        if ( _zipFile == null ) {
            _zipFile = new ZipFile( _zipFilePath );
        }
    }


    @Override 
    public Set<IFileEntry> getEntries() throws IOException {
        HashSet<IFileEntry> zipEntries = new HashSet<IFileEntry>();
        loadZipFileIfNecessary();

        final Enumeration<? extends ZipEntry> entries = _zipFile.entries();
        while (entries.hasMoreElements()) {
            zipEntries.add( new ZipFileEntry( entries.nextElement().getName(), _zipFile ) );
        }

        return zipEntries;
    }

}