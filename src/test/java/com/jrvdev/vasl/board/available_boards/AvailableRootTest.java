package com.jrvdev.vasl.board.available_boards;

import java.net.MalformedURLException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Ignore;

// To run a single test. Can also use wild cards
// mvn test -Dtest=AvailableRootTest#null_parameter_throws



public class AvailableRootTest {
    @Test(expected=IllegalArgumentException.class)
    public void null_parameter_throws() throws MalformedURLException {
        AvailableRoot r = new AvailableRoot(null);
    }

    @Test(expected=MalformedURLException.class)
    public void bad_url_throws() throws MalformedURLException {
        AvailableRoot r = new AvailableRoot("");
    }
    @Test(expected=MalformedURLException.class)
    public void bad_url2_throws() throws MalformedURLException {
        AvailableRoot r = new AvailableRoot("foo");
    }
    @Test
    public void good_url_is_good() throws MalformedURLException {
        AvailableRoot r = new AvailableRoot("http://www.jrvdev.com");
    }

    // https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/
    @Test
    @Ignore // accesses the internet
    public void boards_url_not_null_or_empty() throws MalformedURLException {
        AvailableRoot r = new AvailableRoot("https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");

        assertNotNull(r.getBoardsUrl());
        assertNotSame("", r.getBoardsUrl() );
        
        
    }


}