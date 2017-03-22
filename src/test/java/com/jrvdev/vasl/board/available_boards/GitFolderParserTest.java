package com.jrvdev.vasl.board.available_boards;

// promising but failed: http://www.aliok.com.tr/techposts/2013-06-02-ignore-junit-tests-conditionally.html

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;

// mvn test -Dtest=GitFolderParserTest#get_boards_url

public class GitFolderParserTest {

    @Test(expected=IllegalArgumentException.class)
    public void null_constructor_parameter_throws() {
        GitFolderRetriever retriever = new GitFolderRetriever(null);
    }

    @Test
    @Ignore // accesses the internet
    public void get_boards_url() throws MalformedURLException {
        URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
        GitFolderRetriever retriever = new GitFolderRetriever(target);

        GitFolderParser parser = new GitFolderParser( retriever );

        String boardsUrlString = parser.getSubfolderUrl( "boards" );
//System.out.println( boardsUrlString );
        assertNotNull(boardsUrlString);
        assertNotSame("", boardsUrlString );

        URL boardsUrl = new URL( boardsUrlString );

        GitFolderRetriever boardsRetriever = new GitFolderRetriever( boardsUrl );
        GitFolderParser boardsParser = new GitFolderParser( boardsRetriever );

        System.out.println( boardsParser.getFiles() );
    }

    private class MockRetriever implements IGitFolderRetriever {
        private String _json;
        public MockRetriever( String json ) {
            _json = json;
        }
        public String getJSON() {
            return _json;
        }

    }

    @Test
    public void get_dirs_one_only() {
        GitFolderParser parser = new GitFolderParser( new MockRetriever("[ { 'name': 'abc', 'type' : 'dir', 'url' : 'def' }, { 'name': 'ghi', 'type' : 'jkl', 'url' : 'mno' } ]") );

        List<String> subfolders = parser.getSubfolders();
        assertEquals( subfolders.size(), 1 );
        assertEquals( subfolders.get(0), "abc" );
    }

    @Test
    public void get_dirs_three() {
        GitFolderParser parser = new GitFolderParser( new MockRetriever("[ { 'name': 'abc', 'type' : 'dir', 'url' : 'def' }, { 'name': 'ghi', 'type' : 'jkl', 'url' : 'mno' }, { 'name': 'pqr', 'type' : 'dir', 'url' : 'mno' }, { 'name': 'stu', 'type' : 'dir', 'url' : 'mno' } ]") );

        List<String> subfolders = parser.getSubfolders();
        assertEquals( subfolders.size(), 3 );
        assertTrue( subfolders.get(0).equals( "abc" ) || subfolders.get(1).equals( "abc" ) || subfolders.get(2).equals( "abc" ) );
        assertTrue( subfolders.get(0).equals( "pqr" ) || subfolders.get(1).equals( "pqr" ) || subfolders.get(2).equals( "pqr" ) );
        assertTrue( subfolders.get(0).equals( "stu" ) || subfolders.get(1).equals( "stu" ) || subfolders.get(2).equals( "stu" ) );
    }

   @Test
    public void get_files_one_only() {
        GitFolderParser parser = new GitFolderParser( new MockRetriever("[ { 'name': 'abc', 'type' : 'dir', 'url' : 'def' }, { 'name': 'f_abc', 'type' : 'file', 'url' : 'f_def' }, { 'name': 'ghi', 'type' : 'jkl', 'url' : 'mno' } ]") );

        List<String> files = parser.getFiles();
        assertEquals( 1, files.size() );
        assertEquals( "f_abc", files.get(0) );
    }

    @Test
    public void get_files_three() {
        GitFolderParser parser = new GitFolderParser( new MockRetriever("[ { 'name': 'abc', 'type' : 'dir', 'url' : 'def' }, { 'name': 'ghi', 'type' : 'jkl', 'url' : 'mno' }, { 'name': 'pqr', 'type' : 'dir', 'url' : 'mno' }, { 'name': 'stu', 'type' : 'dir', 'url' : 'mno' }, { 'name': 'f_abc', 'type' : 'file', 'url' : 'def' }, { 'name': 'f_pqr', 'type' : 'file', 'url' : 'mno' }, { 'name': 'f_stu', 'type' : 'file', 'url' : 'mno' } ]") );

        List<String> files = parser.getFiles();
        assertEquals( 3, files.size() );
        assertTrue( files.get(0).equals( "f_abc" ) || files.get(1).equals( "f_abc" ) || files.get(2).equals( "f_abc" ) );
        assertTrue( files.get(0).equals( "f_pqr" ) || files.get(1).equals( "f_pqr" ) || files.get(2).equals( "f_pqr" ) );
        assertTrue( files.get(0).equals( "f_stu" ) || files.get(1).equals( "f_stu" ) || files.get(2).equals( "f_stu" ) );
    }

}