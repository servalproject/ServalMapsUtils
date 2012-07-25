/*
 * Copyright (C) 2012 The Serval Project
 *
 * This file is part of the Serval Maps OSM Bounding Box Split Software
 *
 * Serval Maps OSM PBF Metadata Reader Software is free software; you can 
 * redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either 
 * version 3 of the License, or (at your option) any later version.
 *
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.servalproject.maps.osmbboxsplit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.servalproject.maps.osmbboxsplit.utils.FileUtils;

import crosby.binary.file.BlockInputStream;

/**
 * class used to calculate the bounding box split and optionally write an
 * osmosis script based on the supplied template
 */
public class BBoxSplit {
	
	/**
	 * read an OSM PBF file and optionally write an osmosis script using the supplied template
	 * 
	 * @param inputFile path to the input file
	 * @param outputDir path to the output directory
	 * @param template the contents of the template file
	 * @param minFileSize the minimum file size, in MB, of files to process
	 * @throws IOException if the specified file cannot be read
	 */
	public static void readFile(File inputFile, File outputDir, String template, int minFileSize) throws IOException {
		
		// check the parameters
		try {
			if(FileUtils.isFileAccessible(inputFile.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file");
			}
			
			System.out.println("Processing file: " + inputFile.getCanonicalPath());
			
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// check to see if this file should be ignored
		if(OsmBBoxSplit.ignoreList.size() > 0) {
			if(OsmBBoxSplit.ignoreList.contains(inputFile.getCanonicalPath())) {
				System.out.println("WARNING: File specified in the ignore list, skipping...");
				return;
			}
		}
		
		// read the data in the file
		BlockInputStream blockinput;
		BinaryDataParser dataParser = new BinaryDataParser();
		try {
			blockinput = (new BlockInputStream(new FileInputStream(inputFile), dataParser));
		} catch (FileNotFoundException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// output some information
		try {
			blockinput.process();
			
			System.out.println("File Size: " + FileUtils.humanReadableByteCount(inputFile.length(), true));
		} catch (IOException e) {
			throw new IOException("unable to process the required file", e);
		} finally {
			blockinput.close();
		}
		
		// determine if we need to split the file
		if(inputFile.length() >= (minFileSize * 1000 * 1000)) {
			// file is over the minimum file size so try to split
			
			double minLat = dataParser.getGeoCoordinates()[0];
			double minLng = dataParser.getGeoCoordinates()[1];
			double maxLat = dataParser.getGeoCoordinates()[2];
			double maxLng = dataParser.getGeoCoordinates()[3];
			
			boolean latOk = false;
			boolean lngOk = false;
			
			// check to make sure that the all of the lats and longs are of the same size
			if((minLat < 0 && maxLat < 0) || (minLat > 0 && maxLat > 0)) {
				latOk = true;
			}
			
			if((minLng < 0 && maxLng < 0) || (minLng > 0 && maxLng > 0)) {
				lngOk = true;
			}
			
			if(!latOk || !lngOk) {
				System.out.println("Error: bounding box spans equater or prime meridian, can't split");
				return;
			}
			
			// calculate the differences
			double diffLat = (maxLat - minLat) / 2;
			double diffLng = (maxLng - minLng) / 2;
			
			// calculate the new bounding boxes
			double newLat = minLat + diffLat;
			double newLng = minLng + diffLng;
			
			// output the new definitions
			System.out.println("BBox A lat/lng: " + newLat + ", " + minLng + " - " + maxLat + ", " + newLng);
			System.out.println("URL: " + String.format(BinaryDataParser.URL_FORMAT, minLng, newLat, newLng, maxLat));
			
			System.out.println("BBox B lat/lng: " + newLat + ", " + newLng + " - " + maxLat + ", " + maxLng);
			System.out.println("URL: " + String.format(BinaryDataParser.URL_FORMAT, newLng, newLat, maxLng, maxLat));
			
 			System.out.println("BBox C lat/lng: " + minLat + ", " + minLng + " - " + newLat + ", " + newLng);
 			System.out.println("URL: " + String.format(BinaryDataParser.URL_FORMAT, minLng, minLat, newLng, newLat));
 			
 			System.out.println("BBox D lat/lng: " + minLat + ", " + newLng + " - " + newLat + ", " + maxLng);
 			System.out.println("URL: " + String.format(BinaryDataParser.URL_FORMAT, newLng, minLat, maxLng, newLat));
 			
 			// create a new script
 			if(template != null) {
 				
 				String scriptContents = new String(template);
 				
 				// add missing information
 				scriptContents = scriptContents.replace("{{INPUT_PATH}}", inputFile.getCanonicalPath());
 				scriptContents = scriptContents.replace("{{OUTPUT_PATH}}", inputFile.getCanonicalFile().getParent());
 				
 				//replace all of the a quadrant variables
 				scriptContents = scriptContents.replace("{{BBOX_A_BOTTOM}}", Double.toString(newLat));
 				scriptContents = scriptContents.replace("{{BBOX_A_LEFT}}", Double.toString(minLng));
 				scriptContents = scriptContents.replace("{{BBOX_A_TOP}}", Double.toString(maxLat));
 				scriptContents = scriptContents.replace("{{BBOX_A_RIGHT}}", Double.toString(newLng));
 				scriptContents = scriptContents.replace("{{BBOX_A_FILE}}", inputFile.getName().replace(".osm.pbf", "_a.osm.pbf"));
 				
 				scriptContents = scriptContents.replace("{{BBOX_B_BOTTOM}}", Double.toString(newLat));
 				scriptContents = scriptContents.replace("{{BBOX_B_LEFT}}", Double.toString(newLng));
 				scriptContents = scriptContents.replace("{{BBOX_B_TOP}}", Double.toString(maxLat));
 				scriptContents = scriptContents.replace("{{BBOX_B_RIGHT}}", Double.toString(maxLng));
 				scriptContents = scriptContents.replace("{{BBOX_B_FILE}}", inputFile.getName().replace(".osm.pbf", "_b.osm.pbf"));
 				
 				scriptContents = scriptContents.replace("{{BBOX_C_BOTTOM}}", Double.toString(minLat));
 				scriptContents = scriptContents.replace("{{BBOX_C_LEFT}}", Double.toString(minLng));
 				scriptContents = scriptContents.replace("{{BBOX_C_TOP}}", Double.toString(newLat));
 				scriptContents = scriptContents.replace("{{BBOX_C_RIGHT}}", Double.toString(newLng));
 				scriptContents = scriptContents.replace("{{BBOX_C_FILE}}", inputFile.getName().replace(".osm.pbf", "_c.osm.pbf"));
 				
 				scriptContents = scriptContents.replace("{{BBOX_D_BOTTOM}}", Double.toString(minLat));
 				scriptContents = scriptContents.replace("{{BBOX_D_LEFT}}", Double.toString(newLng));
 				scriptContents = scriptContents.replace("{{BBOX_D_TOP}}", Double.toString(newLat));
 				scriptContents = scriptContents.replace("{{BBOX_D_RIGHT}}", Double.toString(maxLng));
 				scriptContents = scriptContents.replace("{{BBOX_D_FILE}}", inputFile.getName().replace(".osm.pbf", "_d.osm.pbf"));
 				
 				// write the file
 				try {
 					
 					File newFile = new File(outputDir.getCanonicalPath() + File.separator + inputFile.getName() + ".sh");
 					
 					System.out.println("writing script file:\n" + newFile.getCanonicalPath());
 					
	 				org.apache.commons.io.FileUtils.writeStringToFile(newFile, scriptContents);
	 				
	 				newFile.setExecutable(true);
 					
 				} catch(IOException e) {
 					throw new IOException("unable to write the script file.", e);
 				}
 			}

		}
		
		
	}
	
	/**
	 * read a directory looking for OSM PBF files to process
	 * 
	 * @param inputDir path to the parent directory
	 * @param outputDir path to the output directory
	 * @param template the contents of the template file
	 * @param minFileSize the minimum file size, in MB, of files to process
	 * @throws IOException if a file / directory cannot be accessed
	 */
	public static void readFilesInDir(File inputDir, File outputDir, String template, int minFileSize) throws IOException {
		
		// check the parameters
		try {
			if(FileUtils.isDirectoryAccessible(inputDir.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file");
			}
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// get a list of map files in the directory
		File[] fileList = FileUtils.getFileListWithDirs(inputDir, ".osm.pbf");
		
		// test each of the files in turn
		for(File file: fileList) {
			
			// check to see if this is a subdirectory
			if(file.isDirectory()) {
				readFilesInDir(file, outputDir, template, minFileSize);
			} else {
				readFile(file, outputDir, template, minFileSize);
			}
		}
		
	}

}
