package com.jrvdev.vasl.board.available_boards;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.net.URL;
import java.net.MalformedURLException;

public class GitFolderParserTest {

    @Test(expected=IllegalArgumentException.class)
    public void null_constructor_parameter_throws() {
        GitFolderRetriever retriever = new GitFolderRetriever(null);
    }

    @Test
    public void get_boards_url() throws MalformedURLException {
        URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
        GitFolderRetriever retriever = new GitFolderRetriever(target);

        GitFolderParser parser = new GitFolderParser( retriever );

        String boardsUrl = parser.getSubfolderUrl( "boards" );
System.out.println( boardsUrl );
        assertNotNull(boardsUrl);
        assertNotSame("", boardsUrl );

    }

}