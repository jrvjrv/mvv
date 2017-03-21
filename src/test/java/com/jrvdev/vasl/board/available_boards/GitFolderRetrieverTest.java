package com.jrvdev.vasl.board.available_boards;

import java.net.URL;
import java.net.MalformedURLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class GitFolderRetrieverTest {
    @Test
    public void retrieves_value() throws MalformedURLException {
        URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
        GitFolderRetriever r = new GitFolderRetriever(target);

        String json = r.getJSON();

        assertNotNull(json);
        assertNotSame("", json );
        //System.out.println( json );
        /*
        [
            {
                "name":"README.md",
                "path":"README.md",
                "sha":"9420671876cefe0f32350da2e8321d05909851f9",
                "size":773,
                "url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/README.md?ref=master",
                "html_url":"https://github.com/vasl-developers/vasl-boards-extensions/blob/master/README.md",
                "git_url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/blobs/9420671876cefe0f32350da2e8321d05909851f9",
                "download_url":"https://raw.githubusercontent.com/vasl-developers/vasl-boards-extensions/master/README.md",
                "type":"file",
                "_links":{
                    "self":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/README.md?ref=master",
                    "git":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/blobs/9420671876cefe0f32350da2e8321d05909851f9",
                    "html":"https://github.com/vasl-developers/vasl-boards-extensions/blob/master/README.md"
                }
            },
            {
                "name":"boards",
                "path":"boards",
                "sha":"8dd682c777e131e401336336de5a0997017a16ce",
                "size":0,
                "url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/boards?ref=master",
                "html_url":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/boards",
                "git_url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/8dd682c777e131e401336336de5a0997017a16ce",
                "download_url":null,
                "type":"dir",
                "_links":{
                    "self":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/boards?ref=master",
                    "git":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/8dd682c777e131e401336336de5a0997017a16ce",
                    "html":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/boards"
                }
            },
            {
                "name":"documents",
                "path":"documents",
                "sha":"2a5b4de799e00b94521a978a341a1e8b39d67d09",
                "size":0,
                "url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/documents?ref=master",
                "html_url":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/documents",
                "git_url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/2a5b4de799e00b94521a978a341a1e8b39d67d09",
                "download_url":null,
                "type":"dir",
                "_links":{
                    "self":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/documents?ref=master",
                    "git":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/2a5b4de799e00b94521a978a341a1e8b39d67d09",
                    "html":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/documents"
                }
            },
            {
                "name":"extensions-6.0",
                "path":"extensions-6.0",
                "sha":"75927a0170bf8a90384989fbbf496a7c3c13407f",
                "size":0,
                "url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/extensions-6.0?ref=master",
                "html_url":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/extensions-6.0",
                "git_url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/75927a0170bf8a90384989fbbf496a7c3c13407f",
                "download_url":null,
                "type":"dir",
                "_links":{
                    "self":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/extensions-6.0?ref=master",
                    "git":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/75927a0170bf8a90384989fbbf496a7c3c13407f",
                    "html":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/extensions-6.0"
                }
            },
            {
                "name":"extensions-complete",
                "path":"extensions-complete",
                "sha":"ada77f8d2deca0b0177ff8de345e51af50f511c9",
                "size":0,
                "url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/extensions-complete?ref=master",
                "html_url":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/extensions-complete",
                "git_url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/ada77f8d2deca0b0177ff8de345e51af50f511c9",
                "download_url":null,
                "type":"dir",
                "_links":{
                    "self":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/extensions-complete?ref=master",
                    "git":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/ada77f8d2deca0b0177ff8de345e51af50f511c9",
                    "html":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/extensions-complete"
                }
            },
            {
                "name":"extensions",
                "path":"extensions",
                "sha":"2d4cb53944984e5d04f8cbe95b636f7492c59231",
                "size":0,"url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/extensions?ref=master","html_url":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/extensions","git_url":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/2d4cb53944984e5d04f8cbe95b636f7492c59231","download_url":null,"type":"dir","_links":{"self":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/extensions?ref=master","git":"https://api.github.com/repos/vasl-developers/vasl-boards-extensions/git/trees/2d4cb53944984e5d04f8cbe95b636f7492c59231","html":"https://github.com/vasl-developers/vasl-boards-extensions/tree/master/extensions"}}]
*/
    }
}