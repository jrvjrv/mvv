package com.jrvdev.vasl.board.available_boards;

class GitFolderItem {
    private String name;
    private String type;
    private String git_url;

    public String getName() {
        return name;
    }

    public boolean isDir() {
        return type.equals( "dir" );
    }

    public String getGitUrl() {
        return git_url;
    }
}