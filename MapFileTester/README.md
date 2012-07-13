# Serval Maps Utilities #

These Serval Maps Utilities are small applications that have been developed to make it easier for the [Serval Maps] team to work with data related to Serval Maps. 

There are two applications at the moment, with others being added as the need arises. 

More information is available on the [Serval Maps Wiki][Serval Maps]

## Map File Tester ##

The intention of the Map File Tester application is to create a report based on the results of testing a directory of map data files encoded in the [mapsforge] format. The application will recursively search through a directory looking for map data files. 

Each file is opened, and if open successfully some metadata about that file will be output to the console. If the file cannot be opened an error will be output to the console along with some further information from the mapsforge library.

[Serval Maps]: http://developer.servalproject.org/dokuwiki/doku.php?id=content:servalmaps:main_page
[OpenStreetMap]: http://www.openstreetmap.org/
[mapsforge]: http://code.google.com/p/mapsforge/
