# Serval Maps Utilities #

These Serval Maps Utilities are small applications that have been developed to make it easier for the [Serval Maps] team to work with data related to Serval Maps. 

There are three applications at the moment, with others being added as the need arises. 

More information is available on the [Serval Maps Wiki][Serval Maps]

## Map File Index ##

The intention of the Map File Index application is to create an index file of a series of map data files. The index file contains metadata about each map file in the dataset as well as information about the index itself. 

The index file will be used with the Serval Maps Download service (currently being developed) which will act as a repository of [OpenStreetMap] data encoded in the [mapsforge] format.

## Map File Tester ##

The intention of the Map File Tester application is to create a report based on the results of testing a directory of map data files encoded in the [mapsforge] format. The application will recursively search through a directory looking for map data files. 

Each file is opened, and if open successfully some metadata about that file will be output to the console. If the file cannot be opened an error will be output to the console along with some further information from the mapsforge library.

## OSM PBF Metadata ##

The intention of the OSM PBF Metadata application is to output metadata related to an [OpenStreetMap] data file in the [PBF] format. Currently the bounding box and the file size are reported. 

The application can process a single file, or alternatively the application will recursively search through a directory looking for data files. 

## OSM PBF Bounding Box Split ##

The rationale behind the development of the OSM PBF Bounding Box Split applicant is to split the bounding box defined in an [OpenStreetMap] data file in the [PBF] format into 4 equal quadrants. Optionally the application can take a script that is designed to invoke the [Osmosis] application and modify it with the correct values for splitting the input file into four separate files using the four bounding boxes derived from the input file. 

The application can process a single file, or alternatively the application will recursively search through a directory looking for data files. If the directory search option is used it is possible to specify a minimum file size that the file must exceed before the bounding box split calculations is undertaken. 

[Serval Maps]: http://developer.servalproject.org/dokuwiki/doku.php?id=content:servalmaps:main_page
[OpenStreetMap]: http://www.openstreetmap.org/
[mapsforge]: http://code.google.com/p/mapsforge/
[PBF]: http://wiki.openstreetmap.org/wiki/PBF_Format
[Osmosis]: http://wiki.openstreetmap.org/wiki/Osmosis
