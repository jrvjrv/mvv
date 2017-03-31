package com.jrvdev.FileUtils;

import java.io.InputStream;
import java.io.IOException;

public interface IFileEntry {
    String getName();
    InputStream getFileContents() throws IOException;
}