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
package org.servalproject.maps.mapfiletester.utils;

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

}
