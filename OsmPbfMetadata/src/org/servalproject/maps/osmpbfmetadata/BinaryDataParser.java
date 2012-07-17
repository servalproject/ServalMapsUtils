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

import java.util.List;

import crosby.binary.BinaryParser;
import crosby.binary.Osmformat.DenseNodes;
import crosby.binary.Osmformat.HeaderBlock;
import crosby.binary.Osmformat.Node;
import crosby.binary.Osmformat.Relation;
import crosby.binary.Osmformat.Way;
import crosby.binary.file.FileBlockPosition;

/**
 * class used to parse the binary map file
 */
public class BinaryDataParser extends BinaryParser {
	
	/*
	 * private class level constats
	 */
	private static final double MULTIPLIER = .000000001;
	private static final String HEADER_TYPE = "OSMHeader";

	/*
	 * (non-Javadoc)
	 * @see crosby.binary.file.BlockReaderAdapter#complete()
	 */
	@Override
	public void complete() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see crosby.binary.BinaryParser#parse(crosby.binary.Osmformat.HeaderBlock)
	 */
	@Override
	protected void parse(HeaderBlock block) {
	
		// check to see if this block has a bounding box
		if(block.hasBbox()) {
			
			double rightf = truncateDouble(block.getBbox().getRight() * MULTIPLIER);
			double leftf = truncateDouble(block.getBbox().getLeft() * MULTIPLIER);
			double topf = truncateDouble(block.getBbox().getTop() * MULTIPLIER);
			double bottomf = truncateDouble(block.getBbox().getBottom() * MULTIPLIER);
			
			System.out.println("Bounding box: " + topf + ", " + leftf + " - " + bottomf + ", " + rightf);			
		}
		
	}
	
	/*
	 * only process header blocks, skip all other blocks
	 * (non-Javadoc)
	 * @see crosby.binary.BinaryParser#skipBlock(crosby.binary.file.FileBlockPosition)
	 */
	@Override
	public boolean skipBlock(FileBlockPosition message) {
		
		return !message.getType().equals(HEADER_TYPE);

	}
	
	// truncate the coordinates to 5 decimal places, the multiplier makes them too big
	// but without a multiplier the values can not be retrieved
	private double truncateDouble(double val) {
		
		val = val * 100000;
		val = Math.round(val);
		return val / 100000;
		
	}

	/*
	 * (non-Javadoc)
	 * @see crosby.binary.BinaryParser#parseDense(crosby.binary.Osmformat.DenseNodes)
	 */
	@Override
	protected void parseDense(DenseNodes arg0) {
		// unused method, we only needed the parse method for the header block
	}



	/*
	 * (non-Javadoc)
	 * @see crosby.binary.BinaryParser#parseRelations(java.util.List)
	 */
	@Override
	protected void parseRelations(List<Relation> arg0) {
		// unused method, we only needed the parse method for the header block
	}

	/*
	 * (non-Javadoc)
	 * @see crosby.binary.BinaryParser#parseWays(java.util.List)
	 */
	@Override
	protected void parseWays(List<Way> arg0) {
		// unused method, we only needed the parse method for the header block
	}

	/*
	 * (non-Javadoc)
	 * @see crosby.binary.BinaryParser#parseNodes(java.util.List)
	 */
	@Override
	protected void parseNodes(List<Node> arg0) {
		// unused method, we only needed the parse method for the header block
	}

}
