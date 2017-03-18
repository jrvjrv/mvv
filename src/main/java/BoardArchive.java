import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

// TODO: split this into many classes
public class BoardArchive implements IVersionedResource< BoardVersion > {

    private static final String _boardMetadataFileName = "BoardMetadata.xml"; // name of the board metadata file
    private static final String _boardMetadataElementName = "boardMetadata";
    private static final String _boardMetadataVersionAttrName = "version";

    private String _boardArchiveDir;
    private String _boardName;
    private String _boardArchivePath;
    private boolean _metadataLoaded;
    private boolean _hasNewVersion;
    private boolean _hasLegacyVersion;
    private BoardVersion _newBoardVersion;
    private BoardVersion _legacyBoardVersion;

    private Set<IWhiteListMatch> _legalNames;

    // boardName is without "bd" prefix
    public BoardArchive( String boardArchiveDir, String boardName, Set<IWhiteListMatch> legalNames ) {
        _boardArchiveDir = boardArchiveDir;
        _boardName = boardName;
        _boardArchivePath = _boardArchiveDir + System.getProperty("file.separator", "\\") + "bd" + _boardName;
        _legalNames = legalNames;
    }

    private InputStream getInputStreamForArchiveFile(ZipFile archive, String fileName) throws IOException {

        final Enumeration<? extends ZipEntry> entries = archive.entries();
        while (entries.hasMoreElements()){

            final ZipEntry entry = entries.nextElement();

            // if found return an InputStream
            if(entry.getName().equals(fileName)){

                return archive.getInputStream(entry);

            }
        }

        // file not found
        throw new IOException("Could not open the file '" + fileName + "' in archive " + _boardArchivePath );
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

    //ZipFile archive = new ZipFile(targetBoardFileName);
    // BoardArchive::ctor( ... )
    // BoardArchive::getInputStreamForArchiveFile( archive, boardMetadataFleName )
    // BoardMetadata::parseBoardMetadataFile
    private void loadMetaDataIfNecessary() {
        if ( !_metadataLoaded ) {
            ZipFile archive = null;
            try {
                archive = new ZipFile( _boardArchivePath );
                getNewVersion(archive);
                getLegacyVersion(archive);
            }
            catch ( IOException ex ) {
                System.out.println( "Error d " + _boardArchivePath + ex.getMessage() );
                setNullNewBoardVersion();
            }
            finally {
                IOUtilsx.closeQuietly(archive);
            }
            _metadataLoaded = true;
        }
    }

    private void getNewVersion(ZipFile archive ) {
        try {
            InputStream boardMetaDataStream = getInputStreamForArchiveFile( archive, _boardMetadataFileName );
            parseBoardMetadataFile( boardMetaDataStream );
        }
        catch ( IOException ex) {
            //System.out.println( "Error e " + _boardArchivePath + ex.getMessage() );
        }
    }

    private void getLegacyVersion( ZipFile archive ) {
        final String dataFileName = "data"; // name of the legacy data file
        InputStream dataFileStream = null;
        try { 
            dataFileStream = getInputStreamForArchiveFile(archive, dataFileName);
            parseDataFile( dataFileStream );
        }
        catch ( IOException ex ) {
            System.out.println( "Error f " + _boardArchivePath + ex.getMessage() );
        }
        finally {
            IOUtilsx.closeQuietly( dataFileStream );
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


    @Override public BoardVersion getVersion() {
        loadMetaDataIfNecessary();
        if ( _hasNewVersion ) {
            return _newBoardVersion;
        }
        return _legacyBoardVersion;
    }

    private void addBadNames( ZipFile archive, Set<String> badNames ) {
        final Enumeration<? extends ZipEntry> entries = archive.entries();
        while (entries.hasMoreElements()){

            final ZipEntry entry = entries.nextElement();
            if ( isBadName( entry.getName() )) {
                badNames.add( entry.getName() );
            }
        }
    }

    private boolean isBadName( String name ) {
        boolean isGood = false;

        for ( IWhiteListMatch matcher: _legalNames ) {
            isGood = matcher.isMatch( name ) || isGood;
        }

        return !isGood;

    }

    public Set<String> getBadNames() {
        HashSet<String> badNames = new HashSet<String>();

        ZipFile archive = null;
        try {
            archive = new ZipFile( _boardArchivePath );
            addBadNames( archive, badNames );
        }
        catch ( IOException ex ) {
            System.out.println( "Error g " + _boardArchivePath + ex.getMessage() );
            setNullNewBoardVersion();
        }
        finally {
            IOUtilsx.closeQuietly(archive);
        }
        return badNames;
    }
}