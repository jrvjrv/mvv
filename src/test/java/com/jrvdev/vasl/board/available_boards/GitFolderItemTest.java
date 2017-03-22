package com.jrvdev.vasl.board.available_boards;

import org.junit.Test;
import com.google.gson.Gson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GitFolderItemTest {
    @Test
    public void dir_loads_from_json() {
        String testJson = "{ 'name':'abc', 'type': 'dir', 'url': 'ghi'}";

        Gson gson = new Gson();

        GitFolderItem item = gson.fromJson( testJson, GitFolderItem.class );

        assertEquals( "abc", item.getName() );
        assertTrue( item.isDir() );
        assertEquals( "ghi", item.getUrl() );

    }

    @Test
    public void non_dir_loads_from_json() {
        String testJson = "{ 'name':'jkl', 'type': 'mno', 'url': 'pqr'}";

        Gson gson = new Gson();

        GitFolderItem item = gson.fromJson( testJson, GitFolderItem.class );

        assertEquals( "jkl", item.getName() );
        assertFalse( item.isDir() );
        assertEquals( "pqr", item.getUrl() );
        
    }
}
