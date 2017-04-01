package com.jrvdev.FileUtils;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Set;

public interface IFileCollection {
    Set<IFileEntry> getEntries() throws IOException;
    IFileEntry getEntry( String fileName ) throws IOException, NoSuchElementException;
}