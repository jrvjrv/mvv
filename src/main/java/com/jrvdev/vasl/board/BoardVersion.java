package com.jrvdev.vasl.board;

public class BoardVersion implements Comparable<BoardVersion> {

    private final String _version;

    public BoardVersion( String version ) {
        _version = version;
    }

    @Override public int compareTo( BoardVersion theOther ) {
        return _version.compareTo( theOther._version );
    }

    @Override public String toString() {
        return _version;
    }
}