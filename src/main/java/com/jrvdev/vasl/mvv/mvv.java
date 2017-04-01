package com.jrvdev.vasl.mvv;

import com.jrvdev.FileUtils.ZipFileCollection;

import com.jrvdev.vasl.board.BoardArchive;
import com.jrvdev.vasl.board.BoardVersion;
import com.jrvdev.vasl.board.BoardVersionComparer;
import com.jrvdev.vasl.board.ExactMatch;
import com.jrvdev.vasl.board.ExtensionMatch;
import com.jrvdev.vasl.board.LegacyVersionFileParser;
import com.jrvdev.vasl.board.RepositoryRetriever;
import com.jrvdev.vasl.board.VersionedBoard;
import com.jrvdev.vasl.board.available_boards.GitFolderRetriever;
import com.jrvdev.vasl.board.available_boards.GitFolderParser;

import com.jrvdev.vasl.board.IWhiteListMatch;
import com.jrvdev.vasl.version.IMasterVersion;
import com.jrvdev.vasl.version.IVersionComparer;
import com.jrvdev.vasl.version.MasterVersion;
import com.jrvdev.vasl.version.MasterVersionLoader;
import com.jrvdev.vasl.version.WebMasterVersionRetriever;
import com.jrvdev.vasl.version.FileMasterVersionRetriever;
import com.jrvdev.vasl.version.IMasterVersionRetriever;
import com.jrvdev.vasl.version.IMasterVersionLoader;


import java.io.IOException;
import java.lang.NullPointerException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// http://www.oracle.com/technetwork/articles/java/json-1973242.html
// gets root. use url to go to boards directory. need to parse json
// https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/

public class mvv {

    private static String boardVersionURL = "https://raw.githubusercontent.com/vasl-developers/vasl/develop/boards/v5boardVersions.txt";
    private static String overlayVersionURL = "https://raw.githubusercontent.com/vasl-developers/vasl/develop/boards/v5overlayVersions.txt";
    
    public static void main( String[] args ) {

        System.out.println("mvv in");


        IMasterVersion masterVersions;
        
        if ( false ) {
            // use a file on disk
            String localMasterVersionFilename = System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + "master.txt";
            IMasterVersionRetriever localMasterVersionRetriever = new FileMasterVersionRetriever( localMasterVersionFilename );
            IMasterVersionLoader localMasterVersionLoader = new MasterVersionLoader( localMasterVersionRetriever );

            masterVersions = new MasterVersion( localMasterVersionLoader );
        }
        else 
        {
            // retrieve from internet
            IMasterVersionLoader mvl = new MasterVersionLoader(
                new WebMasterVersionRetriever( boardVersionURL ) );
            masterVersions = new MasterVersion( mvl );
        }

        masterVersions.load();

        System.out.println(masterVersions.toString());


        IMasterVersionLoader overlayVersionsLoader = new MasterVersionLoader(
            new WebMasterVersionRetriever( overlayVersionURL ) );

        IMasterVersion overlayVersions = new MasterVersion( overlayVersionsLoader );

        overlayVersions.load();

        System.out.println(overlayVersions.toString());



        VersionedBoard versionedBoard = new VersionedBoard("22", new BoardVersion( "6.1" ) );
        VersionedBoard currentBoard = new VersionedBoard("22", new BoardVersion( "6.2" ) );

        IVersionComparer<BoardVersion> comparer = new BoardVersionComparer( masterVersions );
        IVersionComparer<BoardVersion> overlayComparer = new BoardVersionComparer( overlayVersions );

        System.out.println( "Current is updatable: " + comparer.IsUpdatable( currentBoard ) );
        System.out.println( "Old is updatable: " + comparer.IsUpdatable( versionedBoard ) );
        System.out.println( versionedBoard.getName() + " master vs. archive " + comparer.VersionComparison( versionedBoard ) );
    
        System.out.println("mvv out after GitHubMasterVersionLoader");

        System.out.println("Working Directory = " +
              System.getProperty("user.dir"));

        String baseBoardUrl = "https://raw.githubusercontent.com/vasl-developers/vasl-boards-extensions/master/boards";
        String baseOverlayUrl = "https://raw.githubusercontent.com/vasl-developers/vasl-boards-extensions/master/boards/overlays";
        String boardName = "BOB DZN";
        String targetBoardFileName = System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + "bd" + boardName;
        String sourceBoardUrl = baseBoardUrl + "/bd" + boardName;
        RepositoryRetriever repositoryRetriever = new RepositoryRetriever( sourceBoardUrl, targetBoardFileName );

        if ( ! fileExists( targetBoardFileName ) ) {
            System.out.println( "Retrieving " + sourceBoardUrl );
            if ( !repositoryRetriever.getRepositoryFile() ) {
                System.out.println( "Retrieve failed!");
            }
        }

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

        ZipFileCollection zipFileCollection = new ZipFileCollection(  buildArchivePath( "bd" + boardName ) );

        BoardArchive boardArchive = new BoardArchive(zipFileCollection, boardName, new LegacyVersionFileParser( zipFileCollection ) );

        System.out.println( "From archive: " + boardArchive.getVersion().toString() );

        System.out.println( "Can update  " + boardName + "? " + comparer.IsUpdatable( boardArchive ) );

        System.out.println( boardArchive.getName() + ": master vs archive " + comparer.VersionComparison( boardArchive ));

        CheckAllBoardsAgainstMaster( masterVersions, comparer, baseBoardUrl, legalFiles );
        CheckAllOverlaysAgainstMaster( overlayVersions, overlayComparer, baseOverlayUrl, legalFiles );

        CheckMasterForAllBoards( masterVersions );
        CheckMasterForAllOverlays( overlayVersions );

        GenerateV5boardVersionsTxt( baseBoardUrl, legalFiles );
        GenerateV5overlaysVersionsTxt( baseOverlayUrl, legalFiles );

    }

