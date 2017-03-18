import java.util.Map;

public interface IMasterVersion extends  Map< String, BoardVersion > {
    void load();
}