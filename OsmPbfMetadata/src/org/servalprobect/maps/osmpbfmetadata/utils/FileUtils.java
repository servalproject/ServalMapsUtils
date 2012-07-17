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
package org.servalprobect.maps.osmpbfmetadata.utils;

import java.io.File;

/** 
 * a collection of file utility methods
 */
public class FileUtils {

	/**
	 * Confirm that a file is accessible
	 * 
	 * @param path the path to check
	 * 
	 * @return true if the file at the supplied path can accessed
	 */
	public static boolean isFileAccessible(String path) {
		if(StringUtils.isEmpty(path) == true) {
			return false;
		}

		File mFile = new File(path);

		if(mFile.isFile() == true && mFile.canRead() == true) {
			return true;
		}

		return false;
	}

	/**
	 * Confirm that a directory is accessible
	 * 
	 * @param path the path to check
	 * 
	 * @return true if the file at the supplied path can accessed
	 */
	public static boolean isDirectoryAccessible(String path) {
		if(StringUtils.isEmpty(path) == true) {
			return false;
		}

		File mFile = new File(path);

		if(mFile.isDirectory() == true && mFile.canRead() == true) {
			return true;
		}

		return false;
	}

	/**
	 * return a list of files when provided a directory and extension
	 * @param inputDir
	 * @param fileExtension
	 * @return an array of files that match the provided criteria
	 */
	public static File[] getFileList(File inputDir, String fileExtension) {

		FileNameFilter filter = new FileNameFilter("*", fileExtension);

		File[] fileList = inputDir.listFiles(filter);

		if(fileList == null) {
			return new File[0];
		} else {
			return fileList;


		}
	}

	/**
	 * return a list of files, including sub directories, when provided a directory and extension
	 * @param inputDir
	 * @param fileExtension
	 * @return an array of files that match the provided criteria
	 */
	public static File[] getFileListWithDirs(File inputDir, String fileExtension) {

		FileNameFilter filter = new FileNameFilter("*", fileExtension, true);

		File[] fileList = inputDir.listFiles(filter);

		if(fileList == null) {
			return new File[0];
		} else {
			return fileList;
		}
	}

	/**
	 * format the size of a file in a human readable format
	 * 
	 * code is sourced from http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java/3758880#3758880
	 * and considered to be in the public domain
	 * 
	 * @param bytes the size of the file
	 * @param binary output the size using binary units
	 * @return the human readable representation of the size of the file
	 */
	public static String humanReadableByteCount(long bytes, boolean binary) {
		int unit = binary ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (binary ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (binary ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
}
