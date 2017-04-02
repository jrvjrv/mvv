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


// TODO: split this into many classes
public class BoardArchive implements IBoardArchive, IFileCollection {


    private IFileCollection _fileCollection;
    private String _boardName;
    private boolean _metadataLoaded;
    private boolean _hasNewVersion;
    private BoardVersion _newBoardVersion;

    private boolean _hasLegacyVersion;
    private BoardVersion _legacyBoardVersion;
    private IVersionFileParser _legacyVersionFileParser;
    private IVersionFileParser _boardMetadataVersionFileParser;

    // boardName is without "bd"/"ovr" prefix
    public BoardArchive( IFileCollection fileCollection, String boardName, IVersionFileParser legacyParser, IVersionFileParser boardMetadataVersionFileParser ) {
        _fileCollection = fileCollection;
        _boardName = boardName;
        _legacyVersionFileParser = legacyParser;
        _boardMetadataVersionFileParser = boardMetadataVersionFileParser;
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

    private void getNewVersion() {
        String version = _boardMetadataVersionFileParser.getVersion();
        if ( version == null ) version = "";
        version = version.trim();

        if ( version.length() > 0 ) {
            _hasNewVersion = true;
        }
        else {
            _hasNewVersion = false;
        }
        _newBoardVersion = new BoardVersion( version );
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