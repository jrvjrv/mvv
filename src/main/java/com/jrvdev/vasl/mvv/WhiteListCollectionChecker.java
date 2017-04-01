package com.jrvdev.vasl.mvv;

import com.jrvdev.FileUtils.IFileCollection;
import com.jrvdev.FileUtils.IFileEntry;

import com.jrvdev.vasl.board.IWhiteListMatch;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WhiteListCollectionChecker implements IWhiteListCollectionChecker {

    private final IFileCollection _fileCollection;
    private final Set<IWhiteListMatch> _legalNames;

    public WhiteListCollectionChecker( IFileCollection fileCollection, Set<IWhiteListMatch> legalNames ) {
        _fileCollection = fileCollection;
        _legalNames = legalNames;
    }

    private void addBadNames( Set<String> badNames ) throws IOException {

        _fileCollection.getEntries().forEach(
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
}