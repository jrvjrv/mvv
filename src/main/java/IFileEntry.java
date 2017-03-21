import java.io.InputStream;

public interface IFileEntry {
    String getName();
    InputStream getFileContents();
}