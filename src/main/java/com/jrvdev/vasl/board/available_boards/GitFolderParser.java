package com.jrvdev.vasl.board.available_boards;

import com.google.gson.Gson;

class GitFolderParser {
    private IGitFolderRetriever _retriever;
    private boolean _jsonRetrieved = false;

    private GitFolderItem[] _folderItems;

    public GitFolderParser( IGitFolderRetriever retriever ) {
        if ( retriever == null ) throw new IllegalArgumentException("retriever may not be null");
        _retriever = retriever;
    }

    public String getSubfolderUrl( String name  ) {
        Gson gson = new Gson();

        if ( !_jsonRetrieved ) {
            _folderItems = gson.fromJson( _retriever.getJSON(), GitFolderItem[].class );
        }
        for( GitFolderItem folderItem : _folderItems ) {
            if ( folderItem.getName().equals( name ) && folderItem.isDir() ) {
                return folderItem.getGitUrl();
            }
        }
        // TODO: do something different than this?
        return null;
    }
}