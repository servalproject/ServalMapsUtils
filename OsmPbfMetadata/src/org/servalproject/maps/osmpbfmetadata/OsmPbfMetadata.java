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
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.servalproject.maps.osmpbfmetadata.utils.FileUtils;
import org.servalproject.maps.osmpbfmetadata.utils.StringUtils;


/**
 * main class for the application
 */
public class OsmPbfMetadata {
	
	/**
	 * name of the app
	 */
	public static final String APP_NAME    = "Serval Maps OSM PBF Metadata Reader";

	/**
	 * version of the app
	 */
	public static final String APP_VERSION = "1.0";

	/**
	 * url for more information about the app
	 */
	public static final String MORE_INFO   = "http://developer.servalproject.org/dokuwiki/doku.php?id=content:servalmaps:main_page";

	/**
	 * url for the license info
	 */
	public static final String LICENSE_INFO = "http://www.gnu.org/copyleft/gpl.html";
	
	/**
	 * main method of the main class of the application
	 * 
	 * @param args an array of command line arguments
	 */
	public static void main(String[] args) {
		
		// parse the command line options
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(createOptions(), args);
		}catch(org.apache.commons.cli.ParseException e) {
			// something bad happened so output help message
			printCliHelp("Error in parsing arguments:\n" + e.getMessage());
		}
		
		/*
		 * get and test the command line arguments
		 */

		// input path
		String inputPath = cmd.getOptionValue("input");

		if(StringUtils.isEmpty(inputPath)) {
			printCliHelp("Error: the path to the input file / directory is required");
		}

		if(FileUtils.isFileAccessible(inputPath) == false && FileUtils.isDirectoryAccessible(inputPath) == false) {
			printCliHelp("Error: the input file / directory is not accessible");
		}

		File inputFile = new File(inputPath);
		
		/*
		 * output some text
		 */
		System.out.println(APP_NAME);
		System.out.println("Version: " + APP_VERSION);
		System.out.println("More info: " + MORE_INFO);
		System.out.println("License info: " + LICENSE_INFO + "\n");
		
		// inform user of resources we'll be working on
		try {
			if(inputFile.isDirectory()) {
				System.out.println("Processing OSM PBF files in directory: " + inputFile.getCanonicalPath());
				
				// test the files in the directory
				OsmPbfReader.readFilesInDir(inputFile);
				
			} else {
				
				// test the file
				OsmPbfReader.readFile(inputFile);
			}
		} catch (IOException e) {
			System.err.println("Unable to access file system resources.\n" + e.toString());
			System.exit(-1);
		}
		
	}
	
	/*
	 * output the command line options help
	 */
	private static void printCliHelp(String message) {
		System.out.println(message);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar OsmPbfMetadata.jar", createOptions());
		System.exit(-1);
	}
	
	/*
	 * create the command line options used by the app
	 */
	private static Options createOptions() {

		Options options = new Options();
		
		OptionBuilder.withArgName("path");
		OptionBuilder.hasArg(true);
		OptionBuilder.withDescription("path to the input file / directory");
		OptionBuilder.isRequired(true);
		options.addOption(OptionBuilder.create("input"));
		
		return options;
		
	}

}
