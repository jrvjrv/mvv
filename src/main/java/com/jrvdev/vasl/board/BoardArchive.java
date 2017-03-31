package com.jrvdev.vasl.board;

import com.jrvdev.FileUtils.IFileCollection;
import com.jrvdev.FileUtils.IFileEntry;
import com.jrvdev.vasl.version.IVersionedResource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

// TODO: split this into many classes
public class BoardArchive implements IBoardArchive, IFileCollection {

    private static final String _boardMetadataFileName = "BoardMetadata.xml"; // name of the board metadata file
    private static final String _legacyMetadataFileName = "data";
    private static final String _boardMetadataElementName = "boardMetadata";
    private static final String _boardMetadataVersionAttrName = "version";

    //private String _boardArchiveDir;
    private IFileCollection _fileCollection;
    private String _boardName;
    //private String _boardArchivePath;
    private boolean _metadataLoaded;
    private boolean _hasNewVersion;
    private boolean _hasLegacyVersion;
    private BoardVersion _newBoardVersion;
    private BoardVersion _legacyBoardVersion;

    private Set<IWhiteListMatch> _legalNames;

    // boardName is without "bd"/"ovr" prefix
    public BoardArchive( IFileCollection fileCollection, String boardName, Set<IWhiteListMatch> legalNames ) { //, String prefix ) {
        _fileCollection = fileCollection;
        //_boardArchiveDir = boardArchiveDir;
        _boardName = boardName;
        //_boardArchivePath = _boardArchiveDir + System.getProperty("file.separator", "\\") + prefix + _boardName;
        _legalNames = legalNames;
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

    private void setNullLegacyBoardVersion() {
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
System.out.println( "looking for " + fileName );
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

    private void getLegacyVersion() {
        InputStream dataFileStream = null;
        try { 
            dataFileStream = getStreamByName( _legacyMetadataFileName );
            parseDataFile( dataFileStream );
        }
        catch ( IOException ex ) {
            System.out.println( "Error f " +  ex.getMessage() );
            setNullLegacyBoardVersion();
        }
        catch ( NoSuchElementException ex ) {
            setNullLegacyBoardVersion();
        }
        finally {
            IOUtils.closeQuietly( dataFileStream );
        }
    }

    private void parseDataFile( InputStream dataFileStream ) throws IOException {
        if (dataFileStream != null) {
            if ( !_hasNewVersion ) {
                setNullLegacyBoardVersion();
                _hasLegacyVersion = true;
            }
            BufferedReader file = new BufferedReader(new InputStreamReader(dataFileStream));
            String s;
            while ((s = file.readLine()) != null) {
                parseDataLine(s);
            }

        }
    }
    

    /**
     * Parses one line in the data file setting the appropriate attribute
     * @param s the line of text
     */
    private void parseDataLine(String s) {

        StringTokenizer st = new StringTokenizer(s);

        if (st.countTokens() >= 2) {
            final String VERSION_KEY = "version";
            String s1 = st.nextToken().toLowerCase();
            if ( s1.equals( VERSION_KEY ) ) {
                _hasLegacyVersion = true;
                _legacyBoardVersion = new BoardVersion( st.nextToken() );
            }
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

    private void addBadNames( Set<String> badNames ) throws IOException {

        this.getEntries().forEach(
            ( IFileEntry fileEntry ) -> {
                if ( isBadName( fileEntry.getName() )) {
                    badNames.add( fileEntry.getName() );
                }
            }
        );
    }

    private boolean isBadName( String name ) {
        boolean isGood = false;

        for ( IWhiteListMatch matcher: _legalNames ) {
            isGood = matcher.isMatch( name ) || isGood;
        }

        return !isGood;

    }

    public Set<String> getBadNames() throws IOException {
        HashSet<String> badNames = new HashSet<String>();

        addBadNames( badNames );
        return badNames;
    }


    @Override 
    public Set<IFileEntry> getEntries() throws IOException {

        return _fileCollection.getEntries();
        
    }

}