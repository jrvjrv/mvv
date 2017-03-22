package com.jrvdev.vasl.board.available_boards;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

class GitFolderParser implements IGitFolderParser {
    private IGitFolderRetriever _retriever;
    private boolean _jsonRetrieved = false;

    private GitFolderItem[] _folderItems;

    public GitFolderParser( IGitFolderRetriever retriever ) {
        if ( retriever == null ) throw new IllegalArgumentException("retriever may not be null");
        _retriever = retriever;
    }

    private void RetrieveFolderItemsIfNecessary() {
        if ( !_jsonRetrieved ) {
            Gson gson = new Gson();
            _folderItems = gson.fromJson( _retriever.getJSON(), GitFolderItem[].class );
            _jsonRetrieved = true;
        }
    }

    @Override
    public String getSubfolderUrl( String name  ) {
        RetrieveFolderItemsIfNecessary();

        for( GitFolderItem folderItem : _folderItems ) {
            if ( folderItem.getName().equals( name ) && folderItem.isDir() ) {
                return folderItem.getUrl();
            }
        }
        // TODO: do something different than this?
        return null;
    }

    @Override
    public List<String> getSubfolders() {
        RetrieveFolderItemsIfNecessary();

        ArrayList<String> subDirs = new ArrayList<String>();

        for( GitFolderItem item: _folderItems ) {
            if ( item.isDir() ) {
                subDirs.add( item.getName());
            }
        }
        return subDirs;
    }

    @Override
    public List<String> getFiles() {
        RetrieveFolderItemsIfNecessary();

        ArrayList<String> files = new ArrayList<String>();

        for( GitFolderItem item: _folderItems ) {
            if ( item.isFile() ) {
                files.add( item.getName());
            }
        }
        return files;
    }
}