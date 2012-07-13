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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.servalproject.maps.indexgenerator.types.MapInfo;
import org.servalproject.maps.indexgenerator.utils.FileUtils;
import org.servalproject.maps.indexgenerator.utils.StringUtils;

public class MapIndexGenerator {

	/**
	 * name of the app
	 */
	public static final String APP_NAME    = "Serval Maps Index File Generator";

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
	
	/*
	 * class level private constants
	 */
	private static final String[] FORMAT_TYPES = {"json", "xml"};

	/**
	 * main method of the main class of the application
	 * 
	 * @param args an array of command line arguments
	 */
	public static void main(String[] args) {
		
		/*
		 *  parse the command line options
		 */
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
		
		String inputPath = cmd.getOptionValue("input");
		
		if(StringUtils.isEmpty(inputPath)) {
			printCliHelp("Error: the path to the input directory is required");
		}

		if(FileUtils.isDirectoryAccessible(inputPath) == false) {
			printCliHelp("Error: the input directory is not accessible");
		}
		
		String outputPath = cmd.getOptionValue("output");
		
		if(StringUtils.isEmpty(outputPath)) {
			printCliHelp("Error: the path to the output file is required");
		}
		
		if(FileUtils.isFileAccessible(outputPath) == true) {
			printCliHelp("Error: the specified output file already exists");
		}
		
		String formatType = cmd.getOptionValue("format");
		
		if(StringUtils.isEmpty(formatType)) {
			printCliHelp("Error: the format type is required");
		}
		
		if(StringUtils.isInArray(formatType, FORMAT_TYPES) == false) {
			printCliHelp("Error: the format type is invalid, must be one of: '" + StringUtils.arrayAsString(FORMAT_TYPES));
		}
		
		// build the index
		ArrayList<MapInfo> mapInfoList = null;
		
		try {
			mapInfoList = IndexBuilder.buildIndex(new File(inputPath));
		} catch (IOException e) {
			System.err.println("ERROR: unable to build index: " + e.getMessage());
			System.exit(-1);
		}
		
		// output the index
		if(formatType.equals(FORMAT_TYPES[0])) {
			if(IndexWriter.writeJsonIndex(new File(outputPath), mapInfoList) == false) {
				System.exit(-1);
			}
		} else {
			if(IndexWriter.writeXmlIndex(new File(outputPath), mapInfoList) == false) {
				System.exit(-1);
			}
		}
		
		
		
		

	}
	
	/*
	 * output the command line options help
	 */
	private static void printCliHelp(String message) {
		System.out.println(message);
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("java -jar MapIndexGenerator.jar", createOptions());
		System.exit(-1);
	}
	
	/*
	 * create the command line options used by the app
	 */
	private static Options createOptions() {

		Options options = new Options();
		
		OptionBuilder.withArgName("path");
		OptionBuilder.hasArg(true);
		OptionBuilder.withDescription("path to the input directory");
		OptionBuilder.isRequired(true);
		options.addOption(OptionBuilder.create("input"));
		
		OptionBuilder.withArgName("path");
		OptionBuilder.hasArg(true);
		OptionBuilder.withDescription("path to the output file");
		OptionBuilder.isRequired(true);
		options.addOption(OptionBuilder.create("output"));
		
		OptionBuilder.withArgName("text");
		OptionBuilder.hasArg(true);
		OptionBuilder.withDescription("output format type");
		OptionBuilder.isRequired(true);
		options.addOption(OptionBuilder.create("format"));
		
		return options;
		
	}

}
