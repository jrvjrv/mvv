package com.jrvdev.vasl.board;
import com.jrvdev.vasl.version.IVersionedResource;

public class VersionedBoard implements IVersionedResource< BoardVersion > {
    private final String _name;
    private final BoardVersion _version;

    public VersionedBoard( String name, BoardVersion version ) {
        _name = name;
        _version = version;
    }

    @Override public String getName() {
        return _name;
    }

    @Override public BoardVersion getVersion() {
        return _version;
    }

}