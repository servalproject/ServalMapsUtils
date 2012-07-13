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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * a number of useful date related methods
 */
public class DateUtils {
	
	/**
	 * format the provided date / time into a textual representation
	 * @param date the date to format
	 * @return the date formatted as a long textual representation
	 */
	public static String dateAsFullString(long date) {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		
		SimpleDateFormat formatDate = new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm:ss zzz");
		return formatDate.format(calendar.getTime());
		
	}

}
