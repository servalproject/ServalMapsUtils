/*
 * Copyright (C) 2012 The Serval Project
 *
 * This file is part of the Serval Maps OSM PBF Metadata Reader Software
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
package org.servalproject.maps.osmpbfmetadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.servalproject.maps.osmpbfmetadata.utils.FileUtils;

import crosby.binary.file.BlockInputStream;

/**
 * class used to read data from a OSM PBF file
 */
public class OsmPbfReader {
	
	/**
	 * read the metadata / header information from an PSM PBF file
	 * @param inputPath the path to the file to be read
	 * @return true if the file could be successfully read
	 * @throws IOException if a file cannot be accessed
	 */
	public static boolean readFile(File inputPath) throws IOException {
		
		boolean status = true;
		
		// check the parameters
		try {
			if(FileUtils.isFileAccessible(inputPath.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file");
			}
			
			System.out.println("Processing file: " + inputPath.getCanonicalPath());
			
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		BlockInputStream blockinput;
		try {
			blockinput = (new BlockInputStream(new FileInputStream(inputPath), new BinaryDataParser()));
		} catch (FileNotFoundException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		try {
			blockinput.process();
			
			System.out.println("File Size: " + FileUtils.humanReadableByteCount(inputPath.length(), true));
		} catch (IOException e) {
			throw new IOException("unable to process the required file", e);
		} finally {
			blockinput.close();
		}
		
		return status;
		
	}
	
	
	/**
	 * read all of the files in a directory
	 * @param inputPath the path to the directory containing the files
	 * @return true if all files were able to be read
	 * @throws IOException if a file or directory cannot be accessed
	 */
	public static boolean readFilesInDir(File inputPath) throws IOException {
		
		boolean status = true;
		
		// check the parameters
		try {
			if(FileUtils.isDirectoryAccessible(inputPath.getCanonicalPath()) == false) {
				throw new IOException("unable to access the required file");
			}
		} catch (IOException e) {
			throw new IOException("unable to access the required file", e);
		}
		
		// get a list of map files in the directory
		File[] fileList = FileUtils.getFileListWithDirs(inputPath, ".osm.pbf");
		
		// test each of the files in turn
		for(File file: fileList) {
			
			// check to see if this is a subdirectory
			if(file.isDirectory()) {
				readFilesInDir(file);
			} else {
				if(readFile(file) == false) {
					status = false;
				}
			}
		}
		
		// return the status for the directory
		return status;
	}

}