    private static String buildArchivePath( String fileName ) {
        return System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + fileName;
    }

    private static void GenerateV5overlaysVersionsTxt( String baseOverlayUrl, Set<IWhiteListMatch> legalFiles ) {
        try {
            URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
            GitFolderRetriever retriever = new GitFolderRetriever(target);

            GitFolderParser parser = new GitFolderParser( retriever );

            String boardsUrlString = parser.getSubfolderUrl( "boards" );


            URL boardsUrl = new URL( boardsUrlString );

            GitFolderRetriever boardsRetriever = new GitFolderRetriever( boardsUrl );
            GitFolderParser boardsParser = new GitFolderParser( boardsRetriever );

            URL overlaysUrl = new URL( boardsParser.getSubfolderUrl( "overlays") );

            GitFolderRetriever overlaysRetriever = new GitFolderRetriever( overlaysUrl );
            GitFolderParser overlaysParser = new GitFolderParser( overlaysRetriever );

            ArrayList<String> masterVersionsTxt = new ArrayList<String>();

            overlaysParser.getFiles().forEach( ( String file ) -> {
                if( file.startsWith( "ovr") ) {
                    String overlayName = file.substring( 3 );
                    String masterTargetOverlayFileName = System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + "ovr" + overlayName;
                    String masterSourceOverlayUrl = baseOverlayUrl + "/ovr" + overlayName;
                    RepositoryRetriever masterRepositoryRetriever = new RepositoryRetriever( masterSourceOverlayUrl, masterTargetOverlayFileName );
                    if (  fileExists( masterTargetOverlayFileName ) || masterRepositoryRetriever.getRepositoryFile() ) {
                    //if (  fileExists( masterTargetBoardFileName ) ) {
                        try {
                            ZipFileCollection zipFileCollection = new ZipFileCollection(  buildArchivePath( "ovr" + overlayName ) );
                            BoardArchive actualOverlayArchive = new BoardArchive(zipFileCollection, overlayName, new LegacyVersionFileParser( zipFileCollection ) );
                            System.out.println( "Checking for bad names: " + overlayName );
                            try {
                                WhiteListCollectionChecker whiteList = new WhiteListCollectionChecker( actualOverlayArchive, legalFiles );
                                whiteList.getBadNames().forEach( ( name ) -> {
                                    System.out.println( "  bad name: " + name );
                                });
                            }
                            catch ( IOException ex ) {
                                System.out.println( "Caught IOException " + ex.getMessage() );
                            }
                            masterVersionsTxt.add( "ovr" + overlayName.replace( " ", "\\u0020") + " = " + actualOverlayArchive.getVersion().toString().replace( " ", "\\u0020" ) );
                        }
                        catch ( NullPointerException ex ) {
                            System.out.println( "Error in overlay " + file + " " + ex.getMessage() );
                        }
                    }
                    else {
                        System.out.println( "Could not retrieve " + overlayName );
                    }
                }
            } );

            System.out.println( "# master generated by mvv " + getFormattedDate() );
            System.out.println( "# include 'ovr' prefix in overlay names" );
            System.out.println( "# use \\u0020 for spaces in overlay names" );
            System.out.println( "" );
            masterVersionsTxt.forEach( ( line ) -> { System.out.println( line );});
        }
        catch ( MalformedURLException ex ) {
            System.out.println( ex.getMessage() );
        }
    }

