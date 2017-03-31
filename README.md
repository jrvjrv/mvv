#mvv
##map version verifier

###description

This utility program verifies that vasl maps and overlays available for download match the master file on github. The vasl code checks map file versions used in a new vasl game being set up or a saved vasl game being loaded against a master list on github. If the file version is < the master version, the map is downloaded from github. If the master list falls out of sync with the version of the map in the board/overlay file, vasl attempts to download the map over and over again.

The idea for the program originated when several players reported the problem with maps being downloaded repeatedly. In addition to this problem, it was found that there were other issues in map/overlay archives. 
* Some of the map/overlay archives contained unnecessary files such as the original graphics files (.psd files) and in some cases older versions of the archive files. These additional files sometimes doubled the size of the download. 
* Some items listed in the master file did not exist in the boards directory
* Some archives in the boards directory were not listed in the master file
* Some archives had versions that were formatted in a very old style that is no longer valid
* Some archives were missing essential files, primarily the "data" file
* Some archives could not be opened, i.e. they caused exceptions in the mvv.

This utility checks the master file against the actual board archive files and the actual board archive files against the master file, looking for items present in one but not the other. It checks the contents of the board archives against a list of files that are allowed to be present and reports extras. Other problems (e.g. missing data file, archives that can't be opened) show up as exceptions reported during the run. 

In order to check the actual board archive files, these are downloaded during a run. They are not downloaded if the files are already present, so if the upstream files have changed the user must delete the downloaded files. mvv will not notice that it does not have the latest archive file and will only download if one is not present.

As part of its run mvv will print contents suitable for v5boardVersions.txt and v5overlayVersions.txt. The output needs to be looked over, as it currently outputs lines for map archives that do not have versions with a blank version. These can be removed.

###running mvv
To date mvv has only been run using maven. I use the command "mvn package" to compile and package the program, then "mvn exec:java" to run it. mvv writes all its output to stdout. To capture the output to a file, use standard output redirection, e.g. "mvn exec:java >mvv_run.txt". 

###20170331 status
At this time mvv works but is not a polished program. There are various hard-coded URLs and names, but I may let these slide as they are not very likely to change. It puts all its output to stdout, and again I may leave that alone. Some parts need to be refactored and need unit tests.