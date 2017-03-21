package com.jrvdev.vasl.version;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Hashtable;

import org.apache.commons.io.IOUtils;



public class MasterVersionLoader implements IMasterVersionLoader {

    private final IMasterVersionRetriever _retriever;
    

    public MasterVersionLoader( IMasterVersionRetriever retriever ) {
        _retriever = retriever;
    }

    public Hashtable Load() {
        Properties masterVersionProperties = new Properties();
        InputStream masterVersionInputStream = _retriever.GetVersions();
        try {
            masterVersionProperties.load(masterVersionInputStream);
        }
        catch ( IOException ex) {
            // fail silently if can't contact server'
        }
        finally {
            IOUtils.closeQuietly(masterVersionInputStream);
        }

        return masterVersionProperties;
    }

}