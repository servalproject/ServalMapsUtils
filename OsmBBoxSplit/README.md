# Serval Maps Utilities #

These Serval Maps Utilities are small applications that have been developed to make it easier for the [Serval Maps] team to work with data related to Serval Maps. 

More information is available on the [Serval Maps Wiki][Serval Maps]

## OSM PBF Bounding Box Split ##

The rationale behind the development of the OSM PBF Bounding Box Split applicant is to split the bounding box defined in an [OpenStreetMap] data file in the [PBF] format into 4 equal quadrants. Optionally the application can take a script that is designed to invoke the [Osmosis] application and modify it with the correct values for splitting the input file into four separate files using the four bounding boxes derived from the input file. 

The application can process a single file, or alternatively the application will recursively search through a directory looking for data files. If the directory search option is used it is possible to specify a minimum file size that the file must exceed before the bounding box split calculations is undertaken. 

[Serval Maps]: http://developer.servalproject.org/dokuwiki/doku.php?id=content:servalmaps:main_page
[OpenStreetMap]: http://www.openstreetmap.org/
[PBF]: http://wiki.openstreetmap.org/wiki/PBF_Format
[Osmosis]: http://wiki.openstreetmap.org/wiki/Osmosis
