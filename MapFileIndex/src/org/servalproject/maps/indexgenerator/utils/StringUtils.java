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
package org.servalproject.maps.indexgenerator.utils;

import java.util.Arrays;

/**
 * a collection of string related utility methods
 */
public class StringUtils {
	
	/**
	 * Check to see if a string is empty.
	 * 
	 * @param string the string to evaluate
	 * @return       true if the string is valid
	 */
	public static boolean isEmpty(String string) {
		if(string == null) {
			return true;
		}

		if(string.trim().equals("") == true) {
			return true;
		}

		return false;
	}
	
	/**
	 * check to see if the given value is in the array
	 * @param needle the value to look for
	 * @param haystack the array to look in
	 * @return
	 */
	public static boolean isInArray(String needle, String[] haystack) {
		
		return Arrays.asList(haystack).contains(needle);
		
	}
	
	/**
	 * build a string based on an array of strings, uses the default delimiter ', '
	 * @param haystack the array of strings to use
	 * @return a string containing all of the elements of the array
	 */
	public static String arrayAsString(String[] haystack) {
		return arrayAsString(haystack, ", ");
	}
	
	/**
	 * build a string based on an array of strings
	 * @param haystack the array of strings to use
	 * @param delimiter the delimiter to use between strings
	 * @return a string containing all of the elements of the array
	 */
	public static String arrayAsString(String[] haystack, String delimiter) {
		
		StringBuilder builder = new StringBuilder();
		
		for(String item: haystack) {
			builder.append(item + delimiter);
		}
		
		String value = builder.toString();
		
		return value.substring(0, value.length() - delimiter.length());
		
	}

}
