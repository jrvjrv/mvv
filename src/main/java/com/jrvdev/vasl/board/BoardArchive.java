package com.jrvdev.vasl.board;

import com.jrvdev.FileUtils.IFileCollection;
import com.jrvdev.FileUtils.IFileEntry;
import com.jrvdev.vasl.version.IVersionedResource;

import java.io.InputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

// TODO: split this into many classes
public class BoardArchive implements IBoardArchive, IFileCollection {

    private static final String _boardMetadataFileName = "BoardMetadata.xml"; // name of the board metadata file
    private static final String _boardMetadataElementName = "boardMetadata";
    private static final String _boardMetadataVersionAttrName = "version";

    private IFileCollection _fileCollection;
    private String _boardName;
    private boolean _metadataLoaded;
    private boolean _hasNewVersion;
    private BoardVersion _newBoardVersion;

    private boolean _hasLegacyVersion;
    private BoardVersion _legacyBoardVersion;
    private IVersionFileParser _legacyVersionFileParser;

    // boardName is without "bd"/"ovr" prefix
    public BoardArchive( IFileCollection fileCollection, String boardName, IVersionFileParser legacyParser ) {
        _fileCollection = fileCollection;
        _boardName = boardName;
        _legacyVersionFileParser = legacyParser;
    }

    private void parseBoardMetadataFile(InputStream metadata) {
        try {
            SAXBuilder parser = new SAXBuilder();

            // the root element will be the boardMetadata element
            Document doc = parser.build(metadata);
            Element root = doc.getRootElement();


            if (root.getName().equals(_boardMetadataElementName)){
                // read the board-level metadata
                _newBoardVersion = new BoardVersion( root.getAttributeValue(_boardMetadataVersionAttrName) );
                _hasNewVersion = true;
            }
            else {
                System.out.println( "Error a" );
                setNullNewBoardVersion();
                // do what???
            }
        }
        catch ( IOException ex ) {
                System.out.println( "Error b" );
            setNullNewBoardVersion();
        }
        catch ( JDOMException ex ) {
                System.out.println( "Error c" );
            setNullNewBoardVersion();
        }
    }

    private void setNullNewBoardVersion() {
        _newBoardVersion = new BoardVersion( "" );
    }

    private void getLegacyVersion() {
        String version = _legacyVersionFileParser.getVersion();
        if ( version == null ) version = "";
        version = version.trim();

        if ( version.length() > 0 ) {
            _hasLegacyVersion = true;
        }
        else {
            _hasLegacyVersion = false;
        }
        _legacyBoardVersion = new BoardVersion( version );
    }

    private void setNullLegacyBoardVersion() {
        _hasLegacyVersion = false;
        _legacyBoardVersion = new BoardVersion( "" );
    }

    @Override public String getName() {
        return _boardName;
    }

    private void loadMetaDataIfNecessary() {
        if ( !_metadataLoaded ) {
            getNewVersion();
            getLegacyVersion();
            _metadataLoaded = true;
        }
    }

    private InputStream getStreamByName( String fileName ) throws IOException {
        return _fileCollection.getEntries().stream().filter( ( entry )-> { return entry.getName().equals( fileName );}).findFirst().get().getFileContents();
    }

    private void getNewVersion() {
        InputStream boardMetaDataStream = null;
        try {
            boardMetaDataStream = getStreamByName( _boardMetadataFileName );
            parseBoardMetadataFile( boardMetaDataStream );
        }
        catch ( IOException ex) {
            setNullNewBoardVersion();
            //System.out.println( "Error e " + _boardArchivePath + ex.getMessage() );
        }
        catch ( NoSuchElementException ex ) {
            setNullNewBoardVersion();
            // does not have the 
        }
        finally {
            IOUtils.closeQuietly( boardMetaDataStream );
        }
    }

    @Override 
    public BoardVersion getVersion() {
        loadMetaDataIfNecessary();
        if ( _hasNewVersion ) {
            return _newBoardVersion;
        }
        return _legacyBoardVersion;
    }

    @Override 
    public Set<IFileEntry> getEntries() throws IOException {
        return _fileCollection.getEntries();
        
    }

    @Override
    public IFileEntry getEntry( String fileName )  throws IOException, NoSuchElementException {
        return _fileCollection.getEntry( fileName );
    }

}