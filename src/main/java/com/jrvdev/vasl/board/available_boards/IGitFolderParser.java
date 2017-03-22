package com.jrvdev.vasl.board.available_boards;

import java.util.List;

interface IGitFolderParser {
    public String getSubfolderUrl( String name  );
    public List<String> getSubfolders();
    public List<String> getFiles();
}
