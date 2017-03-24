package com.jrvdev.vasl.board;

import com.jrvdev.IOUtils.IOUtilsx;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

// TODO: split this into a retriever and a writer
public class RepositoryRetriever {

    private String _url;
    String _fileNameToWrite;

    public RepositoryRetriever( String url, String fileNameToWrite ) {
        _url = url;
        _fileNameToWrite = fileNameToWrite;
    }

    public boolean getRepositoryFile()  {

        // Need to disable SNI to read from Github
        System.setProperty("jsse.enableSNIExtension", "false");

        FileOutputStream outFile = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try {
            URL website = new URL(_url.replace(" ", "%20"));
            conn = (HttpURLConnection) website.openConnection();
            conn.setUseCaches(false);

            in = conn.getInputStream();
            ReadableByteChannel rbc = Channels.newChannel(in);
            outFile = new FileOutputStream(_fileNameToWrite);
            outFile.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            return true;

        } catch (IOException e) {
            // Fail silently on any error
            //try {
                //System.out.println( "retriever: " + e.getMessage() + " " + conn.getResponseCode() );
            //}
            //catch (IOException ex ) {
                
            //}
            return false;
        }
        finally {
            IOUtilsx.closeQuietly(outFile);
            IOUtilsx.closeQuietly(in);
        }
    }
}