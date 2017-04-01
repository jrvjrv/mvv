package com.jrvdev.FileUtils;

import java.io.IOException;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;
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

    private ZipFile getZipFile() throws IOException {
        loadZipFileIfNecessary();
        return _zipFile;
    }


    @Override 
    public Set<IFileEntry> getEntries() throws IOException {
        HashSet<IFileEntry> zipEntries = new HashSet<IFileEntry>();

        final Enumeration<? extends ZipEntry> entries = getZipFile().entries();
        while (entries.hasMoreElements()) {
            zipEntries.add( new ZipFileEntry( entries.nextElement().getName(), _zipFile ) );
        }

        return zipEntries;
    }

    @Override
    public IFileEntry getEntry( String fileName ) throws NoSuchElementException, IOException {
        final Enumeration<? extends ZipEntry> entries = getZipFile().entries();
        while (entries.hasMoreElements()){

            final ZipEntry entry = entries.nextElement();

            // if found return an InputStream
            if(entry.getName().equals(fileName)){
                return new ZipFileEntry( entry.getName(), getZipFile() );
            }
        }

        throw new NoSuchElementException( "Not found " + fileName );

    }
}