package com.jrvdev.vasl.board.available_boards;

import java.net.URL;
import java.net.MalformedURLException;

class AvailableRoot {

    private final URL _url;
    private URL _boardsUrl;
    private boolean _boardsUrlLoaded = false;

    public AvailableRoot( String url ) throws MalformedURLException {
        if ( url == null ) throw new IllegalArgumentException("url may not be null");
        _url = new URL( url );
    }

    private void loadBoardsUrl() throws MalformedURLException {
        _boardsUrl = new URL( "" );
        _boardsUrlLoaded = true;
    }

    public URL getBoardsUrl() throws MalformedURLException {
        if ( !_boardsUrlLoaded ) {
            loadBoardsUrl();
        }
        return _boardsUrl;
    }



}