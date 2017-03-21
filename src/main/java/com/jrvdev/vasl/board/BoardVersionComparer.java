package com.jrvdev.vasl.board;

import com.jrvdev.vasl.version.IVersionComparer;
import com.jrvdev.vasl.version.IMasterVersion;
import com.jrvdev.vasl.version.IVersionedResource;

public class BoardVersionComparer implements IVersionComparer<BoardVersion> {
    private final IMasterVersion _masterVersion;

    public BoardVersionComparer(IMasterVersion masterVersion) {
        super();
        _masterVersion = masterVersion;
    }

    @Override public boolean IsUpdatable( IVersionedResource<BoardVersion> versionedResource) {
        if ( !_masterVersion.containsKey( versionedResource.getName() ) ) {
            return false;
        }
        BoardVersion masterVersion = _masterVersion.get( versionedResource.getName() );
        return masterVersion.compareTo( versionedResource.getVersion() ) != 0;
    }

    @Override public String VersionComparison( IVersionedResource<BoardVersion> versionedResource ) {
        if ( !_masterVersion.containsKey( versionedResource.getName() ) ) {
            return "No master version";
        }
        return _masterVersion.get( versionedResource.getName() ).toString() + " <=> " + versionedResource.getVersion();
    }

}