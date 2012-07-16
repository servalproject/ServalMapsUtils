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
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.servalproject.maps.indexgenerator.types.MapInfo;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * writes an index file in the specified format
 */
public class IndexWriter {
	
	/*
	 * private class level variables
	 */
	private static String VERSION = "1.0";
	private static String AUTHOR = "The Serval Project";
	private static String DATA_SOURCE = "Copyright OpenStreetMap contributors, CC BY-SA";
	private static String DATA_FORMAT = "mapsforge";
	private static String DATA_FORMAT_INFO = "http://code.google.com/p/mapsforge/";
	private static String DATA_FORMAT_VERSION = "3";
	private static String MORE_INFO = "http://maps.servalproject.org";
	
	/**
	 * write the index json file
	 * @param outputFile the path to the output file
	 * @param mapInfoList a list of MapInfo objects contained in the index
	 * @return true if the file is 
	 */
	@SuppressWarnings("unchecked")
	public static boolean writeJsonIndex(File outputFile, ArrayList<MapInfo> mapInfoList) {
		
		//build the json object
		JSONObject jsonObject = new JSONObject();
		
		jsonObject.put("version", VERSION);
		jsonObject.put("generated", System.currentTimeMillis());
		jsonObject.put("author", AUTHOR);
		jsonObject.put("DataSource", DATA_SOURCE);
		jsonObject.put("DataFormat", DATA_FORMAT);
		jsonObject.put("DataFormatInfo", DATA_FORMAT_INFO);
		jsonObject.put("DataFormatVersion", DATA_FORMAT_VERSION);
		jsonObject.put("moreInfo", MORE_INFO);
		
		// build the json array of objects
		JSONArray jsonArray = new JSONArray();
		
		jsonArray.addAll(mapInfoList);
		
		// add list of objects
		jsonObject.put("mapFiles", jsonArray);
		
		//debug code
		System.out.println(jsonObject.toString());
		
		// write the file
		try {
			PrintWriter writer = new PrintWriter(outputFile);
			writer.print(jsonObject.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: unable to write output file. " + e.getMessage());
			return false;
		}
		
		return true;
	}
	
	public static boolean writeXmlIndex(File outputFile, ArrayList<MapInfo> mapInfoList) {
		
		// create the xml document builder factory object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        // create the xml document builder object and get the DOMImplementation object
        DocumentBuilder builder = null;
        try {
                builder = factory.newDocumentBuilder();
        } catch (javax.xml.parsers.ParserConfigurationException e) {
        	System.err.println("ERROR: unable to build the XML data. " + e.getMessage());
        	return false;
        }
        
        DOMImplementation domImpl = builder.getDOMImplementation();
        
        // start to build the document
        Document document = domImpl.createDocument(null, "maps", null);
        
        // get the root element
        Element rootElement = document.getDocumentElement();
        
        // add the basic metadata
        Element element = document.createElement("version");
        element.setTextContent(VERSION);
        rootElement.appendChild(element);
        
        element = document.createElement("generated");
        element.setTextContent(Long.toString(System.currentTimeMillis()));
        rootElement.appendChild(element);
        
        element = document.createElement("author");
        element.setTextContent(AUTHOR);
        rootElement.appendChild(element);
        
        element = document.createElement("data_source");
        element.setTextContent(DATA_SOURCE);
        rootElement.appendChild(element);
        
        element = document.createElement("data_format");
        element.setTextContent(DATA_FORMAT);
        rootElement.appendChild(element);
        
        element = document.createElement("data_format_info");
        element.setTextContent(DATA_FORMAT_INFO);
        rootElement.appendChild(element);
        
        element = document.createElement("data_format_version");
        element.setTextContent(DATA_FORMAT_VERSION);
        rootElement.appendChild(element);
        
        element = document.createElement("more_info");
        element.setTextContent(MORE_INFO);
        rootElement.appendChild(element);
        
        // add the map file information
        Element mapInfoElement = document.createElement("map-info");
        rootElement.appendChild(mapInfoElement);
        
        for(MapInfo info : mapInfoList) {
        	mapInfoElement.appendChild(info.toXml(document.createElement("map")));
        }
        
        // output the xml
        try {
            // create a transformer 
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer        transformer  = transFactory.newTransformer();
            
            // set some options on the transformer
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            // get a transformer and supporting classes
            StreamResult result = new StreamResult(new PrintWriter(outputFile));
            DOMSource    source = new DOMSource(document);
            
            // transform the internal objects into XML and print it
            transformer.transform(source, result);

	    } catch (javax.xml.transform.TransformerException e) {
	    	System.err.println("ERROR: unable to write the XML data. " + e.getMessage());
        	return false;
	    } catch (FileNotFoundException e) {
	    	System.err.println("ERROR: unable to write the XML data. " + e.getMessage());
        	return false;
		}
        
        return true;
	}
}
