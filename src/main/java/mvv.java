import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

// gets root. use url to go to boards directory. need to parse json
// https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/

public class mvv {

    private static String boardVersionURL = "https://raw.githubusercontent.com/vasl-developers/vasl/develop/boards/v5boardVersions.txt";
    public static void main( String[] args ) {

        System.out.println("mvv in");

        IMasterVersionLoader mvl = new MasterVersionLoader(
            new WebMasterVersionRetriever( boardVersionURL ) );

        IMasterVersion versions = new MasterVersion( mvl );

        versions.load();

        System.out.println(versions.toString());

        VersionedBoard versionedBoard = new VersionedBoard("22", new BoardVersion( "6.1" ) );
        VersionedBoard currentBoard = new VersionedBoard("22", new BoardVersion( "6.2" ) );

        IVersionComparer<BoardVersion> comparer = new BoardVersionComparer( versions );

        System.out.println( "Current is updatable: " + comparer.IsUpdatable( currentBoard ) );
        System.out.println( "Old is updatable: " + comparer.IsUpdatable( versionedBoard ) );
        System.out.println( versionedBoard.getName() + " master vs. archive " + comparer.VersionComparison( versionedBoard ) );
    
        System.out.println("mvv out after GitHubMasterVersionLoader");

        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));

        String baseBoardUrl = "https://raw.githubusercontent.com/vasl-developers/vasl-boards-extensions/master/boards";
        String boardName = "SC";
        String targetBoardFileName = System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + "bd" + boardName;
        String sourceBoardUrl = baseBoardUrl + "/bd" + boardName;
        RepositoryRetriever repositoryRetriever = new RepositoryRetriever( sourceBoardUrl, targetBoardFileName );

        //repositoryRetriever.getRepositoryFile();

        HashSet<IWhiteListMatch> legalFiles = new HashSet<IWhiteListMatch>();
        legalFiles.add( new ExactMatch( "data" ) );
        legalFiles.add( new ExtensionMatch( ".gif" ) );
        legalFiles.add( new ExtensionMatch( ".png" ) );
        legalFiles.add( new ExactMatch( "BoardMetadata.xml" ) );
        legalFiles.add( new ExactMatch( "LOSData" ) );
        legalFiles.add( new ExactMatch( "colorSSR" ) );
        legalFiles.add( new ExactMatch( "colors" ) );
        legalFiles.add( new ExactMatch( "overlaySSR" ) );
        legalFiles.add( new ExactMatch( "SSRControls" ) );


        BoardArchive boardArchive = new BoardArchive(System.getProperty("user.dir"), boardName, legalFiles );

        System.out.println( "From archive: " + boardArchive.getVersion().toString() );

        System.out.println( "Can update  " + boardName + "? " + comparer.IsUpdatable( boardArchive ) );

        System.out.println( boardArchive.getName() + ": master vs archive " + comparer.VersionComparison( boardArchive ));


        versions.forEach( ( masterBoardName, masterBoardVersion ) -> {
            System.out.println( "loop: " + masterBoardName );
            String masterTargetBoardFileName = System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + "bd" + masterBoardName;
            String masterSourceBoardUrl = baseBoardUrl + "/bd" + masterBoardName;
            RepositoryRetriever masterRepositoryRetriever = new RepositoryRetriever( masterSourceBoardUrl, masterTargetBoardFileName );
            if (  masterRepositoryRetriever.getRepositoryFile() ) {
            //if (  fileExists( masterTargetBoardFileName ) ) {
                BoardArchive masterBoardArchive = new BoardArchive(System.getProperty("user.dir"), masterBoardName, legalFiles );
                if ( comparer.IsUpdatable( masterBoardArchive )) {
                    System.out.println( masterBoardArchive.getName() + ": master vs archive " + comparer.VersionComparison( masterBoardArchive ));
                }
                masterBoardArchive.getBadNames().forEach( ( name ) -> {
                    System.out.println( "  bad name: " + name );
                });
            }
            else {
                System.out.println( "Could not retrieve " + masterBoardName );
            }
        } );

    }

    public static boolean fileExists( String pathString ) {
        Path path = Paths.get(pathString);
        return Files.exists(path);
    }
}