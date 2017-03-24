package com.jrvdev.netUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

public class netUtils {

    // do not use on an already-encoded URL. It will double-encode it.
    public static String encodeUrl( String unencodedURL ) {
        try {
            URL url = new URL(unencodedURL);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef()); 
            return uri.toURL().toString();
        }
        catch( java.net.URISyntaxException ex ) {
            //return "bad url: " + ex.getMessage();
            return unencodedURL;
        }
        catch( java.net.MalformedURLException ex ) {
            //return "bad url2: " + ex.getMessage();
            return unencodedURL;
        }
    }
}