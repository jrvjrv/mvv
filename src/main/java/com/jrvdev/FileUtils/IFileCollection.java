package com.jrvdev.FileUtils;

import java.io.IOException;
import java.util.Set;

public interface IFileCollection {
    Set<IFileEntry> getEntries() throws IOException;
}