public class Board {
	private static Board instance;
	public Board getInstance() { // board should only be instantiated once
		if (instance == null) {
			return new Board();
		}
		else {
			return null;
		}
	}
	
	private Board() {
		// put tiles onto the map
		int tile_count = 0, r = 0, c = 0;
		for (; r < tiles.length; r++) {
			for (; c < tiles[r].length; c++) {
				if (tile_count < 3) {
					tiles[r][c] = new Tile(Tile.BRICK, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 6) {
					tiles[r][c] = new Tile(Tile.ORE, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 10) {
					tiles[r][c] = new Tile(Tile.GRAIN, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 14) {
					tiles[r][c] = new Tile(Tile.WOOL, (int)(Math.random() * 10) + 2);
				}
				else if (tile_count < 18) {
					tiles[r][c] = new Tile(Tile.LUMBER, (int)(Math.random() * 10) + 2);
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
		
		// switch random tiles to randomize the map
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
		
		// create & instantiate the temporary construction arrays for the nodes
		Node[][][] road_constructors = new Node[roads.length][][]; // for road constructor
		for (r = 0; r < roads.length; r++) {
			road_constructors[r] = new RoadNode[roads[r].length][]; // instantiate the varying length of the columns
		}
		RoadNode[][][] town_road_constructors = new RoadNode[roads.length][][]; // for town constructor, road parameter
		for (r = 0; r < town_road_constructors.length; r++) {
			town_road_constructors[r] = new RoadNode[roads[r].length][];
		}
		TownNode[][][] town_town_constructors = new TownNode[towns.length][][]; // for town constructor, town parameter
		for (r = 0; r < town_road_constructors.length; r++) {
			town_road_constructors[r] = new RoadNode[towns[r].length][];
		}
		
		// instantiate the 3rd level arrays to their correct lengths
		int length;
		for (r = 0; r < roads.length; r++) { // loop for the road constructors
			for (c = 0; c < roads[r].length; c++) {
				// find the correct size of the array
				length = 4;
				if (r == 0 || r == roads.length - 1) {
					length--;
				}
				if (c == 0 || c == roads[r].length - 1) {
					length--;
				}
				road_constructors[r][c] = new RoadNode[length];
			}
		}
		for (r = 0; r < towns.length; r++) { // loop for the town constructors, road parameter
			for (c = 0; c < towns[r].length; c++) {
				length = 3;
				if (r == 0 || r == towns.length - 1) {
					length--;
				}
				else if (c == 0 || c == towns[r].length - 1) { // else because all towns have atleast 2 adj roads
					length--;
				}
				town_road_constructors[r][c] = new RoadNode[length];
			}
		}
		for (r = 0; r < towns.length; r++) {
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

		for (r = 0; r < roads.length; r++) {
			for (c = 0; c < roads[r].length; r++) {
				roads[r][c] = new RoadNode(road_constructors[r][c]);
			}
		}
		// construct vertical roads
		for (r = 1; r < roads.length; r += 2) {
			for (c = 1; c < roads[r].length; c++) {
				constructVerticalRoads(r, c, road_constructors);
			}
		}
		
		// construct horizontal roads
		for (r = 0; r < roads.length; r += 2) {
			for (c = 0; c < roads[r].length; c++) {
				constructHorizontalRoads(r, c, road_constructors);
			}
		}
		// initialize the town nodes
		
	}
	
	// these are road construction helper methods
	
	// constructs vertical references and references of adjacent horizontals to the vertical road
	void constructVerticalRoads(final int r, final int c, final Node[][][] constructors) {
		int index = 0, // used to add to the road's array
			other_r, other_c; // used to store the other's location
		if (r == 5) {
			other_r = r - 1;
			other_c = c * 2 - 1;
			if (onBoard(other_r, other_c)) {
				linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++;
			if (onBoard(other_r, other_c)) {
				linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_r = r + 1;
			other_c = c * 2 - 1;
			if (onBoard(other_r, other_c)) {
				linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
			other_c++;
			if (onBoard(other_r, other_c)) {
				linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			}
		}
		else { // the road is in the poles
			int[] other_cs = {c * 2, c * 2 + 1, c * 2 + 1, c * 2 + 2, };
			
			if (r < 5) { // above the equator
				for (int x = 0; x < other_cs.length; x++) {
					if (x < 2) { // changes the row of the other road
						other_r = r - 1;
						other_c = other_cs[x];
						if (onBoard(other_r, other_c)) {
							linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
						}
					}
					else {
						other_r = r + 1;
						other_c = other_cs[x];
						if (onBoard(other_r, other_c)) {
							linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
						}
					}
				}
			}
			else { // below the equator
				for (int x = 0; x < other_cs.length; x++) {
					if (x < 2) { // changes the row of the other road
						other_r = r + 1;
						other_c = other_cs[x];
						if (onBoard(other_r, other_c)) {
							linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
						}
					}
					else {
						other_r = r - 1;
						other_c = other_cs[x];
						if (onBoard(other_r, other_c)) {
							linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
						}
					}
				}
			}
		}
	}
	// completes the references of horizontal roads
	private void constructHorizontalRoads(final int r, final int c, final Node[][][] constructors) {
		Node[] references = constructors[r][c];
		int x;
		if (onBoard(r, c - 1)) {
			for (x = 0; x < references.length; x++) {
				if (references[x] == null) {
					references[x] = roads[r][c - 1];
				}
			}
		}
		if (onBoard(r, c + 1)) {
			for (x = 0; x < references.length;  x++) {
				if (references[x] == null) {
					references[x] = roads[r][c - 1];
				}
			}
		}
	}
	
	private boolean onBoard(final int r, final int c) {
		return r >= 0 || r < roads.length || c >= 0 || c < roads[r].length;
	}
	
	/*
	 * functions for setting up references between two nodes
	 * constructors & others_costructors: the construction arrays to be modified, 
	 * first & other: the objects to be linked
	 * precondition: parameters match up (e.g. others_constructors -> other)
	 */
	private void linkReference(final Node[] constructors, final Node[] others_constructors, final Node first, final Node other) {
		int x;
		// add to first's references
		for (x = 0; x < constructors.length; x++) {
			if (constructors[x] == null) {
				constructors[x] = other;
				break;
			}
		}
		
		// add to other's references
		for (x = 0; x < others_constructors.length; x++) {
			if (others_constructors[x] == null) {
				others_constructors[x] = first;
				break;
			}
		}
	}
	
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

	public Tile getTileAt(int row, int col) {
		return tiles[row][col];
	}
	
	public void moveRobber() {
		
	}
	
	public int[] getRobberLoc() {
		int[] output = {robber_loc[0], robber_loc[1]};
		return output;
	}
}

/*
 * old stuff I don't want to lose
 * 
 * if (r < 5) {
		other_r = r - 1;
		other_c = c * 2;
		if (onBoard(other_r, other_c)) {
			linkReference(constructors[r][c], constructors[other_r][other_c], roads[r][c], roads[other_r][other_c]);
			index++;
		}
		other_c++; // other_c = c * 2 + 1;
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			index++;
		}
		other_r = r + 1;
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			index++;
		}
		other_c++; // other_c = c * 2 + 2
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			//index++; don't need this, function ends
		}
	}
	else {
		other_r = r - 1;
		other_c = c * 2 + 1;
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			index++;
		}
		other_c++; // other_c = c * 2 + 2;
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			index++;
		}
		other_r = r + 1;
		other_c = c * 2;
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			index++;
		}
		other_c++; // other_c = c * 2 + 1;
		if (onBoard(other_r, other_c)) {
			linkRoadReference(r, c, other_r, other_c, index, constructors);
			//index++; not needed, function ends
		}
	}
	*/
