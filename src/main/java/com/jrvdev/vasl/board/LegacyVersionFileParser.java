package com.jrvdev.vasl.board;

import com.jrvdev.FileUtils.IFileCollection;
import com.jrvdev.FileUtils.IFileEntry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;


public class LegacyVersionFileParser implements IVersionFileParser {
    private final String _legacyMetadataFileName = "data";
    private final String VERSION_KEY = "version";

    private boolean _parsed = false;
    private String _versionString = "";
    private IFileCollection _fileCollection;

    public LegacyVersionFileParser( IFileCollection fileCollection ) {
        _fileCollection = fileCollection;
    }

    private String getLegacyVersion() {
        InputStream dataFileStream = null;
        try { 
            dataFileStream = _fileCollection.getEntry( _legacyMetadataFileName ).getFileContents();
            return parseDataFile( dataFileStream );
        }
        catch ( IOException ex ) {
            System.out.println( "Error f " +  ex.getMessage() );
            return "";
        }
        catch ( NoSuchElementException ex ) {
            return "";
        }
        finally {
            IOUtils.closeQuietly( dataFileStream );
        }
    }

    private String parseDataFile( InputStream dataFileStream ) throws IOException {
        if (dataFileStream != null) {
            BufferedReader file = new BufferedReader(new InputStreamReader(dataFileStream));
            String s;
            while ((s = file.readLine()) != null) {
                String parsed = parseDataLine(s);
                if ( parsed.length() > 0 ) {
                    return parsed;
                }
            }
        }
        return "";
    }

    /**
     * Parses one line in the data file setting the appropriate attribute
     * @param s the line of text
     */
    private String parseDataLine(String s) {

        StringTokenizer st = new StringTokenizer(s);
        if (st.countTokens() >= 2) {
            String s1 = st.nextToken().toLowerCase();
            if ( s1.equals( VERSION_KEY ) ) {
                return st.nextToken();
            }
        }
        return "";
    }

    private void loadVersion() {
        InputStream legacyMetadataFile = null;
        try {
            legacyMetadataFile = _fileCollection.getEntry( _legacyMetadataFileName ).getFileContents();
            _versionString = parseDataFile( legacyMetadataFile );
        }
        catch ( IOException ex ) {
            _versionString = "";
        }
        catch ( NoSuchElementException ex ) {
            _versionString = "";
        }
        finally {
            IOUtils.closeQuietly( legacyMetadataFile );
        }
    }

    @Override
    public String getVersion() {
        if ( !_parsed ) {
            loadVersion();
            _parsed = true;
        }
        return _versionString;
    }
}