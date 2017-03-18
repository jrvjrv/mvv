
public class BoardVersionComparer implements IVersionComparer<BoardVersion> {

    private final IMasterVersion _masterVersion;

    public BoardVersionComparer(IMasterVersion masterVersion) {
        super();
        _masterVersion = masterVersion;
    }

    public boolean IsUpdatable( IVersionedResource<BoardVersion> versionedResource) {
        if ( !_masterVersion.containsKey( versionedResource.getName() ) ) {
            return false;
        }
        BoardVersion masterVersion = _masterVersion.get( versionedResource.getName() );
        return masterVersion.compareTo( versionedResource.getVersion() ) != 0;
    }

    public String VersionComparison( IVersionedResource<BoardVersion> versionedResource ) {
        if ( !_masterVersion.containsKey( versionedResource.getName() ) ) {
            return "No master version";
        }
        return _masterVersion.get( versionedResource.getName() ).toString() + " <=> " + versionedResource.getVersion();
    }
}