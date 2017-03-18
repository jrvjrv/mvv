import java.io.InputStream;
import java.util.Set;

public interface IArchiveWrapper {
    Set<IArchiveEntry> getEntries();
    InputStream getFileContents( String filename );
}