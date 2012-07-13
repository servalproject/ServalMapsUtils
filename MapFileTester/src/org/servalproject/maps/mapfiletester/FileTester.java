/*
 * Copyright (C) 2012 The Serval Project
 *
 * This file is part of the Serval Maps Map File Tester Software
 *
 * Serval Maps Map File Tester Software is free software; you can 
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
package org.servalproject.maps.mapfiletester;

import java.io.File;
import java.io.IOException;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.servalproject.maps.mapfiletester.utils.DateUtils;
import org.servalproject.maps.mapfiletester.utils.FileUtils;

/**
 * methods used to rest a map file
 */
public class FileTester {

	/**
	 * a method to test a map file
	 * @param inputFile the map file to test
	 * @return true if the map passes the tests
	 * @throws IOException
	 */
	public static boolean testFile(File inputFile) throws IOException {
		
		// check the parameters
		try {
			if(FileUtils.isFileAccessible(inputFile.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file");
			}
			
			System.out.println("Processing map file: " + inputFile.getCanonicalPath());
			
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// open the file
		MapDatabase mapFile = new MapDatabase();
		FileOpenResult result = mapFile.openFile(inputFile);
		
		// check to see if the file was opened successfully
		if(result != FileOpenResult.SUCCESS) {
			// the file could not be opened
			System.err.println("ERROR: unable to open the file: " + inputFile.getName());
			System.err.println("DETAILS: " + result.toString());
			
			// play nice and tidy up
			mapFile.closeFile();
			
			return false;
		}
		
		// get information about the map file
		MapFileInfo mapFileInfo = mapFile.getMapFileInfo();
		
		// output some information
		System.out.println("Map created: " + DateUtils.dateAsFullString(mapFileInfo.mapDate));
		
		// get the size of the file
		long fileSize = mapFileInfo.fileSize;
		
		// compare the file sizes
		if(fileSize != inputFile.length()) {
			System.err.println("ERROR: report file size '" + fileSize + "' does not match actual size '" + inputFile.length() + "'");
			
			// play nice and tidy up
			mapFile.closeFile();
			
			return false;
		} else {
			System.out.println("File Size: " + FileUtils.humanReadableByteCount(fileSize, true));
		}
		
		// get the bounding box
		BoundingBox boundingBox = mapFileInfo.boundingBox;
		
		// output the bounding box
		System.out.println("Map bounding box: " 
				+ boundingBox.minLatitude + "," 
				+ boundingBox.minLongitude + ","
				+ boundingBox.maxLatitude + ","
				+ boundingBox.maxLongitude);
		
		// play nice and tidy up
		mapFile.closeFile();
		
		// the file passed the tests
		System.out.println("PASSED.");
		return true;
		
	}
	
	/**
	 * test a series of map files in a directory
	 * @param inputDir the directory containing the map files
	 * @return true if all of the maps pass the test
	 */
	public static boolean testFilesInDir(File inputDir) throws IOException {
		
		boolean status = true;
		
		// check the parameters
		try {
			if(FileUtils.isDirectoryAccessible(inputDir.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file");
			}
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// get a list of map files in the directory
		File[] fileList = FileUtils.getFileListWithDirs(inputDir, ".map");
		
		// test each of the files in turn
		for(File file: fileList) {
			
			// check to see if this is a subdirectory
			if(file.isDirectory()) {
				testFilesInDir(file);
			} else {
				if(testFile(file) == false) {
					status = false;
				}
			}
		}
		
		// return the status for the directory
		return status;
	}
}
