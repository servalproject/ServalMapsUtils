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
package org.servalproject.maps.indexgenerator.types;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.servalproject.maps.indexgenerator.utils.DateUtils;
import org.servalproject.maps.indexgenerator.utils.FileUtils;
import org.w3c.dom.Element;

/**
 * a class to store information about the map
 */
public class MapInfo implements Comparable<MapInfo>, JSONAware {
	
	/*
	 * private class level variables
	 */
	private String fileName;
	private double minLatitude;
	private double minLongitude;
	private double maxLatitude;
	private double maxLongitude;
	private long fileDate;
	private long fileSize;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public double getMinLatitude() {
		return minLatitude;
	}
	public void setMinLatitude(double minLatitude) {
		this.minLatitude = minLatitude;
	}
	public double getMinLongitude() {
		return minLongitude;
	}
	public void setMinLongitude(double minLongitude) {
		this.minLongitude = minLongitude;
	}
	public double getMaxLatitude() {
		return maxLatitude;
	}
	public void setMaxLatitude(double maxLatitude) {
		this.maxLatitude = maxLatitude;
	}
	public double getMaxLongitude() {
		return maxLongitude;
	}
	public void setMaxLongitude(double maxLongitude) {
		this.maxLongitude = maxLongitude;
	}
	public long getFileDate() {
		return fileDate;
	}
	public void setFileDate(long fileDate) {
		this.fileDate = fileDate;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/*
	 * implement comparable related methods
	 */
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if((o instanceof MapInfo) == false) {
			return false;
		}
		
		MapInfo m = (MapInfo) o;
		
		return m.fileName.equals(this.fileName);
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
		
		return 31*this.fileName.hashCode();
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("File Name: " + this.fileName + "\n");
		builder.append("File Date: " + DateUtils.dateAsFullString(this.fileDate) + "\n");
		builder.append("File Size: " + FileUtils.humanReadableByteCount(fileSize, true) + "\n");
		builder.append("Min Lat / Lng: " + this.minLatitude + "," + this.minLongitude + "\n");
		builder.append("Max Lat / Lng: " + this.maxLatitude + "," + this.maxLatitude + "\n");
		
		return builder.toString();
		
    }

    public int compareTo(MapInfo m) {
    	
    	return m.fileName.compareTo(this.fileName);

    }
    
	@SuppressWarnings("unchecked")
	@Override
	public String toJSONString() {
		
		// build a JSON object
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("fileName", this.fileName);
		jsonObject.put("fileDate", this.fileDate);
		jsonObject.put("fileSize", this.fileSize);
		jsonObject.put("minLatitude", this.minLatitude);
		jsonObject.put("minLongitude", this.minLongitude);
		jsonObject.put("maxLatitude", this.maxLatitude);
		jsonObject.put("maxLongitude", this.maxLongitude);
		
		return jsonObject.toJSONString();		
	}
	
	/**
	 * create an xml element representation of the map info
	 * @param element the element to use to create the representation
	 * @return the updated element
	 */
	public Element toXml(Element element) {
		element.setAttribute("file_name", this.fileName);
		element.setAttribute("file_date", Long.toString(this.fileDate));
		element.setAttribute("file_size", Long.toString(this.fileSize));
		element.setAttribute("min_latitude", Double.toString(this.minLatitude));
		element.setAttribute("min_longitude", Double.toString(this.minLongitude));
		element.setAttribute("max_latitude", Double.toString(this.maxLatitude));
		element.setAttribute("max_longitude", Double.toString(this.maxLongitude));
		
		return element;
		
	}
}
