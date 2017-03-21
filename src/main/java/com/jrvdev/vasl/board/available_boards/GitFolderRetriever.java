package com.jrvdev.vasl.board.available_boards;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;

import java.nio.channels.Channels;

// TODO: If this fails to retrieve the contents, should signal that fact somehow besides returning
// an empty JSON String
class GitFolderRetriever implements IGitFolderRetriever {
    private final URL _url;
    private String _contentsJson = "";
    private boolean _contentsLoaded = false;

    public GitFolderRetriever( URL url ) {
        if ( url == null ) throw new IllegalArgumentException("url may not be null");
        _url = url;
    }

    private boolean loadRepositoryFile()  {

        // Need to disable SNI to read from Github
        System.setProperty("jsse.enableSNIExtension", "false");

        InputStream in = null;
        try {

            URLConnection conn = _url.openConnection();
            conn.setUseCaches(false);

            in = conn.getInputStream();
            _contentsJson = IOUtils.toString( in );

            return true;

        } catch (IOException e) {
            // Fail silently on any error
            return false;
        }
        finally {
            IOUtils.closeQuietly(in);
        }
    }

    public String getJSON() {
        if ( !_contentsLoaded ) {
            _contentsLoaded = loadRepositoryFile();
        }
        return _contentsJson;
    }
}