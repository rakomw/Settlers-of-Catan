public class Board {
	// all array indexes represent row-major item locations on the map
	private static Tile[][] tiles = {new Tile[3], new Tile[4], new Tile[5], new Tile[4], new Tile[3]};
	private static int[] robber_loc = new int[2];
	private static final int total_tiles = 19;
	//even rows are diagonal, odd are vertical
	private static RoadNode[][] roads = {new RoadNode[6], new RoadNode[4], 
								  new RoadNode[8], new RoadNode[5], 
								  new RoadNode[10], new RoadNode[6], 
								  new RoadNode[10], new RoadNode[5],
								  new RoadNode[8], new RoadNode[4],
								  new RoadNode[6]};
	private static TownNode[][] towns = {new TownNode[7], new TownNode[9], new TownNode[11], new TownNode[11], new TownNode[9], new TownNode[7]};
	private static int ref_count = 0;

	public Board() {
		// put tiles onto the map
		int r = 0, c = 0;
		{
			// limit the # of any tile score to 2 but keep the tile scores random
			int[][] tile_scores = new int[tiles.length][];
			for (r = 0; r < tile_scores.length; r++) {
				tile_scores[r] = new int[tiles[r].length];
			}
			int score = 2;
			for (r = 0; r < tile_scores.length; r++) {
				for (c = 0; c < tile_scores[r].length; c++) {
					if (score == 13) {
						score = 2;
					}
					tile_scores[r][c] = score;
					score++;
				}
			}
			System.out.println("randomize the roll scores for the tiles");
			for (int x = 0; x < 1000; x++) {
				int[] rows = {(int)(Math.random() * 5.0), 
						   (int)(Math.random() * 5.0)};
				
				int[] cols = {(int)(Math.random() * (double)tiles[rows[0]].length), 
						   (int)(Math.random() * (double)tiles[rows[1]].length)};
				
				int temp = tile_scores[rows[0]][cols[0]];
				tile_scores[rows[0]][cols[0]] = tile_scores[rows[1]][cols[1]];
				tile_scores[rows[1]][cols[1]] = temp;
			}
			
			System.out.println("initial construction of tiles");
			int tile_count = 0;
			for (r = 0; r < tiles.length; r++) {
				for (c = 0; c < tiles[r].length; c++) {
					if (tile_count < 3) {
						tiles[r][c] = new Tile(Tile.BRICK, tile_scores[r][c]);
					}
					else if (tile_count < 6) {
						tiles[r][c] = new Tile(Tile.ORE, tile_scores[r][c]);
					}
					else if (tile_count < 10) {
						tiles[r][c] = new Tile(Tile.GRAIN, tile_scores[r][c]);
					}
					else if (tile_count < 14) {
						tiles[r][c] = new Tile(Tile.WOOL, tile_scores[r][c]);
					}
					else if (tile_count < 18) {
						tiles[r][c] = new Tile(Tile.LUMBER, tile_scores[r][c]);
					}
					else {
						tiles[r][c] = new Tile(Tile.DESERT, -1);
						tiles[r][c].has_robber = true;
						robber_loc[0] = r;
						robber_loc[1] = c;
					}
					tile_count++;
				}
			}
			
			System.out.println("switch random tiles to randomize the map");
			for (int x = 0; x < 10000; x++) {
				int[] rows = {(int)(Math.random() * 5.0), 
						   (int)(Math.random() * 5.0)};
				
				int[] cols = {(int)(Math.random() * (double)tiles[rows[0]].length), 
						   (int)(Math.random() * (double)tiles[rows[1]].length)};
				
				Tile tile1 = tiles[rows[0]][cols[0]], 
					 tile2 = tiles[rows[1]][cols[1]], 
					 temp = tile1;
				
				tile1 = tile2;
				tile2 = temp;
			}
		}
		
		System.out.println("create & instantiate the temporary construction arrays for the nodes");
		RoadNode[][][] road_road_constructors = new RoadNode[roads.length][][]; // for road constructor
		for (r = 0; r < roads.length; r++) {
			road_road_constructors[r] = new RoadNode[roads[r].length][]; // instantiate the varying length of the columns
		}
		TownNode[][][] road_town_constructors = new TownNode[roads.length][][];
		for (r = 0; r < roads.length; r++) {
			road_town_constructors[r] = new TownNode[roads[r].length][2];
		}
		RoadNode[][][] town_road_constructors = new RoadNode[towns.length][][]; // for town constructor, road parameter
		for (r = 0; r < town_road_constructors.length; r++) {
			town_road_constructors[r] = new RoadNode[towns[r].length][];
		}
		TownNode[][][] town_town_constructors = new TownNode[towns.length][][]; // for town constructor, town parameter
		for (r = 0; r < town_town_constructors.length; r++) {
			town_town_constructors[r] = 
			new TownNode[towns[r].length][];
		}
		Tile[][][] town_tile_constructors = new Tile[towns.length][][];
		for (r = 0; r < town_tile_constructors.length; r++) {
			town_tile_constructors[r] = new Tile[towns[r].length][];
		}
		
		System.out.println("instantiate the 3rd level arrays to their correct lengths");
		int length;
		for (r = 0; r < roads.length; r++) { // loop for the road constructors, road parameter
			for (c = 0; c < roads[r].length; c++) {
				// find the correct size of the array
				length = 4;
				if (r == 0 || r == roads.length - 1) {
					length--;
				}
				if (c == 0 || c == roads[r].length - 1) {
					length--;
				}
				road_road_constructors[r][c] = new RoadNode[length];
			}
		}
		// every road has the same # of adjacent towns, so there doesn't need to be an algorithm to determine the array length
		for (r = 0; r < towns.length; r++) { // loop for the town constructors, road parameter
			for (c = 0; c < towns[r].length; c++) {
				length = 3;
				if (r == 0 || r == towns.length - 1) {
					length--;
				}
				else if (c == 0 || c == towns[r].length - 1) { // else because all towns have at least 2 adj roads
					length--;
				}
				town_road_constructors[r]
						[c] = new RoadNode[length];
			}
		}
		for (r = 0; r < towns.length; r++) { // loop for the town constructors, town parameter
			for (c = 0; c < towns[r].length; c++) {
				length = 3;
				if (r == 0 || r == towns.length - 1) {
					length--;
				}
				else if (c == 0 || c == towns[r].length - 1) {
					length--;
				}
				town_town_constructors[r][c] = new TownNode[length];
			}
		}
		
		System.out.println("instantiate all the roads");
		for (r = 0; r < roads.length; r++) {
			for (c = 0; c < roads[r].length; c++) {
				roads[r][c] = new RoadNode(road_road_constructors[r][c], road_town_constructors[r][c]);
			}
		}
		System.out.println("instantiate all the towns");
		for (r = 0; r < towns.length; r++) {
			for (c = 0; c < towns[r].length; c++) {
				// stupid annoying stuff to tell if the town has a trader
				if ((c == 0 || c == 1) && (r == 0 || r == 5) || (c == 10 && (r == 2 || r == 3))) {
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c], 5); // 3:1 trader
				}
				// 2:1 traders
				else if (r == 0 && (c == 3 || c == 4)) {
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c], Tile.WOOL);
				}
				else if (r == 1 && c == 0 || r == 2 && c == 1) {
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c], Tile.ORE);
				}
				else if (r == 3 && c == 1 || r == 4 && c == 0) {
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c], Tile.GRAIN);
				}
				else if (r == 4 && (c == 7 || c == 8)) {
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c], Tile.BRICK);
				}
				else if (r == 5 && (c == 3 || c == 4)) {
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c], Tile.LUMBER);
				}
				else { // no trader
					towns[r][c] = new TownNode(town_tile_constructors[r][c], town_road_constructors[r][c], town_town_constructors[r][c]);
				}
			}
		}
		System.out.println("link vertical roads");
		for (r = 1; r < roads.length; r += 2) {
			for (c = 1; c < roads[r].length; c++) {
				linkVerticalRoad(r, c, road_road_constructors, road_town_constructors);
			}
		}
		
		System.out.println("link horizontal roads");
		for (r = 0; r < roads.length; r += 2) {
			for (c = 0; c < roads[r].length; c++) {
				linkHorizontalRoad(r, c, road_road_constructors, road_town_constructors);
			}
		}
		System.out.println("instantiate the town nodes");
		for (r = 0; r < towns.length; r++) {
			for (c = 0; c < roads[r].length; c++) {
				linkTown(r, c, town_town_constructors, town_tile_constructors[r][c]);
			}
		}
		System.out.println("dependencies: " + ref_count);
	}
	
	// these are road construction helper methods
	
	// constructs vertical references and references of adjacent horizontals to the vertical road
	void linkVerticalRoad(final int r, final int c, final RoadNode[][][] road_constructors, final TownNode[][][] town_constructors) {
		int other_r, other_c; // used to store the other's location
		if (r == 5) { // at the equator
			// road-road links
			other_r = r - 1;
			other_c = c * 2 - 1;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_r = r + 1;
			other_c = c * 2 - 1;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			
			// road-town links
			other_r = r / 2;
			other_c = c * 2;
			if (onBoard(towns, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
			}
			other_r++;
			if (onBoard(towns, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
			}
		}
		else if (r < 5) { // above equator
			// road-road links
			other_r = r - 1;
			other_c = c * 2;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], 
								road_constructors[other_r][other_c], 
								roads[r][c], 
								roads[other_r][other_c]);
			}
			other_c++; // other_c = c * 2 + 1;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_r = r + 1;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++; // other_c = c * 2 + 2
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			
			// road-town links
			other_r = r / 2;
			other_c = c;
			if (onBoard(towns, other_r, other_c)) {
				linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
			}
			other_r++;
			other_c++;
			if (onBoard(towns, other_r, other_c)) {
				linkReference(town_constructors[r][c], 
						town_constructors[other_r][other_c], 
						towns[r][c], 
						towns[other_r][other_c]);
			}
		}
		else { // below equator
			// road-road links
			other_r = r - 1;
			other_c = c * 2 + 1;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++; // other_c = c * 2 + 2;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_r = r + 1;
			other_c = c * 2;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++; // other_c = c * 2 + 1;
			if (onBoard(roads, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			
			// road-town links
			other_r = r / 2;
			other_c = c + 1;
			if (onBoard(towns, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
			}
			other_r++;
			other_c--;
			if (onBoard(towns, other_r, other_c)) {
				linkReference(road_constructors[r][c], road_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
			}
		}
	}
	// completes the references of horizontal roads
	private void linkHorizontalRoad(final int r, final int c, final RoadNode[][][] road_constructors, final TownNode[][][] town_constructors) {
		// link road-road
		int other_c = c - 1;
		int other_r = r;
		if (onBoard(roads, other_r, other_c)) {
			linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
		}
		other_c = c + 1;
		if (onBoard(roads, r, other_c)) {
			linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
		}
		
		// link road-town
		other_r = r / 2;
		other_c = c;
		if (onBoard(towns, other_r, other_c)) {
			linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
		}
		other_c = c + 1;
		if (onBoard(towns, other_r, other_c)) {
			linkReference(road_constructors[r][c], road_constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
		}
	}
	
	// helper method for town construction, doesn't need to add road references
	private void linkTown(final int r, final int c, final TownNode[][][] town_constructors, Tile[] tile_constructors) {
		int other_r = r, 
			other_c = c - 1;
		// link references to other towns
		if (onBoard(towns, other_r, other_c)) {
			linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
		}
		other_c = c + 1;
		if (onBoard(towns, other_r, other_c)) {
			linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
		}
		if (r < 3) { // in the north of the board
			if (r % 2 == 0) {
				other_r = r + 1;
				other_c = c + 1;
				if (onBoard(towns, other_r, other_c)) {
					linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
				}
			}
			else {
				other_r = r - 1;
				other_c = c - 1;
				if (onBoard(towns, other_r, other_c)) {
					linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
				}
			}
		}
		else { // in the south of the board
			if (r % 2 == 0) {
				other_r = r - 1;
				other_c = c - 1;
				if (onBoard(towns, other_r, other_c)) {
					linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
				}
			}
			else {
				other_r = r + 1;
				other_c = c + 1;
				if (onBoard(towns, other_r, other_c)) {
					linkReference(town_constructors[r][c], town_constructors[other_r][other_c], towns[r][c], towns[other_r][other_c]);
				}
			}
		}
		
		// add references to tiles
		if (r < 3) { // in the north
			if (c % 2 == 0) {
				other_r = r - 1;
				other_c = c / 2 - 1;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_r++;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_c++;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
			}
			else {
				other_r = r - 1;
				other_c = c / 2 - 1;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_c++;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_r++;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
			}
		}
		else { // in the south
			if (c % 2 == 0) {
				other_r = r - 1;
				other_c = c / 2 - 1;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_c++;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_r++;
				other_c--;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
			}
			else {
				other_r = r - 1;
				other_c = c / 2 - 1;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_r++;
				other_c--;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
				other_c++;
				if (onBoard(tiles, other_r, other_c)) {
					addReference(tile_constructors, tiles[other_r][other_c]);
				}
			}
		}
	}
	
	// parameters: the parts of the board to be checked, the location in those parts to be checked
	private boolean onBoard(final Object[][] parts, final int r, final int c) {
		return r >= 0 && r < parts.length && c >= 0 && c < parts[r].length;
	}
	
	private void addReference(Object[] constructors, Object other) {
		for (int x = 0; x < constructors.length; x++) {
			// the or allows this function to be always used without worrying about copied references
			if (constructors[x] == other) {
				break;
			}
			if (constructors[x] == null) {
				constructors[x] = other;
				ref_count++;
				break;
			}
		}
	}
	
	/*
	 * functions for setting up references between two nodes
	 * constructors & others_costructors: the construction arrays to be modified, 
	 * first & other: the objects to be linked
	 * precondition: parameters match up (e.g. others_constructors -> other)
	 */
	private void linkReference(final Node[] constructors, final Node[] others_constructors, final Node first, final Node other) {
		// add to first's references
		addReference(constructors, other);
		// add to other's references
		addReference(others_constructors, first);
	}
	
	public Tile getTileAt(final int row, final int col) {
		return tiles[row][col];
	}
	
	public void moveRobber(final int r, final int c) {
		tiles[robber_loc[0]][robber_loc[1]].has_robber = false;
		robber_loc[0] = r;
		robber_loc[1] = c;
		tiles[r][c].has_robber = true;
	}
	
	public int[] getRobberLoc() {
		int[] output = {robber_loc[0], robber_loc[1]};
		return output;
	}
}