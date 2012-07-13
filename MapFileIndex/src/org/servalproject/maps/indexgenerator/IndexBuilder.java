/*
 * Copyright (C) 2012 The Serval Project
 *
 * This file is part of the Serval Maps Map Index File Generator
 *
 * Serval Maps Map Index File Generator is free software; you can 
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
package org.servalproject.maps.indexgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.servalproject.maps.indexgenerator.types.MapInfo;
import org.servalproject.maps.indexgenerator.utils.FileUtils;

/**
 * a class used to build the index
 */
public class IndexBuilder {
	
	/*
	 * private class level variables
	 */
	private static String currentDirectory = null;
	
	/**
	 * index a map file
	 * @param inputFile the file to index
	 * @return a MapInfo object containing information about the file
	 * @throws IOException if a file cannot be opened
	 */
	public static MapInfo indexFile(File inputFile) throws IOException {
		
		MapInfo mapInfo = new MapInfo();
		
		// check the parameters
		try {
			if(FileUtils.isFileAccessible(inputFile.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file. '" + inputFile.getCanonicalPath() + "'");
			}
			
			System.out.println("Processing map file: " + inputFile.getCanonicalPath());
			
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		if(currentDirectory == null) {
			currentDirectory = System.getProperty("user.dir");
		}
		
		// open the file
		MapDatabase mapFile = new MapDatabase();
		FileOpenResult result = mapFile.openFile(inputFile);
		
		// check to see if the file was opened successfully
		if(result != FileOpenResult.SUCCESS) {
			throw new IOException("unable to open file '" + result.toString() + "'");
		}
		
		// get the file header
		MapFileInfo mapFileInfo = mapFile.getMapFileInfo();
		
		// populate the map info object
		mapInfo.setFileName(inputFile.getCanonicalPath().replace(currentDirectory, ""));
		mapInfo.setFileSize(mapFileInfo.fileSize);
		mapInfo.setFileDate(mapFileInfo.mapDate);
		
		
		// add the bounding box information
		BoundingBox boundingBox = mapFileInfo.boundingBox;
		
		mapInfo.setMinLatitude(boundingBox.minLatitude);
		mapInfo.setMinLongitude(boundingBox.minLongitude);
		mapInfo.setMaxLatitude(boundingBox.maxLatitude);
		mapInfo.setMaxLongitude(boundingBox.maxLongitude);
		
		return mapInfo;
	}
	
	/**
	 * build an index of all of the map files
	 * @param inputDir the parent directory
	 * @return an ArrayList of MapInfo object
	 * @throws IOException if a directory cannot be accessed
	 */
	public static ArrayList<MapInfo> buildIndex(File inputDir) throws IOException {
		
		ArrayList<MapInfo> mapInfoList = new ArrayList<MapInfo>();
		
		// check the parameter
		try {
			if(FileUtils.isDirectoryAccessible(inputDir.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required path");
			}
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// build the index
		
		// get a list of map files in the directory
		File[] fileList = FileUtils.getFileListWithDirs(inputDir, ".map");
		
		// test each of the files in turn
		for(File file: fileList) {
			
			// check to see if this is a subdirectory
			if(file.isDirectory()) {
				mapInfoList.addAll(buildIndex(file));
			} else {
				try {
					mapInfoList.add(indexFile(file));
				} catch (IOException e) {
					System.err.println("Error: unable to index file.\n" + e.getMessage());
				}
			}
		}
		
		return mapInfoList;	
	}
}
