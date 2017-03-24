package com.jrvdev.netUtils;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class netUtilsTest {
    @Test
    public void encodeSpacesInPath() {
        String url = "http://www.stuff.com/this has spaces/more spaces.txt";

        String encodedUrl = netUtils.encodeUrl( url );

        assertEquals( url.replace( " ", "%20" ), encodedUrl );
    }

    @Test
    @Ignore
    public void alreadyEncodedSpacesInPath() {
        // do not use on an already-encoded URL. It will double-encode it.
        String url = "http://www.stuff.com/this%20has%20spaces/more%20spaces.txt";

        String encodedUrl = netUtils.encodeUrl( url );

        assertEquals( url.replace( " ", "%20" ), encodedUrl );
    }

    @Test
    public void nutsExample() {
        String url = "http://example.com/:@-._~!$&'()*+,=;:@-._~!$&'()*+,=:@-._~!$&'()*+,==?/?:@-._~!$'()*+,;=/?:@-._~!$'()*+,;==#/?:@-._~!$&'()*+,;=";

        String encodedUrl = netUtils.encodeUrl( url );

        assertEquals( url, encodedUrl );
        
    }
}