import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;

import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class WebMasterVersionRetriever implements IMasterVersionRetriever {

    private final String _sourceUri;

    public WebMasterVersionRetriever( String sourceUri ) {
        _sourceUri = sourceUri;
    }

    public InputStream GetVersions() {
        // Need to disable SNI to read from Github
        System.setProperty("jsse.enableSNIExtension", "false");

       try {
            URL base = new URL(_sourceUri);
            URLConnection conn = base.openConnection();
            conn.setUseCaches(false);

            InputStream input = null;
            input = conn.getInputStream();
            return input;

        }
        catch (IOException e) {
//            System.out.println("mvv IOException");
//            
            // Fail silently if we can't contact the server
            return new ByteArrayInputStream("".getBytes());
        }
        finally {

        }
//
    }
}