    private static void GenerateV5boardVersionsTxt( String baseBoardUrl, Set<IWhiteListMatch> legalFiles ) {
        try {
            URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
            GitFolderRetriever retriever = new GitFolderRetriever(target);

            GitFolderParser parser = new GitFolderParser( retriever );

            String boardsUrlString = parser.getSubfolderUrl( "boards" );

            URL boardsUrl = new URL( boardsUrlString );

            GitFolderRetriever boardsRetriever = new GitFolderRetriever( boardsUrl );
            GitFolderParser boardsParser = new GitFolderParser( boardsRetriever );

            ArrayList<String> masterVersionsTxt = new ArrayList<String>();

            boardsParser.getFiles().forEach( ( String file ) -> {
                if( file.startsWith( "bd") ) {
                    String boardName = file.substring( 2 );
                    String masterTargetBoardFileName = System.getProperty("user.dir") + System.getProperty("file.separator", "\\") + "bd" + boardName;
                    String masterSourceBoardUrl = baseBoardUrl + "/bd" + boardName;
                    RepositoryRetriever masterRepositoryRetriever = new RepositoryRetriever( masterSourceBoardUrl, masterTargetBoardFileName );
                    if (  fileExists( masterTargetBoardFileName ) || masterRepositoryRetriever.getRepositoryFile() ) {
                    //if (  fileExists( masterTargetBoardFileName ) ) {
                        try {
                            ZipFileCollection zipFileCollection = new ZipFileCollection(  buildArchivePath( "bd" + boardName ) );
                            BoardArchive actualBoardArchive = new BoardArchive( zipFileCollection, boardName, new LegacyVersionFileParser( zipFileCollection ) );
                            System.out.println( "Checking for bad names: " + boardName );
                            try {
                                WhiteListCollectionChecker whiteList = new WhiteListCollectionChecker( actualBoardArchive, legalFiles );
                                whiteList.getBadNames().forEach( ( name ) -> {
                                    System.out.println( "  bad name: " + name );
                                });
                            }
                            catch ( IOException ex ) {
                                System.out.println( "Caught IOException " + ex.getMessage() );
                            }
                        
                            masterVersionsTxt.add( boardName.replace( " ", "\\u0020") + " = " + actualBoardArchive.getVersion().toString().replace( " ", "\\u0020" ) );
                        }
                        catch ( NullPointerException ex ) {
                            System.out.println( "Error in board " + file + " " + ex.getMessage() );
                        }
                    }
                    else {
                        System.out.println( "Could not retrieve " + boardName );
                    }

                }
            } );

            System.out.println( "# master generated by mvv " + getFormattedDate() );
            System.out.println( "# do NOT include 'bd' prefix in board names" );
            System.out.println( "# use \\u0020 for spaces in board names" );
            System.out.println( "" );
            masterVersionsTxt.forEach( ( line ) -> { System.out.println( line );});
        }
        catch ( MalformedURLException ex ) {
            System.out.println( ex.getMessage() );
        }
    }

    private static String getFormattedDate() {
        return new SimpleDateFormat( "yyyyMMdd" ).format( new Date() );
    }

    private static void CheckMasterForAllBoards(IMasterVersion masterVersions) {
        try {
            URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
            GitFolderRetriever retriever = new GitFolderRetriever(target);

            GitFolderParser parser = new GitFolderParser( retriever );

            String boardsUrlString = parser.getSubfolderUrl( "boards" );

            URL boardsUrl = new URL( boardsUrlString );

            GitFolderRetriever boardsRetriever = new GitFolderRetriever( boardsUrl );
            GitFolderParser boardsParser = new GitFolderParser( boardsRetriever );

            System.out.println( "boards not found in master" );
            boardsParser.getFiles().forEach( ( String file ) -> {
                if( file.startsWith( "bd") ) {
                    String boardName = file.substring( 2 );
                    //System.out.println( "Looking at " + boardName );
                    if ( !masterVersions.keySet().contains( boardName ) ) {
                        System.out.println( boardName );
                    }
                }
            } );
        }
        catch ( MalformedURLException ex ) {
            System.out.println( ex.getMessage() );
        }

    }

