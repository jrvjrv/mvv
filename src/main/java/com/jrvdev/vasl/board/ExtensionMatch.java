package com.jrvdev.vasl.board;

import org.apache.commons.lang3.StringUtils;



public class ExtensionMatch implements IWhiteListMatch {

    private final String _extension;
    private final int _extensionLength;

    public ExtensionMatch( String extension ) {
        _extension = extension;
        _extensionLength = _extension.length();
    }

    @Override public boolean isMatch( String filename ) {
        return StringUtils.right( filename, _extensionLength ).equals( _extension );
    }
}