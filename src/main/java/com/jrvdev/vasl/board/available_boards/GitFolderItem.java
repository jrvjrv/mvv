package com.jrvdev.vasl.board.available_boards;

class GitFolderItem {
    private String name;
    private String type;
    private String url;

    public String getName() {
        return name;
    }

    public boolean isDir() {
        return type.equals( "dir" );
    }

    public boolean isFile() {
        return type.equals( "file" );
    }

    public String getUrl() {
        return url;
    }
}