    private static void CheckMasterForAllOverlays(IMasterVersion masterVersions) {
        try {
            URL target = new URL( "https://api.github.com/repos/vasl-developers/vasl-boards-extensions/contents/");
            GitFolderRetriever retriever = new GitFolderRetriever(target);

            GitFolderParser parser = new GitFolderParser( retriever );

            String boardsUrlString = parser.getSubfolderUrl( "boards" );

            URL boardsUrl = new URL( boardsUrlString );

            GitFolderRetriever boardsRetriever = new GitFolderRetriever( boardsUrl );
            GitFolderParser boardsParser = new GitFolderParser( boardsRetriever );

            String overlaysUrlString = boardsParser.getSubfolderUrl( "overlays" );

            URL overlaysUrl = new URL( overlaysUrlString );

            GitFolderRetriever overlaysRetriever = new GitFolderRetriever( overlaysUrl );
            GitFolderParser overlaysParser = new GitFolderParser( overlaysRetriever );

            System.out.println( "overlays not found in master" );
            overlaysParser.getFiles().forEach( ( String file ) -> {
                if( file.startsWith( "ovr") ) {
                    // overlays have "ovr" in the master file; boards do not have "bd". sigh.
                    String overlayName = file;
                    //System.out.println( "Looking at " + boardName );
                    if ( !masterVersions.keySet().contains( overlayName ) ) {
                        System.out.println( overlayName );
                    }
                }
            } );
        }
        catch ( MalformedURLException ex ) {
            System.out.println( "CheckMasterForAllOverlays() " + ex.getMessage() );
        }

    }

    private static void CheckAllBoardsAgainstMaster( IMasterVersion masterVersions, IVersionComparer<BoardVersion> comparer, String baseBoardUrl, Set<IWhiteListMatch> legalFiles ) {
        masterVersions.forEach( ( String masterBoardName, BoardVersion masterBoardVersion ) -> {
            System.out.println( "loop: " + masterBoardName );
            String masterTargetBoardFileName = buildArchivePath( "bd" + masterBoardName );
            String masterSourceBoardUrl = baseBoardUrl + "/bd" + masterBoardName;
            RepositoryRetriever masterRepositoryRetriever = new RepositoryRetriever( masterSourceBoardUrl, masterTargetBoardFileName );
            if (  fileExists( masterTargetBoardFileName ) || masterRepositoryRetriever.getRepositoryFile() ) {
            //if (  fileExists( masterTargetBoardFileName ) ) {
                ZipFileCollection zipFileCollection = new ZipFileCollection(  masterTargetBoardFileName );
                BoardArchive masterBoardArchive = new BoardArchive(zipFileCollection, masterBoardName, new LegacyVersionFileParser( zipFileCollection ) );
                if ( comparer.IsUpdatable( masterBoardArchive )) {
                    System.out.println( masterBoardArchive.getName() + ": master vs archive " + comparer.VersionComparison( masterBoardArchive ));
                }
            }
            else {
                System.out.println( "Could not retrieve " + masterBoardName );
            }
        } );
    }

    private static void CheckAllOverlaysAgainstMaster( IMasterVersion masterVersions, IVersionComparer<BoardVersion> comparer, String baseOverlayUrl, Set<IWhiteListMatch> legalFiles ) {
        masterVersions.forEach( ( String masterOverlayName, BoardVersion masterOverlayVersion ) -> {
            System.out.println( "loop: " + masterOverlayName );

            // overlays have "ovr" in the master file; boards do not have "bd". sigh.
            String masterTargetOverlayFileName = buildArchivePath( masterOverlayName );
            // overlays have "ovr" in the master file; boards do not have "bd". sigh.
            String masterSourceOverlayUrl = baseOverlayUrl + "/" + masterOverlayName;
            RepositoryRetriever masterRepositoryRetriever = new RepositoryRetriever( masterSourceOverlayUrl, masterTargetOverlayFileName );
            if (  fileExists( masterTargetOverlayFileName ) || masterRepositoryRetriever.getRepositoryFile() ) {
                //if (  fileExists( masterTargetBoardFileName ) ) {
                    ZipFileCollection zipFileCollection = new ZipFileCollection(  masterTargetOverlayFileName );
                    BoardArchive masterOverlayArchive = new BoardArchive( zipFileCollection, masterOverlayName, new LegacyVersionFileParser( zipFileCollection ) );
                    try {
                    if ( comparer.IsUpdatable( masterOverlayArchive )) {
                        System.out.println( masterOverlayArchive.getName() + ": master vs archive " + comparer.VersionComparison( masterOverlayArchive ));
                    }
                }
                catch ( java.lang.NullPointerException ex ) {
                    System.out.println( "Error in overlay " + masterOverlayName + " " + ex.getMessage() );
                }
            }
            else {
                System.out.println( "Could not retrieve " + masterOverlayName );
            }
        } );

    }

    public static boolean fileExists( String pathString ) {
        Path path = Paths.get(pathString);
        return Files.exists(path);
    }
}