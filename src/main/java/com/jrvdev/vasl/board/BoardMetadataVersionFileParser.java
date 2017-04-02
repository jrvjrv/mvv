package com.jrvdev.vasl.board;

import com.jrvdev.FileUtils.IFileCollection;
import com.jrvdev.FileUtils.IFileEntry;

import java.io.InputStream;
import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class BoardMetadataVersionFileParser implements IVersionFileParser {

    private IFileCollection _fileCollection;
    private boolean _parsed = false;
    private String _versionString = "";

    private static final String _boardMetadataFileName = "BoardMetadata.xml"; // name of the board metadata file
    private static final String _boardMetadataElementName = "boardMetadata";
    private static final String _boardMetadataVersionAttrName = "version";

    public BoardMetadataVersionFileParser( IFileCollection fileCollection ) {
        _fileCollection = fileCollection;
    }

    private String parseBoardMetadataFile(InputStream metadata) {
        String version = "";
        try {
            SAXBuilder parser = new SAXBuilder();

            // the root element will be the boardMetadata element
            Document doc = parser.build(metadata);
            Element root = doc.getRootElement();


            if (root.getName().equals(_boardMetadataElementName)){
                // read the board-level metadata
                version = root.getAttributeValue(_boardMetadataVersionAttrName);
            }
            else {
                version = "";
            }
        }
        catch ( IOException ex ) {
            version = "";
        }
        catch ( JDOMException ex ) {
            version = "";
        }

        return version;
    }

    private void loadVersion() {
        InputStream boardMetaDataStream = null;
        try {
            boardMetaDataStream = _fileCollection.getEntry( _boardMetadataFileName ).getFileContents();
            _versionString = parseBoardMetadataFile( boardMetaDataStream );
        }
        catch ( IOException ex) {
            _versionString = "";
            //System.out.println( "Error e " + _boardArchivePath + ex.getMessage() );
        }
        catch ( NoSuchElementException ex ) {
            _versionString = "";
            // does not have the 
        }
        finally {
            IOUtils.closeQuietly( boardMetaDataStream );
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