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
package org.servalproject.maps.osmpbfmetadata.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * class used to filter file names when building a file list
 */
public class FileNameFilter implements FilenameFilter {

	/*
	 * private class level variables
	 */
	private String start;
	private String ext;
	private boolean withDirs;
	
	public FileNameFilter(String start, String ext) {
		this(start, ext, false);
	}

	public FileNameFilter(String start, String ext, boolean withDirs) {

		// store the prefix for later
		// explicitly set to null if the start is not provided
		if(StringUtils.isEmpty(start) == true) {
			this.start = "*";
		} else {
			this.start = start.trim().toLowerCase();
		}

		// store the extension for later
		// explicitly set to null of the extension is not provided

		if(StringUtils.isEmpty(ext) == true) {
			this.ext = "";
		} else {
			this.ext = ext.trim().toLowerCase();
		}

		this.start = start;
		this.ext   = ext;
		this.withDirs = withDirs;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File directory, String filename) {
		
		// return subdirectories
		if(withDirs) {
			
			// use the full canonical path to stop Java from treating paths as relative to the
			// current working directory
			File testDir;
			try {
				testDir = new File(directory.getCanonicalPath() + File.separatorChar + filename);
			} catch (IOException e) {
				return false;
			}

			if(testDir.isDirectory() && !testDir.getName().equals(".") && !testDir.getName().equals("..")) {
				return true;
			}
		}

		if(start.equals("*") == false) {
			
			// return matching file names
			if(filename.startsWith(start) && filename.endsWith(ext)) {
				return true;
			} else {
				return false;
			}
		} else {
			if(filename.endsWith(ext)) {
				return true;
			} else {
				return false;
			}
		}
	}
}
